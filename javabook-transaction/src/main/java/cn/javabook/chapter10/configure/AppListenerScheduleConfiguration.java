package cn.javabook.chapter10.configure;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import cn.javabook.cloud.core.constant.GlobalConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 配置周期性计划任务线程池
 * 
 */
@Configuration
@EnableScheduling
public class AppListenerScheduleConfiguration implements SchedulingConfigurer {
	/**
	 * 添加到定时器
	 *
	 * @param taskRegistrar
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	/**
	 * 定时任务线程池
	 *
	 * @return
	 */
	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(GlobalConstant.THREADPOOL_CORE_POOL_SIZE);
	}
}
