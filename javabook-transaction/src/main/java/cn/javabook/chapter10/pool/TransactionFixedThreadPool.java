package cn.javabook.chapter10.pool;

import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.chapter10.task.TransactionTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动完成支付订单消息的线程池
 * 
 */
public class TransactionFixedThreadPool {
	private static final ExecutorService threadPool = new ThreadPoolExecutor(
			GlobalConstant.THREADPOOL_CORE_POOL_SIZE,
			GlobalConstant.THREADPOOL_MAX_POOL_SIZE,
			GlobalConstant.THREADPOOL_KEEP_ALIVE_TIME,
			TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(GlobalConstant.THREADPOOL_QUEUE_CAPACITY),
			new ThreadPoolExecutor.CallerRunsPolicy()
	);

	public static void addTask(TransactionTask transactionTask) {
		threadPool.execute(transactionTask);
	}
}
