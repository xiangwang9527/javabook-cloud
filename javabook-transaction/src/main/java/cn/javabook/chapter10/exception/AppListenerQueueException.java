package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 消息监听异常类
 * 
 */
public class AppListenerQueueException extends BaseException {
	private static final long serialVersionUID = 631729762859751188L;

	public static final AppListenerQueueException EXCEPTION_LISTENER_QUEUE_COMPLETE_SERVICE = new AppListenerQueueException(15302, "execute service exception"); // 执行业务方法异常
	public static final AppListenerQueueException EXCEPTION_LISTENER_QUEUE_COMPLETE_PAYMENT_ORDER = new AppListenerQueueException(15304, "complete payment order exception"); // 完成支付订单信息异常

	public AppListenerQueueException() {
		super();
	}

	public AppListenerQueueException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public AppListenerQueueException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
