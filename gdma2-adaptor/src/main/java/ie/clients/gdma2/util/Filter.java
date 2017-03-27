package ie.clients.gdma2.util;

import org.springframework.util.StringUtils;

public class Filter {

	private Long columnId;

	private String columnName;

	private int filterOperator;

	private String filterOperatorText;

	private int columnType;

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	private String filterValue;

	private boolean blank = false;

	private boolean nullValue = false;

	private boolean orValue = false;  // to deal with OR's in the filter sql

	private boolean notValue = false;  // to deal with NOT's in the filter sql

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(int filterOperator) {
		this.filterOperator = filterOperator;
	}

	public String getfilterOperatorText(){
		return filterOperatorText;
	}

	public void setFilterOperatorText(String filterOperatorText) {
		this.filterOperatorText = filterOperatorText;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public boolean isBlank() {
		if(getFilterOperator() == 9)
			return true;
		else
			return false;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public boolean isOrValue() {
		return orValue;
	}

	public void setOrValue(boolean orValue) {
		this.orValue = orValue;
	}

	public boolean isNotValue() {
		return notValue;
	}

	public void setNotValue(boolean notValue) {
		this.notValue = notValue;
	}

	public boolean isNullValue() {
		if(getFilterOperator() == 8)
			return true;
		else
			return false;
	}

	public void setNullValue(boolean nullValue) {
		this.nullValue = nullValue;
	}

	public boolean isValid() {
		return isBlank() || isNullValue() || StringUtils.hasText(filterValue);
	}

	public String toString() {
		return "Filter [columnId=" + columnId + ", columnName="
				+ columnName + ", columnType=" + columnType + ", filterOperator=" + filterOperator + ", filterValue="
				+ filterValue + ", nullValue=" + nullValue + ", notValue=" + notValue + ", blank=" + blank + ", orValue=" + orValue + "]";
	}
}
