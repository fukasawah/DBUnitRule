package ryzzy.dbunitrule.matcher;

import java.util.List;

import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.hamcrest.Matcher;

import ryzzy.dbunitrule.data.ColumnValue;

public class IgnoreColumnValueHandler extends DiffCollectingFailureHandler
{
	private final Matcher<ColumnValue> ignoreColumnValueMatcher;

	public IgnoreColumnValueHandler(Matcher<ColumnValue> ignoreColumnValueMatcher)
	{
		this.ignoreColumnValueMatcher = ignoreColumnValueMatcher;
	}

	@Override
	public void handle(Difference diff)
	{
		ColumnValue cv = new ColumnValue(diff.getColumnName(), diff.getActualValue());
		if (ignoreColumnValueMatcher.matches(cv)) {
			return;
		}
		super.handle(diff);
	}

	public void verify()
	{
		if (!getDiffList().isEmpty())
		{
			throw this.createFailure(this.toDiffString());
		}
	}

	// pretty assertion message.
	protected String toDiffString()
	{
		if (getDiffList().isEmpty())
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();

		sb.append("Difference");
		sb.append("\n");

		@SuppressWarnings("unchecked")
		List<Difference> diffList = getDiffList();

		for (Difference diff : diffList)
		{
			String tableName = diff.getExpectedTable().getTableMetaData().getTableName();
			String columnName = diff.getColumnName();
			Object expectedValue = diff.getExpectedValue();
			Object actualValue = diff.getActualValue();

			int rowIndex = diff.getRowIndex();

			sb.append("  >> ");
			sb.append("[");
			sb.append("tableName=\"").append(tableName).append("\"");
			sb.append(", row=").append(rowIndex);
			sb.append(", columnName=\"").append(columnName).append("\"");
			sb.append(", expected=\"").append(expectedValue).append("\"");
			sb.append(", actual=\"").append(actualValue).append("\"");
			sb.append("]");

			sb.append("\n");
		}

		return sb.toString();
	}
}
