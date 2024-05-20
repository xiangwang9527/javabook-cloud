package cn.javabook.chapter10.service;

import cn.javabook.chapter10.entity.*;
import cn.javabook.chapter10.exception.AppListenerQueueException;
import cn.javabook.chapter10.exception.AppPayProviderException;
import cn.javabook.cloud.core.enums.*;
import com.javabook.chapter03.entity.*;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.utils.MD5Util;
import cn.javabook.cloud.core.utils.PayUtil;
import cn.javabook.chapter10.dao.RedisDao;
import cn.javabook.chapter10.dao.MySQLDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 支付订单Service接口实现
 * 
 */
@Service
public class PaymentOrderService extends BaseService {
	private static final long serialVersionUID = 2002846460012944421L;

	@Resource
	private RedisDao redisDao;

	@Resource
	private MySQLDao mySQLDao;

	@Resource
	private UserTaskService userTaskService;

	@Resource
	private UserTradeService userTradeService;

	@Resource
	private AccountService accountService;

	// redis：支付订单对象是否已保存
	public Object getSavedObject(String key) {
		return redisDao.get(key);
	}

	// redis：保存支付订单对象（Object，1天失效）
	public void setSavedObject(String key, String value, long expire) {
		if (0 == expire) {
			redisDao.set(key, value);
		} else {
			redisDao.set(key, value, expire);
		}
	}

	// redis：删除待支付订单
	public void removePaymentOrder(String key) {
		redisDao.remove(key);
	}

	/**
	 * 余额支付订单酬金
	 *
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized TransactionMessage balancePay(long taskid, long userid, int paytype, int money, String descript) throws Exception {
		String loginfo = "/balancePay - " + "taskid=" + taskid + "&userid=" + userid + "&paytype=" + paytype + "&money=" + money + "&descript=" + descript;
		logger.info(loginfo);

		// 余额支付无需判断订单的状态，因为订单一定是未发布的
		try {
			// 生成支付订单编码
			long paymentid = nextId(GlobalConstant.GUID_APP_PAYMENT_DATANODE, 1L);
			// 生成支付订单号后缀
			String suffix = MD5Util.md5encode(userid + "-" + paymentid + "-" + System.currentTimeMillis() + "-" + Thread.currentThread().hashCode(), "UTF-8").toUpperCase();
			String out_trade_no = PayUtil.generatorPaySequence(suffix);
			String transno = PayUtil.generatorTransNo();
			// 封装参数
			JSONObject params = new JSONObject();
			params.put("userid", userid);
			params.put("taskid", taskid);
			// 封装用户支付订单实体
			PaymentOrder paymentOrder = sealPaymentOrder(paymentid, userid, taskid, transno, out_trade_no, "支付订单", PayTypeEnum.BALANCE.name(), PayClassEnum.CONSUME.name(),
					PayChannelEnum.APP.name(),1, 0, "", TradeStatusEnum.PAYMENT_SUCCESS.name(), "");
			// 封装事务消息
			TransactionMessage message = new TransactionMessage();
			message.setGuid(nextId(GlobalConstant.GUID_APP_MESSAGE_DATANODE, 1L));
			message.setType(MessageTypeEnum.TRANSACTION.name());
			message.setFlag("");
			message.setQueue(GlobalConstant.MESSAGE_QUEUE_CONSUMER_TRANSACTION_QUEUE);
			message.setContent(JSONObject.toJSONString(paymentOrder));
			message.setParams(params.toJSONString());
			return message;
		} catch (Exception e) {
			logger.error("用余额支付订单酬金异常：{}", e.getMessage());
			throw AppPayProviderException.EXCEPTION_PAYMENT_BALANCE_PAY;
		}
	}

	/**
	 * 完成支付订单
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized int completePaymentOrder(PaymentOrder paymentOrder, JSONObject params) throws Exception {
		// 订单状态为支付成功才执行后续动作
		if (paymentOrder.getStatus().equalsIgnoreCase(TradeStatusEnum.PAYMENT_SUCCESS.name())) {
			boolean flag = false;
			int result = 0;
			Long userid = params.getLongValue("userid");
			Long taskid = params.getLongValue("taskid");
			int paytype = paymentOrder.getPaytype().equalsIgnoreCase(PayTypeEnum.BALANCE.name()) ? 0 : -1;

			// 完成业务方法
			try {
				result = postPayTaskAction(taskid, userid, paymentOrder.getOuterno(), paytype, paymentOrder.getAmount().intValue());
				switch (result) {
					case 12000:
						return 12000;//ResultUtils.failure(12000, "create task failed");
					case 12005:
						return 12005;//ResultUtils.failure(12005, "update task failed");
					case 12006:
						return 12006;//ResultUtils.failure(12006, "task not exist");
					case 14002:
						return 14002;//ResultUtils.failure(14002, "record publisher bill failed");
					case 14013:
						return 14013;//ResultUtils.failure(14013, "record publisher trade failed");
				}
				// sleepMillisSecond(100);
			} catch (Exception e) {
				logger.error("完成支付订单异常：{}", e.getMessage());
				throw AppListenerQueueException.EXCEPTION_LISTENER_QUEUE_COMPLETE_PAYMENT_ORDER;
			}
			return 0;
		}
		return -1;
	}

	// 支付订单酬金通用代码
	@Transactional(rollbackFor = Exception.class)
	public synchronized int postPayTaskAction(Long taskid, Long userid, String outerno, int paytype, int money) throws Exception {
		boolean flag = false;
		try {
			UserTask task = null;
			// 幂等判断：检查订单是否已存在
			// sleepMillisSecond(100);
			task = userTaskService.queryTask(taskid);
			if (null == task) {
				logger.error("订单不存在：{}", taskid);
				return 12006;
			} else {
				if (1 == task.getStatus() && 1 == task.getPaystatus()) {// 如果订单状态是已支付且待接单，则不做操作
					flag = true;
				} else {// 如果订单状态不是已支付且待接单，则更新
					flag = userTaskService.updateTaskStatus(taskid);
				}
			}
			if (!flag) {
				logger.error("更新订单失败：{}", taskid);
				return 12005;
			}

			// 如果支付方式为余额，则更新余额账户
			// 幂等判断：当前支付订单的余额是否已扣除
			Integer balance_result = (Integer) accountService.getSavedObject(GlobalConstant.RIDES_ACCOUNT_BALANCE_SAVED_PREFIX + taskid + "-" + userid);
			if (null == balance_result || 0 == balance_result) {// 余额之前未扣除或扣除失败
				accountService.updateBalance(userid, -money);
				// 保存支付订单的余额扣除状态
				accountService.setSavedObject(GlobalConstant.RIDES_ACCOUNT_BALANCE_SAVED_PREFIX + taskid + "-" + userid, "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
			}

			// 在缓存中查询
			Integer bill_result = (Integer) accountService.getSavedObject(GlobalConstant.RIDES_ACCOUNT_BILL_SAVED_PREFIX + taskid + "-" + userid);
			if (null == bill_result || 0 == bill_result) {
				// 在数据库中查询
				UserBill userBill = accountService.queryBill(taskid, userid, 2, 0L, 0, paytype, money);
				// 幂等判断：检查发单人账单是否已存在
				if (null == userBill) {
					// 创建发单人账单
					String subject = new String("支付订单".getBytes(StandardCharsets.UTF_8));
					boolean result = accountService.createBill(taskid, userid, outerno, 2, 0L, 0, paytype, money, subject);
					if (!result) {
						logger.error("创建用户账单失败，用户编码：{}，任务编码：{}", userid, taskid);
						return 14002;
					}
					// 保存账单的已存在状态
					accountService.setSavedObject(GlobalConstant.RIDES_ACCOUNT_BILL_SAVED_PREFIX + taskid + "-" + userid, "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
				}
			}

			// 在缓存中查询
			Integer trade_result = (Integer) userTradeService.getSavedObject(GlobalConstant.RIDES_TRADE_SAVED_PREFIX + taskid + "-" + userid);
			if (null == trade_result || 0 == trade_result) {// 交易不存在
				// 在数据库中查询
				UserTrade userTrade = userTradeService.queryTrade(taskid, userid, null, 0, 0);
				// 幂等判断：检查发单人交易记录是否已存在（非违约交易）
				if (null == userTrade) {// 交易不存在
					// 创建发单人交易记录（默认对方为用户）
					boolean result = userTradeService.insertTrade(taskid, userid, 0, paytype, money);
					if (!result) {
						logger.error("创建用户交易记录失败，用户编码：{}，任务编码：{}", userid, taskid);
						return 14013;
					}
					// 保存交易的已存在状态
					userTradeService.setSavedObject(GlobalConstant.RIDES_TRADE_SAVED_PREFIX + taskid + "-" + userid, "1", GlobalConstant.EXPIRE_MAX_TIMEOUT);
				}
			}
			return 0;
		} catch (Exception e) {
			logger.error("支付订单酬金异常：{}", e.getMessage());
			throw AppListenerQueueException.EXCEPTION_LISTENER_QUEUE_COMPLETE_PAYMENT_ORDER;
		}
	}

	// 查找支付订单记录
	public PaymentOrder findPaymentOrderByOuterNo(final String outerno) {
		StringBuffer sb = new StringBuffer("SELECT guid, userid, taskid, transno, outerno, title, paytype, payclass, paychannel, ");
		sb.append("amount, period, reason, status, createtime, updatetime, remark FROM sys_payment_order WHERE outerno = '").append(outerno).append("' LIMIT 1");
		try {
			Object object = mySQLDao.findOne(sb.toString(), new PaymentOrder());
			if (null == object) {
				return null;
			}
			return (PaymentOrder) object;
		} catch (Exception e) {
			logger.error("查找支付订单记录异常，商户订单号：{}", outerno);
			throw AppPayProviderException.EXCEPTION_PAYMENT_ORDER_QUERY;
		}
	}

	// 创建支付订单信息
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean insertUserPaymentOrder(PaymentOrder paymentOrder) throws Exception {
		StringBuffer sb = new StringBuffer("INSERT INTO sys_payment_order (guid, userid, taskid, transno, outerno, ");
		sb.append("title, paytype, payclass, paychannel, amount, period, reason, status, remark) VALUES (");
		sb.append(paymentOrder.getGuid()).append(", ");
		sb.append(paymentOrder.getUserid()).append(", ");
		sb.append(paymentOrder.getTaskid()).append(", '");
		sb.append(paymentOrder.getTransno()).append("', '");
		sb.append(paymentOrder.getOuterno()).append("', '");
		sb.append(paymentOrder.getTitle()).append("', '");
		sb.append(paymentOrder.getPaytype()).append("', '");
		sb.append(paymentOrder.getPayclass()).append("', '");
		sb.append(paymentOrder.getPaychannel()).append("', ");
		sb.append(paymentOrder.getAmount()).append(", ");
		sb.append(paymentOrder.getPeriod()).append(", '");
		sb.append(paymentOrder.getReason()).append("', '");
		sb.append(paymentOrder.getStatus()).append("', '");
		sb.append(paymentOrder.getRemark()).append("')");

		try {
			long flag = mySQLDao.create(sb.toString());
			return 0 == flag;
		} catch (Exception e) {
			logger.error("创建支付订单信息异常，商户订单号：{}", paymentOrder.getOuterno());
			throw AppPayProviderException.EXCEPTION_PAYMENT_ORDER_CREATE;
		}
	}

	// 封装支付订单PaymentOrder
	private PaymentOrder sealPaymentOrder(long guid, long userid, long taskid, String transno, String outerno, String title, String paytype, String payclass,
										  String paychannel, double amount, int period, String reason, String status, String remark) {
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setGuid(guid);
		paymentOrder.setUserid(userid);
		paymentOrder.setTaskid(taskid);
		paymentOrder.setTransno(transno);
		paymentOrder.setOuterno(outerno);
		paymentOrder.setTitle(title);
		paymentOrder.setPaytype(paytype);
		paymentOrder.setPayclass(payclass);
		paymentOrder.setPaychannel(paychannel);
		paymentOrder.setAmount(amount);
		paymentOrder.setPeriod(period);
		paymentOrder.setReason(reason);
		paymentOrder.setStatus(status);
		paymentOrder.setRemark(remark);
		return paymentOrder;
	}
}
