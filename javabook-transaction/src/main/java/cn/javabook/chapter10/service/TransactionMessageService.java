package cn.javabook.chapter10.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.javabook.chapter10.exception.TransactionMessageProviderException;
import cn.javabook.cloud.core.enums.MessageDeadEnum;
import cn.javabook.cloud.core.enums.MessageStatusEnum;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.utils.DateUtil;
import cn.javabook.chapter10.dao.MongoDao;
import cn.javabook.chapter10.entity.TransactionMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mongodb.MongoCommandException;

/**
 * 事务消息Service实现类
 * 
 */
@Service
public class TransactionMessageService extends BaseService {
	private static final long serialVersionUID = -159687284455183020L;

	@Resource
	private MongoDao mongoDao;

	@Resource
	private JmsMessagingTemplate jmsMessagingTemplate;

	/**
	 * 查询事务消息
	 *
	 * @param guid
	 * @param entityClass
	 * @return
	 */
	public Object getMessageById(Long guid, Class<?> entityClass) {
		return mongoDao.find(new Query(Criteria.where("guid").is(guid)), entityClass);
	}

	/**
	 * 查询事务消息列表
	 *
	 * @param status
	 * @param isdead
	 * @param timebefore
	 * @param entityClass
	 * @return
	 */
	public List<?> getMessageList(String status, String isdead, String timebefore, Class<?> entityClass) {
		Criteria criteria = null;
		try {
			if (StringUtils.isNotBlank(isdead)) {
				criteria = Criteria.where("isdead").is(isdead).and("status").is(status).and("createtime").lte(timebefore);
			} else {
				criteria = Criteria.where("status").is(status).and("createtime").lte(timebefore);
			}
			return mongoDao.findAll(new Query(criteria), entityClass);
		} catch (Exception e) {
			logger.error("查询事务消息列表异常，消息状态：{}，是否死亡：{}，多久前消息：{}", status, isdead, timebefore);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}

	/**
	 * 查询事务消息列表
	 *
	 * @param status
	 * @param isdead
	 * @param entityClass
	 * @return
	 */
	public List<?> getMessageList(String status, String isdead, Class<?> entityClass) {
		Criteria criteria = null;
		try {
			if (StringUtils.isNotBlank(isdead)) {
				criteria = Criteria.where("isdead").is(isdead).and("status").is(status);
			} else {
				criteria = Criteria.where("status").is(status);
			}
			return mongoDao.findAll(new Query(criteria), entityClass);
		} catch (Exception e) {
			logger.error("查询事务消息列表异常，消息状态：{}，是否死亡：{}", status, isdead);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}

	/**
	 * 保存事务消息
	 *
	 * @param message
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean saveMessage(TransactionMessage message) {
		return upsertMessage(message, TransactionMessage.class);
	}

	/**
	 * 更新事务消息
	 *
	 * @param message
	 * @param entityClass
	 * @return
	 */
	public synchronized boolean upsertMessage(TransactionMessage message, Class<?> entityClass) {
		Query query = null;
		Update update = null;

		try {
			query = new Query(Criteria.where("guid").is(message.getGuid()));
			update = new Update();
			update.set("type", message.getType());
			update.set("flag", message.getFlag());
			update.set("queue", message.getQueue());
			update.set("content", message.getContent());
			update.set("status", message.getStatus());
			update.set("createtime", message.getCreatetime());
			update.set("sendtime", message.getSendtime());
			update.set("repeats", message.getRepeats());
			update.set("isdead", message.getIsdead());
			update.set("params", message.getParams());
			return mongoDao.upsert(query, update, entityClass);
		} catch (MongoCommandException | IllegalArgumentException e) {
			logger.error("更新事务消息异常，消息内容：{}", message.toString());
			throw BaseException.EXCEPTION_PUBLIC_DB_UPDATE_FAILED;
		}
	}

	/**
	 * 更新事务消息
	 *
	 * @param guid
	 * @param status
	 * @param createtime
	 * @param sendtime
	 * @param repeats
	 * @param isdead
	 * @param entityClass
	 * @return
	 */
	public synchronized boolean upsertMessage(Long guid, String status, String createtime, Date sendtime, int repeats, String isdead, Class<?> entityClass) {
		Query query = null;
		Update update = null;
		try {
			query = new Query(Criteria.where("guid").is(guid));
			update = new Update();
			update.set("status", status);
			update.set("createtime", createtime);
			update.set("sendtime", sendtime);
			update.set("repeats", repeats);
			update.set("isdead", isdead);
			return mongoDao.upsert(query, update, entityClass);
		} catch (MongoCommandException | IllegalArgumentException e) {
			logger.error("更新事务消息异常，消息编码：{}", guid);
			throw BaseException.EXCEPTION_PUBLIC_DB_UPDATE_FAILED;
		}
	}

	/**
	 * 更新事务消息
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean updateMessage(TransactionMessage message) throws Exception {
		return upsertMessage(message.getGuid(), message.getStatus(), message.getCreatetime(), message.getSendtime(), message.getRepeats(), message.getIsdead(), TransactionMessage.class);
	}

	/**
	 * 删除事务消息
	 *
	 * @param guid
	 * @param collectionName
	 */
	public synchronized void removeMessageById(Long guid, String collectionName) {
		try {
			mongoDao.remove(new Query(Criteria.where("guid").is(guid)), collectionName);
		} catch (MongoCommandException | IllegalArgumentException e) {
			logger.error("删除事务消息异常，消息编码：{}", guid);
			throw BaseException.EXCEPTION_PUBLIC_DB_DELETE_FAILED;
		}
	}

	/**
	 * 存储并发送事务消息
	 *
	 * @param message
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized void saveAndSendMessage(TransactionMessage message) throws Exception {
		boolean flag = false;
		if (null == message) {
			throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_ISNULL;
		}
		if (StringUtils.isBlank(message.getQueue())) {
			throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_QUEUE_ISNULL;
		}

		// 发送中
		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setIsdead(MessageDeadEnum.ACTIVE.name());
		message.setCreatetime(DateUtil.parse(new Date()));
		message.setSendtime(new Date());
		// 重发次数
		message.setRepeats(0);
		try {
			// 保存事务消息
			flag = saveMessage(message);
			if (!flag) {
				return;
			}
			// 发送事务消息到MQ
			sendMessage(message);
		} catch (Exception e) {
			logger.error("存储并发送事务消息异常，消息内容：{}", message.toString());
			throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_SAVESEND;
		}
	}

	/**
	 * 重发事务消息
	 *
	 * @param message
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized void reSendMessage(TransactionMessage message) throws Exception {
		boolean flag = false;

		try {
			if (null == message) {
				throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_ISNULL;
			}
			if (StringUtils.isBlank(message.getQueue())) {
				throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_QUEUE_ISNULL;
			}

			message.addRepeats();
			message.setCreatetime(DateUtil.parse(new Date()));
			// 更新事务消息状态
			flag = updateMessage(message);
			if (!flag) {
				return;
			}
			// 发送事务消息到MQ
			sendMessage(message);
		} catch (Exception e) {
			assert message != null;
			logger.error("重发事务消息异常，消息内容：{}", message.toString());
			throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_RESEND;
		}
	}

	/**
	 * 将事务消息标记为已死亡
	 *
	 * @param guid
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized void setMessageToAreadlyDead(Long guid) throws Exception {
		TransactionMessage message = null;
		try {
			message = (TransactionMessage) getMessageById(guid, TransactionMessage.class);
			if (null == message) {
				throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_ISNULL;
			}
			if (StringUtils.isBlank(message.getQueue())) {
				throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_QUEUE_ISNULL;
			}

			message.setIsdead(MessageDeadEnum.DEAD.name());
			message.setCreatetime(DateUtil.parse(new Date()));
			updateMessage(message);
		} catch (Exception e) {
			logger.error("将事务消息标记为已死亡异常，消息编码：{}", guid);
			throw TransactionMessageProviderException.EXCEPTION_PUBLIC_MESSAGE_SETDEAD;
		}
	}

	/**
	 * 发送消息
	 *
	 * @param message
	 */
	public void sendMessage(TransactionMessage message) {
		jmsMessagingTemplate.convertAndSend(message.getQueue(), message);
	}
}
