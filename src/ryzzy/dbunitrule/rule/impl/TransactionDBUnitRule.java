package ryzzy.dbunitrule.rule.impl;

import java.sql.Connection;
import java.sql.SQLException;

import ryzzy.dbunitrule.data.NoCommitConnection;
import ryzzy.dbunitrule.rule.AbstractDBUnitRule;

public class TransactionDBUnitRule extends AbstractDBUnitRule {
	private final Connection fakeConnection;

	public TransactionDBUnitRule(Connection con) {
		super(con);
		fakeConnection = new NoCommitConnection(con);
	}

	protected Connection getOriginalConnection() {
		return super.getConnection();
	}

	@Override
	public Connection getConnection() {
		return fakeConnection;
	}

	@Override
	protected void rollback() throws SQLException {
		getOriginalConnection().rollback();
		super.rollback();
	}
}