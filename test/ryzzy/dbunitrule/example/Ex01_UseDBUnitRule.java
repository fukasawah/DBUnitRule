package ryzzy.dbunitrule.example;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import ryzzy.dbunitrule.data.ColumnValue;
import ryzzy.dbunitrule.matcher.IgnoreAnyColumnOf;
import ryzzy.dbunitrule.mock.ConnectionFactory;
import ryzzy.dbunitrule.rule.DBUnitRule;
import ryzzy.dbunitrule.rule.DBUnitRuleFactory;

public class Ex01_UseDBUnitRule {

	// Create rule.
	// Don't use @ClassRule annotation.
	@Rule
	public DBUnitRule dbrule = DBUnitRuleFactory.createFileBackupDBUnitRule(
			ConnectionFactory.createMySQLConnection("test_resources/dbunitrule.properties")
			);

	// Ignore DATE columns. usable wildcard.
	private static final Matcher<ColumnValue> IGNORE_DATE_COLUMN_MATCHER = IgnoreAnyColumnOf.ignoreAnyColumnOf("*_DATE");

	// Database data resource file. Supported file extension is xml, xls.
	private static final String SAMPLE_DATASET = "test_resources/example1.xls";

	// Get connection.
	public final Connection con = dbrule.getConnection();

	// Insert data equals expected data.
	@Test
	public void initializeTest() {
		dbrule.cleanInsert(SAMPLE_DATASET); // Initialize the data in the database by resource file.
		dbrule.expect(SAMPLE_DATASET); // Can expected database state by resource file after the test.
	}

	// (Example) Select Test.
	@Test
	public void selectTest() throws SQLException {
		dbrule.cleanInsert(SAMPLE_DATASET);

		PreparedStatement ps = con.prepareStatement("select * from T_USER where ADDRESS = ? ORDER BY USER_ID DESC");
		ResultSet rs = null;
		ps.setString(1, "tokyo");

		try {
			rs = ps.executeQuery();
			assertThat(rs.next(), is(true));
			assertThat(rs.getString("NAME"), is("ken"));

			assertThat(rs.next(), is(true));
			assertThat(rs.getString("NAME"), is("yamada"));

			assertThat(rs.next(), is(false));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
	}

	// (Example) Insert Test.
	// Ignores the value that matches the column named "*_DATE"
	@Test
	public void insertTest() throws SQLException {
		dbrule.cleanInsert(SAMPLE_DATASET);
		dbrule.expect("test_resources/insertTest_expected.xls", IGNORE_DATE_COLUMN_MATCHER); // Ignore *_DATE columns.

		PreparedStatement ps = con.prepareStatement("insert into T_USER values ('user4', 'John', 23, 'kyoto', now())"); // Cannot predict value of INSERT_DATE.

		assertThat(ps.executeUpdate(), is(1));
	}
}
