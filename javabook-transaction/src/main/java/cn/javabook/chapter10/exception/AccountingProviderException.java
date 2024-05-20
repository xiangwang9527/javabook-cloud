package cn.javabook.chapter10.exception;

import cn.javabook.cloud.core.parent.BaseException;

/**
 * 会计分录异常类
 * 
 */
public class AccountingProviderException extends BaseException {
	private static final long serialVersionUID = 631729762859751188L;

	public static final AccountingProviderException EXCEPTION_ACCOUNTING_CREATE = new AccountingProviderException(15200, "create accounting exception"); // 创建会计分录异常

	public AccountingProviderException() {
		super();
	}

	public AccountingProviderException(int errorcode, String errormsg, Object... args) {
		super(errorcode, errormsg, args);
	}

	public AccountingProviderException(int errorcode, String errormsg) {
		super(errorcode, errormsg);
	}
}
