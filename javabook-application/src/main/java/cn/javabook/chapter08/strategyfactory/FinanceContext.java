package cn.javabook.chapter08.strategyfactory;

import cn.javabook.chapter08.common.Account;
import cn.javabook.chapter08.common.Order;

/**
 * 策略模式封装
 * 
 */
public class FinanceContext {
	// 结算策略
	private final Finance finance;

	public FinanceContext(Finance finance) {
		this.finance = finance;
	}

	// 执行结算
	public void settleAccount(Account account, Order order) {
		this.finance.settleAccount(account, order);
	}
}
