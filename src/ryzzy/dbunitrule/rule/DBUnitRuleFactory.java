package ryzzy.dbunitrule.rule;

import java.sql.Connection;

import ryzzy.dbunitrule.rule.impl.FileBackupDBUnitRule;
import ryzzy.dbunitrule.rule.impl.PlainDBUnitRule;
import ryzzy.dbunitrule.rule.impl.TransactionDBUnitRule;

public class DBUnitRuleFactory {
	private DBUnitRuleFactory() {
	}

	//	public static DBUnitRule create(String prefixTypeName) {
	//
	//	}

	/**
	 * <pre>
	 * The data of a database is saved to a file before the test, and restored after the test.
	 * When canceling or parallel execution, the database can not be recovered.
	 * </pre>
	 * 
	 * @param con
	 * @return
	 */
	public static DBUnitRule createFileBackupDBUnitRule(Connection con) {
		return new FileBackupDBUnitRule(con);
	}

	/**
	 * <pre>
	 * Not commited DBUnitRule.	Connection#setAutoCommit is set as false
	 * Commit operation is ignored.
	 * The data before a test is restored by a rollback.
	 * </pre>
	 * 
	 * @param con
	 * @return
	 */
	public static DBUnitRule createTransactionalDBUnitRule(Connection con) {
		return new TransactionDBUnitRule(con);
	}

	/**
	 * <pre>
	 * Plain DBUnitRule.
	 * When testing, the data is lost in the database.
	 * </pre>
	 * 
	 * @param con
	 * @return
	 */
	public static DBUnitRule createPlainDBUnitRule(Connection con) {
		return new PlainDBUnitRule(con);
	}
}
