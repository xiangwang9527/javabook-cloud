package cn.javabook.chapter10.service;

import cn.javabook.chapter10.exception.AccountProviderException;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.chapter10.dao.RedisDao;
import cn.javabook.chapter10.dao.MySQLDao;
import cn.javabook.chapter10.entity.UserBill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户账户Service实现
 * 
 */
@Service
public class AccountService extends BaseService {
	private static final long serialVersionUID = 8949846737988068097L;

	@Resource
	private RedisDao redisDao;

	@Resource
	private MySQLDao mySQLDao;

	// redis：余额更新状态是否已保存
	public Object getSavedObject(String key) {
		return redisDao.get(key);
	}

	// redis：保存余额更新状态（Object，1天失效）
	public void setSavedObject(String key, String value, long expire) {
		if (0 == expire) {
			redisDao.set(key, value);
		} else {
			redisDao.set(key, value, expire);
		}
	}

	// 更新用户余额
	@Transactional(rollbackFor = Exception.class)
	public synchronized void updateBalance(Long guid, int money) throws Exception {
		StringBuffer sb = null;
		try {
			sb = new StringBuffer("UPDATE sys_user_amount SET balance = balance + ");
			sb.append(money).append(" WHERE guid = ").append(guid).append(" LIMIT 1");
			mySQLDao.update(sb.toString());
		} catch (Exception e) {
			logger.error("更新用户余额异常，用户编码：{}", guid);
			throw AccountProviderException.EXCEPTION_ACCOUNT_UPDATE_BALANCE;
		}
	}

	// 查询用户账单
	public UserBill queryBill(Long taskid, Long userid, int billobject, Long objectid, int billtype, int paytype, int money) {
		StringBuffer sb = null;
		try {
			sb = new StringBuffer("SELECT guid, taskid, userid, outerno, billobject, objectid, billtype, paytype, money, subject, billtime FROM sys_user_bill WHERE ");
			sb.append("taskid = ").append(taskid).append(" AND ");
			sb.append("userid = ").append(userid).append(" AND ");
			sb.append("billobject = ").append(billobject).append(" AND ");
			sb.append("objectid = ").append(objectid).append(" AND ");
			sb.append("billtype = ").append(billtype).append(" AND ");
			sb.append("paytype = ").append(paytype).append(" AND ");
			sb.append("money = ").append(money).append(" LIMIT 1");
			return (UserBill) mySQLDao.findOne(sb.toString(), new UserBill());
		} catch (Exception e) {
			logger.error("查询用户账单异常，用户编码：{}，订单编码：{}，对方用户编码：{}，账单类别：{}，支付类别：{}", userid, taskid, objectid, billtype, paytype);
			throw AccountProviderException.EXCEPTION_ACCOUNT_QUERY_BILL;
		}
	}

	// 创建用户账单
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean createBill(Long taskid, Long userid, String outerno, int billobject, Long objectid, int billtype, int paytype, int money, String subject) throws Exception {
		StringBuffer sb = null;
		Long guid = nextId(GlobalConstant.GUID_APP_TRADE_DATANODE, 1L);

		try {
			sb = new StringBuffer("INSERT INTO sys_user_bill (guid, taskid, userid, outerno, billobject, objectid, billtype, paytype, money, subject) VALUES (");
			sb.append(guid).append(", ");
			sb.append(taskid).append(", ");
			sb.append(userid).append(", '");
			sb.append(outerno).append("', ");
			sb.append(billobject).append(", ");
			sb.append(objectid).append(", ");
			sb.append(billtype).append(", ");
			sb.append(paytype).append(", ");
			sb.append(money).append(", '");
			sb.append(subject).append("')");

			long flag = mySQLDao.create(sb.toString());
			return 0 == flag;
		} catch (Exception e) {
			logger.error("创建用户账单异常，账单编码：{}，用户编码：{}，订单编码：{}，商户订单号：{}，对方用户编码：{}，账单类别：{}，支付类别：{}", guid, userid, taskid, outerno, objectid, billtype, paytype);
			throw AccountProviderException.EXCEPTION_ACCOUNT_CREATE_BILL;
		}
	}
}
