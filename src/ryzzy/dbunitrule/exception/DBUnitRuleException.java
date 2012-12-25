package ryzzy.dbunitrule.exception;

public class DBUnitRuleException extends RuntimeException {
	public DBUnitRuleException(String msg) {
		super(msg);
	}

	public DBUnitRuleException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DBUnitRuleException(Throwable cause) {
		super(cause);
	}

}
