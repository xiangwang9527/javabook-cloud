package cn.javabook.chapter10.service;

import cn.javabook.chapter10.exception.AccountingProviderException;
import cn.javabook.cloud.core.constant.GlobalConstant;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.chapter10.dao.RedisDao;
import cn.javabook.chapter10.entity.AccountingVoucher;
import cn.javabook.chapter10.dao.MySQLDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * 会计凭证Service实现
 * 
 */
@Service
public class AccountingVoucherService extends BaseService {
	private static final long serialVersionUID = 4600710796629136344L;

	@Resource
	private RedisDao redisDao;

	@Resource
	private MySQLDao mySQLDao;

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

	// 创建会计分录
	@Transactional(rollbackFor = Exception.class)
	public synchronized boolean createAccountingVoucher(String voucherno, Long requestno, String entrytype, String origin, double payamount, double changeamount, 
			String payeraccountno, String receiveraccountno, double income, double cost, double profit, String remark) throws Exception {
		try {
			Long guid = nextId(GlobalConstant.GUID_APP_ACCOUNTING_DATANODE, 1L);
			StringBuffer sb = new StringBuffer("INSERT INTO sys_accounting_voucher (guid, voucherno, requestno, entrytype, origin, payamount, changeamount, ");
			sb.append("payeraccountno, receiveraccountno, income, cost, profit, remark) VALUES (");
			sb.append(guid).append(", '");
			sb.append(voucherno).append("', ");
			sb.append(requestno).append(", '");
			sb.append(entrytype).append("', '");
			sb.append(origin).append("', ");
			sb.append(payamount).append(", ");
			sb.append(changeamount).append(", '");
			sb.append(payeraccountno).append("', '");
			sb.append(receiveraccountno).append("', ");
			sb.append(income).append(", ");
			sb.append(cost).append(", ");
			sb.append(profit).append(", '");
			sb.append(remark).append("')");
			long flag = mySQLDao.create(sb.toString());
			return 0 == flag;
		} catch (Exception e) {
			logger.error("创建会计分录异常，商户订单号：{}", voucherno);
			throw AccountingProviderException.EXCEPTION_ACCOUNTING_CREATE;
		}
	}

	// 查询会计分录
	public AccountingVoucher queryAccountingVoucherByVoucherno(String voucherno) {
		try {
			StringBuffer sb = new StringBuffer("SELECT guid, voucherno, requestno, entrytype, origin, payamount, changeamount, ");
			sb.append("payeraccountno, receiveraccountno, income, cost, profit, createtime, remark ");
			sb.append("FROM sys_accounting_voucher WHERE voucherno = '").append(voucherno).append("' LIMIT 1");
			return (AccountingVoucher) mySQLDao.findOne(sb.toString(), new AccountingVoucher());
		} catch (Exception e) {
			logger.error("查询会计分录异常，商户订单号：{}", voucherno);
			throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
		}
	}
}
