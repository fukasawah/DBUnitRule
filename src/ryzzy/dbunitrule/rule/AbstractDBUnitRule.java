package ryzzy.dbunitrule.rule;

import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.assertion.DbAssertionFailedError;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.hamcrest.Matcher;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import ryzzy.dbunitrule.data.ColumnValue;
import ryzzy.dbunitrule.exception.DBUnitRuleException;
import ryzzy.dbunitrule.matcher.DataSetOf;
import ryzzy.dbunitrule.matcher.IgnoreAnyColumnOf;
import ryzzy.dbunitrule.matcher.VerifyMatcher;
import ryzzy.dbunitrule.util.DBUnitRuleUtil;

public abstract class AbstractDBUnitRule extends TestWatcher implements DBUnitRule {

	private final Connection con;
	private VerifyMatcher<IDataSet> expectedDataSetMatcher;

	public AbstractDBUnitRule(Connection con) {
		this.con = con;
	}

	@Override
	public Connection getConnection() {
		return con;
	}

	@Override
	public void cleanInsert(String filePath) {
		DBUnitRuleUtil.cleanInsert(getConnection(), filePath);
	}

	@Override
	public void insert(String filePath) {
		DBUnitRuleUtil.insert(getConnection(), filePath);
	}

	@Override
	public void expect(String filePath, String... ignoreColumns) {
		this.expect(filePath, new IgnoreAnyColumnOf(ignoreColumns));
	}

	@Override
	public void expect(String filePath, Matcher<ColumnValue> ignoreMatcher) {
		this.expect(getExpectedDataSetMatcher(filePath, ignoreMatcher));
	}

	@Override
	public void expect(VerifyMatcher<IDataSet> matcher) {
		this.expectedDataSetMatcher = matcher;
	}

	@Override
	protected void succeeded(Description description) {
		try {
			if (expectedDataSetMatcher != null) {
				String[] tableNames = expectedDataSetMatcher.getExpectedObject().getTableNames();
				assertThat(DBUnitRuleUtil.queryDataSet(getConnection(), tableNames), expectedDataSetMatcher);
			}
		} catch (DbAssertionFailedError e) {
			throw new AssertionError(e);
		} catch (AssertionError e) {
			throw e;
		} catch (DataSetException e) {
			throw new DBUnitRuleException(e);
		}
	}

	@Override
	protected void finished(Description description) {
		if (con != null) {
			try {
				rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	};

	protected void rollback() throws SQLException {
	}

	public static VerifyMatcher<IDataSet> getExpectedDataSetMatcher(String filePath) {
		return DataSetOf.dataSetOf(DBUnitRuleUtil.loadDataSet(filePath));
	}

	public static VerifyMatcher<IDataSet> getExpectedDataSetMatcher(String filePath, Matcher<ColumnValue> ignoreMatcher) {
		return DataSetOf.dataSetOf(DBUnitRuleUtil.loadDataSet(filePath), ignoreMatcher);
	}
}
