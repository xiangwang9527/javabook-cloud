package cn.javabook.chapter08.strategyfactory;

import cn.javabook.chapter08.common.Account;
import cn.javabook.chapter08.common.Order;

/**
 * 结算策略接口
 * 
 */
public interface Finance {
	// 结算接口
	public void settleAccount(Account account, Order order);
}
