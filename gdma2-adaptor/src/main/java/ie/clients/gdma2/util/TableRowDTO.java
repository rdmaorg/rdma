package ie.clients.gdma2.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TableRowDTO {

	private BigInteger rowNumber;
	private List<TableRowDTO.TableColumn> columns = new ArrayList<TableRowDTO.TableColumn>();
	
	public BigInteger getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(BigInteger rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<TableRowDTO.TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableRowDTO.TableColumn> columns) {
		this.columns = columns;
	}

	private void exampleRowCreation(){
		columns.add(new TableColumn("name", new String("some name")));
		columns.add(new TableColumn("age", new Integer(47)));
		
		//	TableRowDTO.TableColumn tableRow =  tableRowDTO.new TableColumn(columnName, resultSetValue);
	}
	
	
	/*every cell in the row is column represented with name and value*/
	public class TableColumn{
		private String columnName;
		private Object val;
		
		public TableColumn(String name, Object val) {
			this.columnName = name;
			this.val = val;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public Object getVal() {
			return val;
		}

		public void setVal(Object val) {
			this.val = val;
		}
		
		
	}
	
	
	
}
