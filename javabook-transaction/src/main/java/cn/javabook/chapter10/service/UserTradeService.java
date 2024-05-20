package cn.javabook.chapter10.service;

import java.util.Date;

import cn.javabook.chapter10.exception.TradeProviderException;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.utils.DateUtil;
import cn.javabook.chapter10.dao.RedisDao;
import cn.javabook.chapter10.dao.MySQLDao;
import cn.javabook.chapter10.entity.UserTrade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * 用户交易Service实现
 * 
 */
@Service
public class UserTradeService extends BaseService {
	private static final long serialVersionUID = 6424179340884497658L;

	@Resource
	private MySQLDao mySQLDao;

	@Resource
	private RedisDao redisDao;

	// redis：会计分录对象是否已保存
	public Object getSavedObject(String key) {
		return redisDao.get(key);
	}

	// redis：保存会计分录对象（Object，1天失效）
	public void setSavedObject(String key, String value, long expire) {
		if (0 == expire) {
			redisDao.set(key, value);
		} else {
			redisDao.set(key, value, expire);
		}
	}

	// 查询交易
	public UserTrade queryTrade(Long taskid, Long userid, Long objectid, int tradetype, int status) {
		StringBuilder sb = new StringBuilder("SELECT guid, taskid, userid, tradeobject, objectid, tradetype, paytype, money, status, remark, publishtime, accepttime, canceltime, tradetime ");
		sb.append("FROM sys_user_trade WHERE taskid = ").append(taskid);
		sb.append(" AND userid = ").append(userid);
		if (null != objectid) {
			sb.append(" AND objectid = ").append(objectid);
		}
		sb.append(" AND tradetype = ").append(tradetype);
		sb.append(" AND status = ").append(status).append(" LIMIT 1");
		try {
			return (UserTrade) mySQLDao.findOne(sb.toString(), new UserTrade());
		} catch (Exception e) {
			logger.error("查询交易异常，订单编码：{}，用户编码：{}，交易对象编码：{}，交易类别：{}", taskid, userid, objectid, tradetype);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}

	// 查询交易
	public UserTrade queryTrade(Long userid, String outerno) {
		StringBuilder sb = new StringBuilder("SELECT guid, taskid, userid, tradeobject, objectid, tradetype, paytype, money, status, remark, publishtime, accepttime, canceltime, tradetime ");
		sb.append("FROM sys_user_trade WHERE userid = ").append(userid).append(" AND remark = '").append(outerno).append("'").append(" LIMIT 1");
		try {
			return (UserTrade) mySQLDao.findOne(sb.toString(), new UserTrade());
		} catch (Exception e) {
			logger.error("查询交易异常，用户编码：{}，商户订单号：{}", userid, outerno);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}

	// 创建发单交易（不包括交易完成时间）
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean insertTrade(Long taskid, Long userid, int tradetype, int paytype, int money) throws Exception {
		Long guid = nextId(GlobalConstant.GUID_APP_TRADE_DATANODE, 1L);
		StringBuilder sb = new StringBuilder("INSERT INTO sys_user_trade (guid, taskid, userid, tradetype, paytype, money) VALUES (");
		sb.append(guid).append(", ");
		sb.append(taskid).append(", ");
		sb.append(userid).append(", ");
		sb.append(tradetype).append(", ");
		sb.append(paytype).append(", ");
		sb.append(money).append(")");
		try {
			long flag = mySQLDao.create(sb.toString());
			return 0 == flag;
		} catch (Exception e) {
			logger.error("创建发单交易异常，交易编码：{}，订单编码：{}，用户编码：{}，交易类别：{}，支付类别：{}", guid, taskid, userid, tradetype, paytype);
			throw TradeProviderException.EXCEPTION_TRADE_CREATE_PUBLISH;
		}
	}

	// 更新交易（接发单的正常交易）
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean updateTrade(Long taskid, Long userid, int tradeobject, Long objectid, int tradetype) throws Exception {
		StringBuilder sb = new StringBuilder("UPDATE sys_user_trade SET tradeobject = ");
		sb.append(tradeobject).append(", objectid = ");
		sb.append(objectid).append(", status = 1, ");
		// 交易完成
		sb.append("tradetime = '").append(DateUtil.parse(new Date())).append("'");
		sb.append(" WHERE taskid = ").append(taskid);
		sb.append(" AND userid = ").append(userid);
		sb.append(" AND tradetype = ").append(tradetype);
		sb.append(" AND status = 0 LIMIT 1");
		try {
			return mySQLDao.update(sb.toString());
		} catch (Exception e) {
			logger.error("更新交易异常，订单编码：{}，用户编码：{}，交易对象编码：{}，交易类别：{}", taskid, userid, objectid, tradetype);
			throw TradeProviderException.EXCEPTION_TRADE_UPDATE;
		}
	}
}
