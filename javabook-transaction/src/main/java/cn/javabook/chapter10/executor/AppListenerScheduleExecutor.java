package cn.javabook.chapter10.executor;

import cn.javabook.chapter10.service.AppListenerScheduleService;
import cn.javabook.cloud.core.BaseObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * 计划任务执行实现类
 * 
 */
@Component
public class AppListenerScheduleExecutor extends BaseObject {
	private static final long serialVersionUID = 1070629478791251442L;

	@Resource
	private AppListenerScheduleService appListenerScheduleService;

	private static int sending_active_counter = 0;
	private static int sending_dead_counter = 0;

	/**
	 * 启动延迟1秒，每60秒执行1次
	 *
	 * @throws Exception
	 */
	@Scheduled(initialDelay=1000, fixedDelay=60000)
	public void executeOneMinute() throws Exception {
		boolean done = false;

		// 处理状态为“发送中”但超时没有被成功消费确认的消息
		done = appListenerScheduleService.handleSendingAndActiveMessage();
		if (done) {
			logger.info("处理[ACTIVE]消息结束，已处理 {} 次", ++sending_active_counter);
		}

		// 处理状态为“发送中”但已标记为死亡的消息
		done = appListenerScheduleService.handleSendingAndDeadMessage();
		if (done) {
			logger.info("处理[DEAD]消息结束，已处理 {} 次", ++sending_dead_counter);
		}
	}
}
