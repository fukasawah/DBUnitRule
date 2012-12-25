package ryzzy.dbunitrule.data;

public class ColumnValue {
	private final String columnName;
	private final Object value;

	public ColumnValue(String columnName, Object value) {
		this.columnName = columnName;
		this.value = value;
	}

	public String getColumnName() {
		return columnName;
	}

	public Object getValue() {
		return value;
	}

}
