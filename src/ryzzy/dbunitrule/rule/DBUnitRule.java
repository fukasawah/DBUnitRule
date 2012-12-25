package ryzzy.dbunitrule.rule;

import java.sql.Connection;

import org.dbunit.dataset.IDataSet;
import org.hamcrest.Matcher;
import org.junit.rules.TestRule;

import ryzzy.dbunitrule.data.ColumnValue;
import ryzzy.dbunitrule.matcher.VerifyMatcher;

public interface DBUnitRule extends TestRule {

	void cleanInsert(String filePath);

	void insert(String filePath);

	void expect(String filePath, String... ignoreColumns);

	void expect(String filePath, Matcher<ColumnValue> ignoreMatcher);

	void expect(VerifyMatcher<IDataSet> matcher);

	Connection getConnection();

}
