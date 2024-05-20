package cn.javabook.chapter08.template;

import cn.javabook.chapter08.strategy.PayByBalance;

/**
 * 余额支付类
 *
 */
public class BalancePayment extends Payment {
    /**
     * 构造器
     *
     * @param strategy
     */
    public BalancePayment(PayByBalance strategy) {
        this.strategy = strategy;
    }

    /**
     * 支付前锁定账户
     *
     */
    @Override
    protected void payBefore() {
        System.out.println("payBefore：余额支付前处理");
    }

    /**
     * 支付后增加积分
     *
     */
    @Override
    protected void payAfter() {
        System.out.println("payAfter：余额支付后处理");
    }
}
