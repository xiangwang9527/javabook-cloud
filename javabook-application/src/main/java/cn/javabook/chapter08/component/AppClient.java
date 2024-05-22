package cn.javabook.chapter08.component;

import cn.javabook.chapter08.chain.*;
import cn.javabook.chapter08.common.Account;
import cn.javabook.chapter08.common.Order;
import cn.javabook.chapter08.facade.FinanceFacade;
import cn.javabook.chapter08.iterator.Cart;
import cn.javabook.chapter08.iterator.ConcreteCart;
import cn.javabook.chapter08.iterator.Iterator;
import cn.javabook.chapter08.memento.Caretaker;
import cn.javabook.chapter08.memento.Originator;
import cn.javabook.chapter08.observer.AlipayPayment;
import cn.javabook.chapter08.observer.BalancePayment;
import cn.javabook.chapter08.observer.WeixinPayment;
import cn.javabook.chapter08.strategy.PayByBalance;
import cn.javabook.chapter08.strategy.PayByAlipay;
import cn.javabook.chapter08.strategy.PayByWeixin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端应用
 * 
 */
public class AppClient {
	// 获得键盘输入
	private static String getInput() {
		String str = "";
		try {
			str = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 初始化一个账户
	private static Account initAccount() {
		Account account = new Account();
		// 增加提示，手动输入账户类型是为了处理结算模式
		System.out.println("请输入账户编码（请以t1或非t1开头）");
		// 这里将原来的account.setAccid("1")改为接收键盘输入的方式
		account.setAccid(getInput());
		account.setDeposit(500);
		account.setBalance(200);
		return account;
	}

	// 创建一笔订单
	private static Order createOrder() {
		Order order = new Order();
		System.out.print("请输入交易编号：");
		order.setOid(getInput());
		System.out.print("请输入交易金额：");
		order.setAmount(Integer.parseInt(getInput()));
		// 返回订单
		return order;
	}

	// 提现
	private static void withdraw(double amount) {
		// 组装责任链
		WithdrawHandler handler1 = new NoAuditHandler();
		WithdrawHandler handler2 = new RiskDeptHandler();
		WithdrawHandler handler3 = new ViceHandler();
		WithdrawHandler handler4 = new BankHandler();
		handler1.setNext(handler2);
		handler2.setNext(handler3);
		handler3.setNext(handler4);
		// 提交请求
		handler1.handle(amount);
	}

	// 支付
	private static void pay(Account account, double amount) {
		System.out.print("请选择支付类型：");
		String type = getInput();
		switch (type) {
			case "alipay":
				AlipayPayment alipayPayment = new AlipayPayment(new PayByAlipay());
				alipayPayment.templateMethod(amount);
				// 注册订阅者
				alipayPayment.addSubscriber(account);
				// 通知订阅者
				alipayPayment.notifySubscribers("支付宝", 5);
				break;
			case "weixin":
				WeixinPayment weixinPayment = new WeixinPayment(new PayByWeixin());
				weixinPayment.templateMethod(amount);
				// 注册订阅者
				weixinPayment.addSubscriber(account);
				// 通知订阅者
				weixinPayment.notifySubscribers("微信支付", 5);
				break;
			case "yue":
				BalancePayment balancePayment = new BalancePayment(new PayByBalance());
				balancePayment.templateMethod(amount);
				// 注册订阅者
				balancePayment.addSubscriber(account);
				// 通知订阅者
				balancePayment.notifySubscribers("余额支付", 20);
				break;
			default:
				break;
		}
	}

	// 打印出当前卡内交易余额
	private static void showAccount(Account account) {
		System.out.println("账户编号:" + account.getAccid());
		System.out.println("账户押金：" + account.getDeposit() + " 元");
		System.out.println("账户余额：" + account.getBalance() + " 元");
	}

	// 模拟交易
	public static void main(String[] args) {
		Account account = initAccount();
		System.out.println("\n======== 初始化账户信息 =========");
		showAccount(account);
		System.out.println();

		// 创建一笔订单
		Order order = createOrder();
		// 支付
		pay(account, order.getAmount());

		// 执行结算模式
		// if (account.getAccid().contains("t1")) {
		// 	new FinanceContext(new T1Finance()).settleAccount(account, order);
		// }
		// if (account.getAccid().contains("t7")) {
		// 	new FinanceContext(new T7Finance()).settleAccount(account, order);
		// }
		// 创建策略工厂
		// StrategyFactory factory = StrategyFactory.getInstance();
		// // 初始化
		// factory.init();
		// // 通过工厂获取一个具体的策略对象
		// Finance finance = factory.get(account.getAccid());
		// // 将策略对象包装进上下文中
		// FinanceContext context = new FinanceContext(finance);
		// // 通过上下文执行结算处理
		// context.settleAccount(account, order);
		// 调用外观模式
		FinanceFacade.doSettle(account, order);

		// 交易成功，打印出成功处理消息
		System.out.println("\n========= 订单凭证 =========");
		System.out.println(order.getOid() + " 订单交易成功！");
		System.out.println("本次支付的订单金额为：" + order.getAmount() + " 元");

		// 创建交易快照
		Originator originator = new Originator();
		Caretaker caretaker = new Caretaker(originator);
		originator.setSnapshot(account.toString() + " | " + order.toString());
		System.out.println("创建交易快照：" + originator.getSnapshot());
		// 保存交易快照
		caretaker.save();

		// 看看账户余额
		System.out.println("\n======== 支付后账户信息 =========");
		showAccount(account);

		// 提现
		withdraw(order.getAmount());
		// 提现后余额
		account.setBalance(account.getBalance() - order.getAmount());

		// 看看账户余额
		System.out.println("\n======== 提现后账户信息 =========");
		showAccount(account);

		// 往购物车中添加商品
		Cart cart = new ConcreteCart();
		cart.add(new cn.javabook.chapter08.iterator.Product("白菜", 1, 3.8));
		cart.add(new cn.javabook.chapter08.iterator.Product("大米", 1, 88.9));
		cart.add(new cn.javabook.chapter08.iterator.Product("纸巾", 1, 24.8));
		cart.add(new cn.javabook.chapter08.iterator.Product("方便面", 1, 33.7));
		List<String> list = new ArrayList<>();
		list.iterator();
		// 开始抽检
		Iterator iterator = cart.createIterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.getNext().toString() + "\t");
		}

		// 打印购物小票
		double total = 0;
		SubOrder seller1, mediumOrder1, smallOrder1, smallOrder2;
		seller1 = new SubOrder("商家1");
		mediumOrder1 = new SubOrder("商家子订单1");
		smallOrder1 = new SubOrder("商家小订单1");
		smallOrder2 = new SubOrder("商家小订单2");
		smallOrder1.add(new Product("面条", 2, 4.9));
		smallOrder1.add(new Product("老干妈", 1, 9.8));
		smallOrder2.add(new Product("毛巾", 1, 14.2));
		smallOrder2.add(new Product("洗发水", 1, 59.9));
		mediumOrder1.add(smallOrder1);
		mediumOrder1.add(smallOrder2);
		seller1.add(mediumOrder1);
		System.out.println("您选购的商品有：");
		seller1.receipt();
		total = seller1.calculation();
		System.out.println("要支付的总价是：" + String.format("%.2f", total) + " 元");
	}
}
