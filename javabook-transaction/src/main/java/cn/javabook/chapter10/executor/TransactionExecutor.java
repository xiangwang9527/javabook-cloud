package cn.javabook.chapter10.executor;

import cn.javabook.chapter10.exception.AccountingProviderException;
import cn.javabook.chapter10.exception.AppListenerQueueException;
import cn.javabook.chapter10.service.TransactionMessageService;
import cn.javabook.chapter10.service.AccountingVoucherService;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.enums.EntryTypeEnum;
import cn.javabook.cloud.core.enums.PayTypeEnum;
import cn.javabook.cloud.core.enums.TradeStatusEnum;
import cn.javabook.cloud.core.parent.BaseListener;
import cn.javabook.chapter10.entity.AccountingVoucher;
import cn.javabook.chapter10.entity.PaymentOrder;
import cn.javabook.chapter10.entity.TransactionMessage;
import cn.javabook.chapter10.service.PaymentOrderService;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;

/**
 * 完成支付订单的处理
 * 
 */
@Component
public class TransactionExecutor extends BaseListener {
	private static final long serialVersionUID = 4998242490907751862L;

	@Resource
	private TransactionMessageService transactionMessageService;

	@Resource
	private PaymentOrderService paymentOrderService;

	@Resource
	private AccountingVoucherService accountingVoucherService;

	/**
	 * 完成支付订单的处理
	 *
	 * @param message
	 * @param paymentOrder
	 * @param params
	 */
	public synchronized void completeTransaction(TransactionMessage message, PaymentOrder paymentOrder, JSONObject params) {
		Integer payment_result = null;
		Integer accounting_voucher_result = null;
		PaymentOrder existPaymentOrder = null;
		AccountingVoucher accountingVoucher = null;
		boolean result_payment = false;
		boolean result_accounting = false;
		long messageid = message.getGuid();

		try {
			// 在缓存中查询支付订单保存状态
			payment_result = (Integer) paymentOrderService.getSavedObject(GlobalConstant.RIDES_PAYMENT_ORDER_SAVED_PREFIX + paymentOrder.getOuterno());
			// 缓存中未创建支付订单
			if (null == payment_result || 0 == payment_result) {
				// 在数据库中查询
				existPaymentOrder = paymentOrderService.findPaymentOrderByOuterNo(paymentOrder.getOuterno());
				// 幂等判断，已有则不创建
				// 数据库中不存在
				if (null == existPaymentOrder) {
					// 实际完成支付订单的方法
					result_payment = completeTransaction(paymentOrder, params);
				} else {// 数据库中已存在
					// 创建支付订单成功
					result_payment = true;
					// 保存支付订单的已存在状态
					paymentOrderService.setSavedObject(GlobalConstant.RIDES_PAYMENT_ORDER_SAVED_PREFIX + paymentOrder.getOuterno(), "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
				}
			} else {
				// 缓存中已创建支付订单
				// 在数据库中查询，防止出现缓存中有而数据库中无的情况
				existPaymentOrder = paymentOrderService.findPaymentOrderByOuterNo(paymentOrder.getOuterno());
				// 幂等判断，已有则不创建
				// 数据库中还不存在
				if (null == existPaymentOrder) {
					result_payment = completeTransaction(paymentOrder, params);
				} else {// 缓存中和数据库中都已存在
					// 创建支付订单成功
					result_payment = true;
				}
			}

			// 在缓存中查询
			accounting_voucher_result = (Integer) accountingVoucherService.getSavedObject(GlobalConstant.RIDES_ACCOUNTINT_VOUCHER_SAVED_PREFIX + paymentOrder.getOuterno());
			// 缓存中不存在会计分录
			if (null == accounting_voucher_result || 0 == accounting_voucher_result) {
				// 在数据库中查询
				accountingVoucher = accountingVoucherService.queryAccountingVoucherByVoucherno(paymentOrder.getOuterno());
				// 幂等判断，已有则不创建
				if (null == accountingVoucher) {
					// 创建会计分录
					result_accounting = createAccountingVoucher(paymentOrder);
				} else {
					// 创建会计分录成功
					result_accounting = true;
					// 保存会计分录的已存在状态
					accountingVoucherService.setSavedObject(GlobalConstant.RIDES_ACCOUNTINT_VOUCHER_SAVED_PREFIX + paymentOrder.getOuterno(), "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
				}
			} else {// 缓存中已存在会计分录
				// 在数据库中查询
				accountingVoucher = accountingVoucherService.queryAccountingVoucherByVoucherno(paymentOrder.getOuterno());
				// 幂等判断，已有则不创建
				if (null == accountingVoucher) {
					// 创建会计分录
					result_accounting = createAccountingVoucher(paymentOrder);
				} else {
					// 创建会计分录成功
					result_accounting = true;
				}
			}
			// 只有支付订单和会计分录同时成功完成，才能删除redis缓存和事务消息
			if (result_payment && result_accounting) {
				// 删除redis中保存的PaymentOrder支付订单缓存信息
				paymentOrderService.removePaymentOrder(GlobalConstant.RIDES_PAYMENT_ORDER_SAVED_PREFIX + paymentOrder.getOuterno());
				// 删除事务消息
				transactionMessageService.removeMessageById(messageid, GlobalConstant.MONGO_TABLE_TRANSACTIONMESSAGE);
			}
		} catch (Exception e) {
			logger.error("完成支付订单异常，消息编码：{}", messageid);
			e.printStackTrace();
			throw AppListenerQueueException.EXCEPTION_LISTENER_QUEUE_COMPLETE_PAYMENT_ORDER;
		}
	}

	/**
	 * 完成支付订单的处理
	 *
	 * @param paymentOrder
	 * @param params
	 * @return
	 */
	public synchronized boolean completeTransaction(PaymentOrder paymentOrder, JSONObject params) {
		boolean flag = false;
		boolean result_payment = false;

		try {
			int result_code = paymentOrderService.completePaymentOrder(paymentOrder, params);
			if (0 == result_code) {
				// 创建支付订单记录，状态为交易完成
				paymentOrder.setStatus(TradeStatusEnum.TRADE_SUCCESS.name());
				flag = paymentOrderService.insertUserPaymentOrder(paymentOrder);
				if (flag) {
					// 创建支付订单成功
					result_payment = true;
					// 保存支付订单的已存在状态
					paymentOrderService.setSavedObject(GlobalConstant.RIDES_PAYMENT_ORDER_SAVED_PREFIX + paymentOrder.getOuterno(), "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
					// 余额
					if (paymentOrder.getPaytype().equals(PayTypeEnum.BALANCE.name())) {
						logger.info("余额支付订单成功，交易订单号：{}，商户订单号：{}", paymentOrder.getTransno(), paymentOrder.getOuterno());
					}
				} else {
					// 创建支付订单异常
					result_payment = false;
					logger.error("创建支付订单记录异常，交易订单号：{}，商户订单号：{}", paymentOrder.getTransno(), paymentOrder.getOuterno());
					//throw AppListenerQueueException.EXCEPTION_LISTENER_QUEUE_CREATE_PAYMENT_ORDER;
				}
			}
		} catch (Exception e) {
			// 创建支付订单异常
			result_payment = false;
			logger.error("创建支付订单记录异常，交易订单号：{}，商户订单号：{}", paymentOrder.getTransno(), paymentOrder.getOuterno());
		}
		return result_payment;
	}

	/**
	 * 创建会计分录
	 *
	 * @param paymentOrder
	 * @return
	 */
	public synchronized boolean createAccountingVoucher(PaymentOrder paymentOrder) {
		boolean flag = false;
		boolean result_accounting = false;
		AccountingVoucher accountingVoucher = sealAccountingVoucher(paymentOrder);

		try {
			flag = accountingVoucherService.createAccountingVoucher(accountingVoucher.getVoucherno(), accountingVoucher.getRequestno(), accountingVoucher.getEntrytype(), 
					accountingVoucher.getOrigin(), accountingVoucher.getPayamount(), accountingVoucher.getChangeamount(), accountingVoucher.getPayeraccountno(),
					accountingVoucher.getReceiveraccountno(), accountingVoucher.getIncome(), accountingVoucher.getCost(), accountingVoucher.getProfit(), accountingVoucher.getRemark());
			if (flag) {
				// 创建会计分录成功
				result_accounting = true;
				// 保存会计分录的已存在状态
				accountingVoucherService.setSavedObject(GlobalConstant.RIDES_ACCOUNTINT_VOUCHER_SAVED_PREFIX + paymentOrder.getOuterno(), "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
				logger.info("创建会计分录成功，商户订单号：{}", paymentOrder.getOuterno());
			} else {
				// 创建会计分录异常
				logger.error("创建会计分录异常，商户订单号：{}", paymentOrder.getOuterno());
			}
		} catch (Exception e) {
			// 创建会计分录异常
			result_accounting = false;
			logger.error("创建会计分录异常，商户订单号：{}", paymentOrder.getOuterno());
			throw AccountingProviderException.EXCEPTION_ACCOUNTING_CREATE;
		}
		return result_accounting;
	}

	/**
	 * 封装会计凭据
	 *
	 * @param paymentOrder
	 * @return
	 */
	private AccountingVoucher sealAccountingVoucher(PaymentOrder paymentOrder) {
		AccountingVoucher accountingVoucher = new AccountingVoucher();
		accountingVoucher.setVoucherno(paymentOrder.getOuterno());
		accountingVoucher.setRequestno(System.currentTimeMillis());
		accountingVoucher.setEntrytype(EntryTypeEnum.BALANCE_PAY.name());
		// 来源系统
		accountingVoucher.setOrigin(paymentOrder.getPaychannel());
		accountingVoucher.setPayamount(paymentOrder.getAmount());
		// 平台帐户变动金额
		accountingVoucher.setChangeamount(0.00);
		// 付款方账号
		accountingVoucher.setPayeraccountno(String.valueOf(paymentOrder.getUserid()));
		// 收款方账号
		accountingVoucher.setReceiveraccountno(String.valueOf(0));
		accountingVoucher.setIncome(0.00);
		accountingVoucher.setCost(0.00);
		accountingVoucher.setProfit(0.00);
		accountingVoucher.setRemark(paymentOrder.getRemark());
		return accountingVoucher;
	}
}
