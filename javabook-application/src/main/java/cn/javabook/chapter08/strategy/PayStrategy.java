package cn.javabook.chapter08.strategy;

/**
 * 支付策略接口
 * 
 */
public interface PayStrategy {
	// 支付方法
	boolean pay(double amount);
}
