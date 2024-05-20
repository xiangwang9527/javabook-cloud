package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 账户异常类
 * 
 */
public class AccountProviderException extends BaseException {
	private static final long serialVersionUID = 631729762859751188L;

	public static final AccountProviderException EXCEPTION_ACCOUNT_CREATE = new AccountProviderException(15100, "create account exception"); // 创建用户账户异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_CREATE_BILL = new AccountProviderException(15101, "create bill exception"); // 创建用户账单异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_CREATE_THIRDPAY = new AccountProviderException(15102, "create third pay account exception"); // 创建第三方支付账户信息异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_UPDATE_PAYKEY = new AccountProviderException(15103, "update paykey exception"); // 更新用户支付信息异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_UPDATE_PAYKAY_PASSWORD = new AccountProviderException(15104, "update paykey password exception"); // 更新用户支付密码异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_UPDATE_BALANCE = new AccountProviderException(15105, "update balance exception"); // 更新用户余额异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_QUERY_BILL = new AccountProviderException(15106, "query bill exception"); // 查询用户账单异常
	public static final AccountProviderException EXCEPTION_ACCOUNT_UPDATE_BILL = new AccountProviderException(15107, "update bill exception"); // 更新用户账单异常

	public AccountProviderException() {
		super();
	}

	public AccountProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public AccountProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
