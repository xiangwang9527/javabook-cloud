package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 交易异常类
 * 
 */
public class TradeProviderException extends BaseException {
	private static final long serialVersionUID = -5820906235683454542L;

	public static final TradeProviderException EXCEPTION_TRADE_CREATE_PUBLISH = new TradeProviderException(14000, "create publish trade record exception"); // 创建发单交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_CREATE_RECEIVE = new TradeProviderException(14001, "create receive trade record exception"); // 创建接单交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_UPDATE = new TradeProviderException(14002, "update trade record exception"); // 更新交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_UPDATE_ONCANCEL = new TradeProviderException(14003, "update trade record on cancel exception"); // 更新交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_UPDATE_RECEIVE = new TradeProviderException(14004, "update receive trade record exception"); // 更新接单交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_UPDATE_PENALTY = new TradeProviderException(14005, "update penalty trade record exception"); // 更新未完成违约交易记录异常
	public static final TradeProviderException EXCEPTION_TRADE_CREATE_SNAPSHOT = new TradeProviderException(14006, "create trade snapshot exception"); // 创建交易快照异常

	public TradeProviderException() {
		super();
	}

	public TradeProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public TradeProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
