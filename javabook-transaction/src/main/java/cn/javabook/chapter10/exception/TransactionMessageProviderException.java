package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 事务消息异常类
 * 
 */
public class TransactionMessageProviderException extends BaseException {
	private static final long serialVersionUID = 169570812800065045L;

	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_ISNULL = new TransactionMessageProviderException(10200, "save message is null"); // 保存的事务消息为空
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_QUEUE_ISNULL = new TransactionMessageProviderException(10201, "message queue is null"); // 事务消息的消费队列为空
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_SEND = new TransactionMessageProviderException(10201, "send message exception"); // 发送事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_WAITINGCONFIRM = new TransactionMessageProviderException(10203, "save waiting confirm message exception"); // 保存待确认事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_CONFIRMSEND = new TransactionMessageProviderException(10204, "confirm and send message exception"); // 确认并发送事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_SAVESEND = new TransactionMessageProviderException(10205, "save and send message exception"); // 存储并发送事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_RESEND = new TransactionMessageProviderException(10206, "resend message exception"); // 重发事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_RESENDBYID = new TransactionMessageProviderException(10207, "resend message by id exception"); // 重发事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_SETDEAD = new TransactionMessageProviderException(10208, "set message to areadly dead exception"); // 将事务消息标记为已死亡异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_UPDATEBYID = new TransactionMessageProviderException(10209, "update message by id exception"); // 更新事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_BATCH_UPDATE = new TransactionMessageProviderException(10210, "batch update message exception"); // 批量更新事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_DELETEBYID = new TransactionMessageProviderException(10211, "delete message by id exception"); // 删除事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_BATCH_DELETE = new TransactionMessageProviderException(10212, "batch delete message exception"); // 批量删除事务消息异常
	public static final TransactionMessageProviderException EXCEPTION_PUBLIC_MESSAGE_RESENDALL = new TransactionMessageProviderException(10213, "resend all dead message by queue name exception"); // 重发队列中全部已死亡的消息异常

	public TransactionMessageProviderException() {
		super();
	}

	public TransactionMessageProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public TransactionMessageProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
