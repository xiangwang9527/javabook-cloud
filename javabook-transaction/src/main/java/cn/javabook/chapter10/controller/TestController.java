package cn.javabook.chapter10.controller;

import cn.javabook.chapter10.service.PaymentOrderService;
import cn.javabook.chapter10.service.UserTaskService;
import cn.javabook.chapter10.entity.TransactionMessage;
import cn.javabook.chapter10.service.TransactionMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试Controller
 *
 * 数据库脚本：
 * -- 未付款任务数
 * SELECT COUNT(guid) AS 未付款任务数 FROM sy_user_task WHERE paystatus = 0;
 * -- 已付款任务数
 * SELECT COUNT(guid) AS 已付款任务数 FROM sy_user_task WHERE paystatus = 1;
 * -- 已支付数
 * SELECT COUNT(guid) AS 已支付数 FROM sy_payment_order;
 * -- 账单数
 * SELECT COUNT(guid) AS 账单数 FROM sy_user_bill;
 * -- 交易数
 * SELECT COUNT(guid) AS 交易数 FROM sy_user_trade;
 * -- 会计分录数
 * SELECT COUNT(guid) AS 会计分录数 FROM sy_accounting_voucher;
 *
 * 预期结果：
 * has_pay_task_num = payment_num = bill_num = trade_num = voucher_num
 *
 * 实际结果：
 * has_pay_task_num = payment_num = bill_num = trade_num = voucher_num
 *
 */
@RestController
public class TestController extends Thread {
	@Resource
	private UserTaskService userTaskService;

	@Resource
	private PaymentOrderService paymentOrderService;

	@Resource
	private TransactionMessageService transactionMessageService;

	/**
	 * 单条支付测试
	 *
	 */
	@GetMapping("/balancePayOne")
	public void balancePayOne() {
		try {
			// 下单
			long taskid = userTaskService.createTask(1L, "XXXX省", "YYYY市", "100", "ZZZZ区", "123456", "", 1, "余额支付测试", 600, 1, 0, 0, 0);
			// 封装事务消息
			TransactionMessage message = paymentOrderService.balancePay(taskid, 1L, 0, 1, "余额支付测试");
			// TODO：省略中间若干步骤
			// 发送消息
			transactionMessageService.saveAndSendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付压力测试
	 * 
	 */
	@GetMapping("/balancePay")
	public void balancePay() {
		Runnable runnable = new Runnable() {
			@Override
			public synchronized void run() {
				// 开10个线程，模拟10个用户
				int count = 10;
				ExecutorService executorService = Executors.newFixedThreadPool(count);
				for (long i = 1; i < 11; i++) {
					executorService.execute(new TestBalancePayTask(userTaskService, paymentOrderService, transactionMessageService, i));
				}
			}
		};
		runnable.run();
	}

	public static class TestBalancePayTask implements Runnable {
		private final UserTaskService userTaskService;
		private final PaymentOrderService paymentOrderService;
		private final TransactionMessageService transactionMessageService;
		private final long userid;
		public TestBalancePayTask(UserTaskService userTaskService, PaymentOrderService paymentOrderService, TransactionMessageService transactionMessageService, long userid) {
			this.userTaskService = userTaskService;
			this.paymentOrderService = paymentOrderService;
			this.transactionMessageService = transactionMessageService;
			this.userid = userid;
		}

		@Override
		public void run() {
			// 每个用户支付n次
			for (int j = 0; j < 10000; j++) {
				try {
					// 下单
					long taskid = userTaskService.createTask(userid, "XXXX省", "YYYY市", "100", "ZZZZ区", "123456", "", 1, "余额支付压力测试任务", 600, 1, 0, 0, 0);
					// 封装事务消息
					TransactionMessage message = paymentOrderService.balancePay(taskid, userid, 0, 1, "余额支付压力测试");
					// TODO：省略中间若干步骤
					// 发送消息
					transactionMessageService.saveAndSendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
