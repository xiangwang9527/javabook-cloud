package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 支付网关异常类
 * 
 */
public class AppPayProviderException extends BaseException {
	private static final long serialVersionUID = 3530470331181524782L;

	public static final AppPayProviderException EXCEPTION_ANTSHIELD_QUERY = new AppPayProviderException(15000, "query ant shield risk exception"); // 查询蚁盾风险评估异常

	public static final AppPayProviderException EXCEPTION_PAYMENT_ORDER_QUERY = new AppPayProviderException(15010, "query payment order exception"); // 查询支付订单异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_ORDER_CREATE = new AppPayProviderException(15011, "create payment order exception"); // 创建支付订单异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_ORDER_UPDATE = new AppPayProviderException(15012, "update payment order exception"); // 更新支付订单异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_ORDER_DELETE = new AppPayProviderException(15013, "delete payment order exception"); // 删除支付订单异常

	public static final AppPayProviderException EXCEPTION_WITHDRAW_ORDER_QUERY = new AppPayProviderException(15020, "query withdraw order exception"); // 查询提现订单异常
	public static final AppPayProviderException EXCEPTION_WITHDRAW_ORDER_CREATE = new AppPayProviderException(15021, "create withdraw order exception"); // 创建提现订单异常
	public static final AppPayProviderException EXCEPTION_WITHDRAW_ORDER_UPDATE = new AppPayProviderException(15022, "update withdraw order exception"); // 更新提现订单异常
	public static final AppPayProviderException EXCEPTION_WITHDRAW_ORDER_DELETE = new AppPayProviderException(15023, "delete withdraw order exception"); // 删除提现订单异常

	public static final AppPayProviderException EXCEPTION_PAYMENT_BALANCE_PAY = new AppPayProviderException(15030, "balance pay exception"); // 余额支付订单酬金异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_BALANCE_TIPS = new AppPayProviderException(15031, "payment pay exception"); // 余额支付补充酬金异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_BALANCE_PENALTY = new AppPayProviderException(15032, "balance penalty exception"); // 余额支付订单违约金异常

	public static final AppPayProviderException EXCEPTION_PAYMENT_QUERY_ALIPAY = new AppPayProviderException(15080, "query alipay payment order exception"); // 查询支付宝订单异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_ALIPAY = new AppPayProviderException(15081, "payment alipay exception"); // 支付宝支付异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_SAVE_ALIPAY_SUCCESS = new AppPayProviderException(15082, "save alipay successfully payment order exception"); // 保存支付宝订单成功异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_SAVE_ALIPAY_FAILED = new AppPayProviderException(15083, "save alipay failed payment order exception"); // 保存支付宝订单失败异常

	public static final AppPayProviderException EXCEPTION_PAYMENT_QUERY_WEXIN = new AppPayProviderException(15090, "query weixin payment order exception"); // 查询微信订单异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_WEIXIN = new AppPayProviderException(15091, "payment weixin exception"); // 微信支付异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_SAVE_WEIXIN_SUCCESS = new AppPayProviderException(15092, "save wxpay successfully payment order exception"); // 保存微信订单成功异常
	public static final AppPayProviderException EXCEPTION_PAYMENT_SAVE_WXPAY_FAILED = new AppPayProviderException(15093, "save wxpay failed payment order exception"); // 保存微信订单失败异常

	public AppPayProviderException() {
		super();
	}

	public AppPayProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public AppPayProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
