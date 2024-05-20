package cn.javabook.chapter10.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.javabook.chapter10.exception.AppListenerScheduleException;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.enums.MessageDeadEnum;
import cn.javabook.cloud.core.enums.MessageStatusEnum;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.utils.DateUtil;
import cn.javabook.chapter10.entity.TransactionMessage;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import javax.annotation.Resource;

/**
 * 消息定时器接口实现
 * 
 */
@Service
public class AppListenerScheduleService extends BaseService {
	private static final long serialVersionUID = -8782414015511522102L;

	private static final Map<Integer, Integer> notifyParam = Maps.newConcurrentMap();

	@Resource
	private TransactionMessageService transactionMessageService;

	/**
	 * 初始化静态变量
	 */
	static {
		// 第一次间隔1分钟后发送
		notifyParam.put(1, GlobalConstant.MESSAGE_QUEUE_MESSAGE_SEND_1_TIME);
		// 第二次间隔1分钟后发送
		notifyParam.put(2, GlobalConstant.MESSAGE_QUEUE_MESSAGE_SEND_2_TIME);
		// 第三次间隔2分钟后发送
		notifyParam.put(3, GlobalConstant.MESSAGE_QUEUE_MESSAGE_SEND_3_TIME);
		// 第四次间隔5分钟后发送
		notifyParam.put(4, GlobalConstant.MESSAGE_QUEUE_MESSAGE_SEND_4_TIME);
		// 第五次间隔10分钟后发送
		notifyParam.put(5, GlobalConstant.MESSAGE_QUEUE_MESSAGE_SEND_5_TIME);
	}

	/**
	 * 从MongoDB中过滤出状态为「发送中」但超时没有被成功消费确认的消息
	 *
	 */
	public boolean handleSendingAndActiveMessage() throws Exception {
		Map<String, Object> paramMap = null;
		Map<String, TransactionMessage> messageMap = null;

		try {
			// 每页条数
			int numPerPage = GlobalConstant.PAGE_DEFAULT_SIZE_PER_PAGE;
			// 一次最多处理页数
			int maxHandlePageCount = GlobalConstant.PAGE_DEFAULT_SIZE_PER_PAGE;
			// 查询条件
			paramMap = Maps.newConcurrentMap();
			// 发送中的消息
			paramMap.put("status", MessageStatusEnum.SENDING.name());
			// 存活的消息
			paramMap.put("isdead", MessageDeadEnum.ACTIVE.name());
			// 8分钟前的SENDING消息
			paramMap.put("sendTimeBefore", DateUtil.before(GlobalConstant.MESSAGE_QUEUE_MESSAGE_HANDLE_DURATION));
			// 分页查询的排序方式，正向排序
			paramMap.put("sortby", "ASC");
			messageMap = getMessageMap(numPerPage, maxHandlePageCount, paramMap, true);
			if (null == messageMap) {
				return false;
			} else {
				if (0 >= messageMap.size()) {
					return false;
				}
			}
			return handleSendingAndActiveMessage(messageMap);
		} catch (Exception e) {
			logger.error("处理[ACTIVE]状态的消息异常：{}", e.getMessage());
			throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_ACTIVE;
		}
	}

	/**
	 * 从MongoDB中过滤出状态为「发送中」但已标记为死亡的消息
	 * 
	 */
	public boolean handleSendingAndDeadMessage() throws Exception {
		Map<String, Object> paramMap = null;
		Map<String, TransactionMessage> messageMap = null;

		try {
			// 每页条数
			int numPerPage = GlobalConstant.PAGE_DEFAULT_SIZE_PER_PAGE;
			// 一次最多处理页数
			int maxHandlePageCount = GlobalConstant.PAGE_DEFAULT_SIZE_PER_PAGE;
			// 查询条件
			paramMap = Maps.newConcurrentMap();
			// 发送中的消息
			paramMap.put("status", MessageStatusEnum.SENDING.name());
			// 死亡的消息
			paramMap.put("isdead", MessageDeadEnum.DEAD.name());
			// 8分钟前的SENDING消息
			paramMap.put("sendTimeBefore", DateUtil.before(GlobalConstant.MESSAGE_QUEUE_MESSAGE_HANDLE_DURATION));
			// 分页查询的排序方式，正向排序
			paramMap.put("sortby", "ASC");
			messageMap = getMessageMap(numPerPage, maxHandlePageCount, paramMap, true);
			if (null == messageMap) {
				return false;
			} else {
				if (0 >= messageMap.size()) {
					return false;
				}
			}
			return handleSendingAndDeadMessage(messageMap);
		} catch (Exception e) {
			logger.error("处理[DEAD]状态的消息异常：{}", e.getMessage());
			throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_DEAD;
		}
	}

	/**
	 * 处理[ACTIVE]状态的消息
	 *
	 */
	private boolean handleSendingAndActiveMessage(Map<String, TransactionMessage> messageMap) {
		logger.info("开始处理状态为[ACTIVE]的消息Map，共[" + messageMap.size() + "]条");
		TransactionMessage message = null;

		try {
			// 单条消息处理
			for (Map.Entry<String, TransactionMessage> entry : messageMap.entrySet()) {
				message = entry.getValue();
				try {
					// 判断发送次数
					logger.info("消息编码为[{}]的消息，已经重新发送了 {} 次", message.getGuid(), message.getRepeats() + 1);
					// 如果超过最大发送次数直接退出
					if (GlobalConstant.MESSAGE_QUEUE_MESSAGE_MAX_SEND_TIMES < message.getRepeats()) {
						// 标记为死亡
						transactionMessageService.setMessageToAreadlyDead(message.getGuid());
						continue;
					}
					// 判断是否达到发送消息的时间间隔条件
					int repeats = message.getRepeats();
					int times = notifyParam.get(repeats == 0 ? 1 : repeats);
					long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
					long needTime = currentTimeInMillis - times * 60 * 1000;
					long hasTime = message.getSendtime().getTime();
					// 判断是否达到了可以再次发送的时间条件
					if (hasTime > needTime) {
						logger.info("当前时间：{}，{}消息上次发送时间：{}，{}分钟后才可以再发送", DateUtil.parse(new Date()), message.getGuid(), DateUtil.parse(message.getSendtime()), times);
						continue;
					}
					// 重新发送消息
					transactionMessageService.reSendMessage(message);
				} catch (Exception e) {
					logger.error("处理单条[ACTIVE]消息异常，消息编码：{}", message.getGuid());
					throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_ACTIVE;
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("处理状态为[ACTIVE]的消息Map异常：{}", e.getMessage());
			throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_ACTIVE;
		}
	}

	/**
	 * 处理[DEAD]状态的消息
	 * 
	 */
	private boolean handleSendingAndDeadMessage(Map<String, TransactionMessage> messageMap) {
		logger.info("开始处理状态为[DEAD]的消息Map，共[" + messageMap.size() + "]条");
		TransactionMessage message = null;

		try {
			// 单条消息处理
			for (Map.Entry<String, TransactionMessage> entry : messageMap.entrySet()) {
				message = entry.getValue();
				try {
					transactionMessageService.removeMessageById(message.getGuid(), GlobalConstant.MONGO_TABLE_TRANSACTIONMESSAGE);
					logger.info("删除已死亡消息成功，消息编码：{}", message.getGuid());
				} catch (Exception e) {
					logger.error("处理单挑[DEAD]消息异常，消息编码：{}", message.getGuid());
					throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_DEAD;
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("处理状态为[DEAD]的消息Map异常：{}", e.getMessage());
			throw AppListenerScheduleException.EXCEPTION_SCHEDULE_SENDING_DEAD;
		}
	}

	/**
	 * 根据分页参数及查询条件批量获取消息数据
	 * 
	 * @param numPerPage 每页记录数
	 * @param maxHandlePageCount 最多获取页数
	 * @param paramMap 查询参数
	 */
	private Map<String, TransactionMessage> getMessageMap(int numPerPage, int maxHandlePageCount, Map<String, Object> paramMap, boolean hastime) {
		// 每次拿到的结果集
		List<TransactionMessage> list = null;
		// 转换成map
		Map<String, TransactionMessage> messageMap = Maps.newConcurrentMap();

		try {
			// 获取分页数据集
			String status = (String) paramMap.get("status");
			String isdead = (String) paramMap.get("isdead");
			if (hastime) {
				String timeBefore = (String) paramMap.get("sendTimeBefore");
				list = (List<TransactionMessage>) transactionMessageService.getMessageList(status, isdead, timeBefore, TransactionMessage.class);
			} else {
				list = (List<TransactionMessage>) transactionMessageService.getMessageList(status, isdead, TransactionMessage.class);
			}
			if (null == list) {
				return null;
			}
			for (TransactionMessage message : list) {
				messageMap.put(String.valueOf(message.getGuid()), message);
			}
			return messageMap;
		} catch (Exception e) {
			logger.error("批量查询消息数据Map异常：{}", e.getMessage());
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}
}
