package ryzzy.dbunitrule.matcher;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.SortedDataSet;
import org.hamcrest.Matcher;

import ryzzy.dbunitrule.data.ColumnValue;

public class DataSetOf extends VerifyMatcher<IDataSet>
{
	private final Matcher<ColumnValue> ignoreColumnValueMatcher;

	public DataSetOf(IDataSet expectedDataset, Matcher<ColumnValue> ignoreColumnValueMatcher)
	{
		super(expectedDataset);
		this.ignoreColumnValueMatcher = ignoreColumnValueMatcher;
	}

	@Override
	protected void verify() throws Exception
	{
		IDataSet expected = new SortedDataSet(getExpectedObject());
		IDataSet actual = new SortedDataSet(getActualObject());

		IgnoreColumnValueHandler handler = new IgnoreColumnValueHandler(ignoreColumnValueMatcher);
		Assertion.assertEquals(expected, actual, handler);
		handler.verify();
	}

	public static DataSetOf dataSetOf(IDataSet expectedDataset, String... ignoreColumns)
	{
		return dataSetOf(expectedDataset, new IgnoreAnyColumnOf(ignoreColumns));
	}

	public static DataSetOf dataSetOf(IDataSet expectedDataset, Matcher<ColumnValue> ignoreColumnValueMatcher)
	{
		return new DataSetOf(expectedDataset, ignoreColumnValueMatcher);
	}
}
