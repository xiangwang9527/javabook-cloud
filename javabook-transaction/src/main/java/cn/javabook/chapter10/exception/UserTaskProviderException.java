package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 订单异常类
 * 
 */
public class UserTaskProviderException extends BaseException {
	private static final long serialVersionUID = 7935972434396041704L;

	public static final UserTaskProviderException EXCEPTION_TASK_CREATE_TEMPLATE = new UserTaskProviderException(12000, "create template task exception"); // 创建临时订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_CREATE = new UserTaskProviderException(12001, "create task exception"); // 创建订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_UPDATE = new UserTaskProviderException(12002, "update task status exception"); // 更新订单状态异常
	public static final UserTaskProviderException EXCEPTION_TASK_UPDATE_ONLY = new UserTaskProviderException(12003, "update only task status exception"); // 仅更新订单状态异常
	public static final UserTaskProviderException EXCEPTION_TASK_UPDATE_NO_PUBLISH = new UserTaskProviderException(12004, "update only task no publish exception"); // 仅更新订单不发布异常
	public static final UserTaskProviderException EXCEPTION_TASK_ACCEPT = new UserTaskProviderException(12005, "accept task exception"); // 接单后更新订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_CANCEL = new UserTaskProviderException(12006, "cancel task exception"); // 取消订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_CANCEL_FORCE = new UserTaskProviderException(12007, "force cancel task exception"); // 强制取消订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_DELETE = new UserTaskProviderException(12008, "delete task exception"); // 删除订单异常
	public static final UserTaskProviderException EXCEPTION_TASK_CONFIRM = new UserTaskProviderException(12009, "confirm task exception"); // 确认订单完成异常
	public static final UserTaskProviderException EXCEPTION_TASK_COMPLETE = new UserTaskProviderException(12010, "confirm task audit exception"); // 确认订单仲裁完成异常
	public static final UserTaskProviderException EXCEPTION_TASK_QUERY = new UserTaskProviderException(12011, "query task exception"); // 查询订单异常

	public UserTaskProviderException() {
		super();
	}

	public UserTaskProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public UserTaskProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
