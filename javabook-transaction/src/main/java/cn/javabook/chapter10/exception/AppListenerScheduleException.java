package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 计划任务异常类
 * 
 */
public class AppListenerScheduleException extends BaseException {
	private static final long serialVersionUID = 631729762859751188L;

	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_WAITING = new AppListenerScheduleException(15400, "execute waiting confirm message schedule exception"); // 处理[WAITING]状态的消息异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_SENDING_ACTIVE = new AppListenerScheduleException(15401, "execute sending and active message schedule exception"); // 处理[SENDING][ACTIVE]状态的消息异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_SENDING_DEAD = new AppListenerScheduleException(15402, "execute sending and dead message schedule exception"); // 处理[SENDING][DEAD]状态的消息异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_NOTIFY = new AppListenerScheduleException(15403, "execute notify schedule exception"); // 执行推送通知异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_TASK_AUDIT = new AppListenerScheduleException(15404, "execute task audit schedule exception"); // 执行订单仲裁任务异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_TASK_COMMENT = new AppListenerScheduleException(15405, "execute task comment schedule exception"); // 处理订单评价异常
	public static final AppListenerScheduleException EXCEPTION_SCHEDULE_SYSTEM_HELP = new AppListenerScheduleException(15406, "execute system help schedule exception"); // 执行系统求助异常

	public AppListenerScheduleException() {
		super();
	}

	public AppListenerScheduleException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public AppListenerScheduleException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
