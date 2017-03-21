package ie.clients.gdma2.util;

/**
 * Helper class - populated from DB column medatadata
 * and used to create Column Entity
 * @author Avnet
 *
 */
public class ColumnMetaData {

	private String columnClassName;
	private int columnDisplaySize;
	private String columnLabel;
	private String columnName;
	private int columnType;
	private String columnTypeName;
	
	private boolean autoIncrement;
	//the nullability status of the given column; one of columnNoNulls, columnNullable or columnNullableUnknown
	private int nullable;
	private boolean writable;
	
	public ColumnMetaData(String columnClassName, int columnDisplaySize,
			String columnLabel, String columnName, int columnType,
			String columnTypeName, boolean autoIncrement, int nullable,
			boolean writable) {
		
		this.columnClassName = columnClassName;
		this.columnDisplaySize = columnDisplaySize;
		this.columnLabel = columnLabel;
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnTypeName = columnTypeName;
		this.autoIncrement = autoIncrement;
		this.nullable = nullable;
		this.writable = writable;
	}
	
	

	public String getColumnClassName() {
		return columnClassName;
	}



	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}



	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}



	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}



	public String getColumnLabel() {
		return columnLabel;
	}



	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}



	public String getColumnName() {
		return columnName;
	}



	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}



	public int getColumnType() {
		return columnType;
	}



	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}



	public String getColumnTypeName() {
		return columnTypeName;
	}



	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}



	public boolean isAutoIncrement() {
		return autoIncrement;
	}



	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}



	public int getNullable() {
		return nullable;
	}



	public void setNullable(int nullable) {
		this.nullable = nullable;
	}



	public boolean isWritable() {
		return writable;
	}



	public void setWritable(boolean writable) {
		this.writable = writable;
	}



	@Override
	public String toString() {
		return "ColumnMetaData [columnClassName=" + columnClassName
				+ ", columnDisplaySize=" + columnDisplaySize + ", columnLabel="
				+ columnLabel + ", columnName=" + columnName + ", columnType="
				+ columnType + ", columnTypeName=" + columnTypeName
				+ ", autoIncrement=" + autoIncrement + ", nullable=" + nullable
				+ ", writable=" + writable + "]";
	}
	
	
	
}
