package cn.javabook.chapter08.facade;

import cn.javabook.chapter08.common.Account;
import cn.javabook.chapter08.common.Order;
import cn.javabook.chapter08.strategyfactory.FinanceContext;
import cn.javabook.chapter08.strategyfactory.StrategyFactory;

/**
 * 结算外观模式
 * 
 */
public class FinanceFacade {
	// 对外公布的结算方法
	public static void doSettle(Account account, Order order) {
		// 创建策略工厂
		StrategyFactory factory = StrategyFactory.getInstance();
		// 初始化
		factory.init();
		// 通过工厂创建一个具体的策略对象并将策略对象包装进上下文中
		FinanceContext context = new FinanceContext(factory.get(account.getAccid()));
		// 通过上下文执行结算处理
		context.settleAccount(account, order);
	}
}
