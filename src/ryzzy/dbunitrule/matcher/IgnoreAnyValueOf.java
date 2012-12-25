package ryzzy.dbunitrule.matcher;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import ryzzy.dbunitrule.data.ColumnValue;

public class IgnoreAnyValueOf extends TypeSafeMatcher<ColumnValue> {

	private final Object[] ignoreValues;

	public IgnoreAnyValueOf(Object... ignoreValues) {
		this.ignoreValues = ignoreValues;
	}

	@Override
	public void describeTo(Description description) {
		// This method is not called
	}

	@Override
	public boolean matchesSafely(ColumnValue columnValue) {
		Object value;
		if (columnValue == null) {
			value = null;
		} else {
			value = columnValue.getValue();
		}

		for (Object ignoreValue : ignoreValues) {
			if ((ignoreValue == null && value == null) || (ignoreValue != null && ignoreValue.equals(value))) {
				return true;
			}
		}
		return false;
	}
}
