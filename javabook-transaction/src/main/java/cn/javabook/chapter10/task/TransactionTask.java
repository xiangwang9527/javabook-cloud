package cn.javabook.chapter10.task;

import cn.javabook.chapter10.entity.PaymentOrder;
import cn.javabook.chapter10.entity.TransactionMessage;
import cn.javabook.chapter10.executor.TransactionExecutor;
import com.alibaba.fastjson.JSONObject;
import cn.javabook.cloud.core.parent.BaseListener;

/**
 * 事务进程任务
 * 
 */
public class TransactionTask extends BaseListener implements Runnable {
	private static final long serialVersionUID = 822422428577130401L;

	private TransactionMessage message;
	private PaymentOrder paymentOrder;
	private JSONObject params;
	private TransactionExecutor transactionExecutor;

	public TransactionTask(TransactionMessage message, PaymentOrder paymentOrder, JSONObject params) {
		this.message = message;
		this.paymentOrder = paymentOrder;
		this.params = params;
	}

	public TransactionMessage getMessage() {
		return message;
	}

	public void setMessage(TransactionMessage message) {
		this.message = message;
	}

	public PaymentOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(PaymentOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public TransactionExecutor getTransactionExecutor() {
		return transactionExecutor;
	}

	public void setTransactionExecutor(TransactionExecutor transactionExecutor) {
		this.transactionExecutor = transactionExecutor;
	}

	@Override
	public void run() {
		transactionExecutor.completeTransaction(message, paymentOrder, params);
	}
}
