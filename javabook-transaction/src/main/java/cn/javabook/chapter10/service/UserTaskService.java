package cn.javabook.chapter10.service;

import java.util.Date;

import cn.javabook.chapter10.exception.UserTaskProviderException;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.utils.DateUtil;
import cn.javabook.chapter10.dao.RedisDao;
import cn.javabook.chapter10.dao.MySQLDao;
import cn.javabook.chapter10.entity.UserTask;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * 用户订单Service实现
 * 
 */
@Service
public class UserTaskService extends BaseService {
	private static final long serialVersionUID = 7056494469781733891L;

	@Resource
	private RedisDao redisDao;

	@Resource
	private MySQLDao mySQLDao;

	// redis：支付订单对象是否已保存
	public Object getSavedObject(String key) {
		return redisDao.get(key);
	}

	// redis：保存支付订单对象（Object，1天失效）
	public void setSavedObject(String key, String value, long expire) {
		if (0 == expire) {
			redisDao.set(key, value);
		} else {
			redisDao.set(key, value, expire);
		}
	}

	// redis：删除待完成任务
	public void removeTask(String key) {
		redisDao.remove(key);
	}

	// 创建订单
	@Transactional(rollbackFor = Exception.class)
	public synchronized long createTask(long userid, String province, String city, String citycode, String district, String zipcode, String images, int money,
										String descript, int closetime, int status, int paystatus, int tradetype, int paytype) throws Exception {
		long guid = nextId(GlobalConstant.GUID_APP_TASK_DATANODE, 1L);
		try {
			StringBuilder sb = new StringBuilder("INSERT INTO sys_user_task (guid, userid, province, city, citycode, district, zipcode, images, money, descript, closetime, publishtime, status, paystatus, tradetype, paytype) VALUES (");
			sb.append(guid).append(", ");
			sb.append(userid).append(", '");
			sb.append(province).append("', '");
			sb.append(city).append("', '");
			sb.append(citycode).append("', '");
			sb.append(district).append("', '");
			sb.append(zipcode).append("', '");
			sb.append(images).append("', ");
			sb.append(money).append(", '");
			sb.append(descript).append("', ");
			sb.append(closetime).append(", '");
			sb.append(DateUtil.parse(new Date())).append("', ");
			sb.append(status).append(", ");
			sb.append(paystatus).append(", ");
			sb.append(tradetype).append(", ");
			sb.append(paytype).append(")");
			try {
				long flag = mySQLDao.create(sb.toString());
				if (0 == flag) {
					return guid;
				} else {
					return flag;
				}
			} catch (Exception e) {
				logger.error("创建订单异常，用户编码：{}，订单编码：{}", userid, guid);
				throw UserTaskProviderException.EXCEPTION_TASK_CREATE;
			}
		} catch (Exception e) {
			logger.error("创建订单异常，用户编码：{}", userid);
			throw UserTaskProviderException.EXCEPTION_TASK_CREATE;
		}
	}

	// 查询订单
	public UserTask queryTask(Long guid) {
		StringBuilder sb = new StringBuilder("SELECT guid, userobject, userid, acceptobject, accepterid, province, city, citycode, district, zipcode, images, ");
		sb.append("money, descript, closetime, publishtime, accepttime, finishtime, modifiedtime, canceltime, confirmtime, type, status, paystatus, tradetype, paytype ");
		sb.append("FROM sys_user_task WHERE guid = ").append(guid).append(" LIMIT 1");
		try {
			return (UserTask) mySQLDao.findOne(sb.toString(), new UserTask());
		} catch (DataAccessException e) {
			logger.error("查询订单异常，订单编码：{}", guid);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}

	// 更新订单状态
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean updateTaskStatus(Long guid) throws Exception {
		StringBuilder sb = new StringBuilder("UPDATE sys_user_task SET status = 1, paystatus = 1, publishtime = '").append(DateUtil.parse(new Date())).append("' ");
		sb.append("WHERE guid = ").append(guid).append(" LIMIT 1");
		try {
			return mySQLDao.update(sb.toString());
		} catch (Exception e) {
			logger.error("更新订单状态异常，订单编码：{}", guid);
			throw UserTaskProviderException.EXCEPTION_TASK_UPDATE;
		}
	}

	// 封装任务UserTask
	private UserTask sealUserTask(long taskid, long userid, String province, String city, String citycode, String district, String zipcode, String images,
								  int money, String descript, int closetime, int status, int paystatus, int tradetype, int paytype) {
		UserTask task = new UserTask();
		task.setGuid(taskid);
		task.setUserid(userid);
		task.setProvince(province);
		task.setCity(city);
		task.setCitycode(citycode);
		task.setDistrict(district);
		task.setZipcode(zipcode);
		task.setImages(images);
		task.setMoney(money);
		task.setDescript(descript);
		task.setClosetime(closetime);
		task.setStatus(status);
		task.setPaystatus(paystatus);
		task.setTradetype(tradetype);
		task.setPaytype(paytype);
		return task;
	}
}
