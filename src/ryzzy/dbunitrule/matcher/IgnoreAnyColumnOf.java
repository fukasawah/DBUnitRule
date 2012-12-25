package ryzzy.dbunitrule.matcher;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import ryzzy.dbunitrule.data.ColumnValue;
import ryzzy.dbunitrule.util.StringUtil;

public class IgnoreAnyColumnOf extends TypeSafeMatcher<ColumnValue> {
	private final boolean caseSensitive;
	private final String[] ignoreColumns;

	public IgnoreAnyColumnOf(String... ignoreColumns) {
		this(/* caseSensitive = */false, ignoreColumns);
	}

	public IgnoreAnyColumnOf(boolean caseSensitive, String... ignoreColumns) {
		this.caseSensitive = caseSensitive;
		this.ignoreColumns = ignoreColumns;
	}

	@Override
	public void describeTo(Description description) {
		// This method is not called
	}

	@Override
	public boolean matchesSafely(ColumnValue actual) {
		if (actual == null) {
			return false;
		}

		String actualColumnName = actual.getColumnName();
		for (String ignoreColumn : ignoreColumns) {
			if (!caseSensitive) {
				actualColumnName = StringUtil.toLower(actualColumnName);
				ignoreColumn = StringUtil.toLower(ignoreColumn);
			}
			if (StringUtil.wildcardMatch(actualColumnName, ignoreColumn)) {
				return true;
			}
		}
		return false;
	}

	public static IgnoreAnyColumnOf ignoreAnyColumnOf(String... ignoreColumns) {
		return new IgnoreAnyColumnOf(ignoreColumns);
	}
}
