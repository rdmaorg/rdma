package ie.clients.gdma2.domain.ui;

import org.springframework.util.StringUtils;

public class Filter {
	
	private Integer columnId;

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

	
	/*mapping to UI dropdown for filter selection*/
	protected final int OPERATOR_PLEASE_SELECT = -1;
	protected final String OPERATOR_PLEASE_SELECT_TEXT = "Please Select";
	
	protected final int OPERATOR_EQUAL_TO = 0;
	protected final String OPERATOR_EQUAL_TO_TEXT = "Equal To";
	
	protected final int OPERATOR_LESS_THAN = 1;
	protected final String OPERATOR_LESS_THAN_TEXT = "Less Than";
	
	protected final int OPERATOR_LESS_THAN_OR_EQUAL = 2;
	protected final String OPERATOR_LESS_THAN_OR_EQUAL_TEXT = "Less Than Or Equal To";
	
	protected final int OPERATOR_GREATER_THAN = 3;
	protected final String OPERATOR_GREATER_THAN_TEXT = "Greater Than";
	
	protected final int OPERATOR_GREATHER_THAN_OR_EQUAL_TO = 4;
	protected final String OPERATOR_GREATHER_THAN_OR_EQUAL_TO_TEXT = "Greater Than Or Equal To";
	
	protected final int OPERATOR_BEGINS_WITH = 5;
	protected final String OPERATOR_BEGINS_WITH_TEXT = "Begins With";
	
	protected final int OPERATOR_CONTAINS = 6;
	protected final String OPERATOR_CONTAINS_TEXT = "Contains";
	
	protected final int OPERATOR_ENDS_WITH = 7;
	protected final String OPERATOR_ENDS_WITH_TEXT= "Ends With";
	
	protected final int OPERATOR_IS_NULL = 8;
	protected final String OPERATOR_IS_NULL_TEXT = "Is Null";
	
	protected final int OPERATOR_BLANK = 9;
	protected final String OPERATOR_BLANK_TEXT = "Is Blank";
	
	
	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
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
