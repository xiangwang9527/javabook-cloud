package cn.javabook.chapter10.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import cn.javabook.chapter10.executor.TransactionExecutor;
import com.alibaba.fastjson.TypeReference;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.enums.MessageTypeEnum;
import cn.javabook.cloud.core.parent.BaseListener;
import cn.javabook.chapter10.entity.PaymentOrder;
import cn.javabook.chapter10.entity.TransactionMessage;
import cn.javabook.chapter10.pool.TransactionFixedThreadPool;
import cn.javabook.chapter10.task.TransactionTask;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 监听创建支付订单的请求
 * 
 */
@Component
public class TransactionMessageListener extends BaseListener {
	private static final long serialVersionUID = -5149841366973800197L;

	@Resource
	private TransactionExecutor transactionExecutor;

	/**
	 * 监听创建订单的请求
	 *
	 * @param objectMessage
	 * @throws Exception
	 */
	@JmsListener(destination = GlobalConstant.MESSAGE_QUEUE_CONSUMER_TRANSACTION_QUEUE, containerFactory="queueListener")
	public void onPaymentMessage(ObjectMessage objectMessage) throws Exception {
		TransactionMessage message = null;
		PaymentOrder paymentOrder = null;
		JSONObject params = null;

		try {
			// 强转为事务消息对象
			message = (TransactionMessage) objectMessage.getObject();
			// 事务类型的消息
			if (message.getType().equalsIgnoreCase(MessageTypeEnum.TRANSACTION.name())) {
				paymentOrder = JSON.parseObject(message.getContent(), new TypeReference<PaymentOrder>() {});
				// 获得回调附加参数
				params = JSONObject.parseObject(message.getParams());
				// 丢到线程池中执行
				TransactionTask transactionTask = new TransactionTask(message, paymentOrder, params);
				transactionTask.setTransactionExecutor(transactionExecutor);
				TransactionFixedThreadPool.addTask(transactionTask);
				// 确认消息接收
				objectMessage.acknowledge();
				logger.info("收到事务消息，消息编码：{}", message.getGuid());
			}
		} catch (JMSException e) {
			logger.error("监听创建订单的请求异常：{}", e.getMessage());
		}
	}
}
