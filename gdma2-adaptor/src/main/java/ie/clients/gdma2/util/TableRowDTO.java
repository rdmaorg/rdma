package ie.clients.gdma2.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**

{  
   "data":[  
      {  
         "rowNumber":1,
         "columns":[  
            {  
               "columnName":"country",
               "val":"France"
            },
            {  
               "columnName":"city",
               "val":"Nantes"
            },
            {  
               "columnName":"contactFirstName",
               "val":"Carine "
            },
            {  
               "columnName":"membership_id",
               "val":{  
                  "value":"101",
                  "did":2255,
                  "sid":2254,
                  "dropdownOptions":[  
                     [  
                        0,
                        103,
                        "gold"
                     ],
                     [  
                        1,
                        101,
                        "ordinary"
                     ],
                     [  
                        2,
                        102,
                        "silver"
                     ]
                  ]
               }
            },
          
            {  
               "columnName":"state",
               "val":null
            }
         ]
      },
      {  
         "rowNumber":2,
         "columns":[  
            {  
               "columnName":"country",
               "val":"USA"
            }
          ]
      }
   ],
   "draw":0,
   "recordsTotal":122,
   "recordsFiltered":122
}
            
 * @author Avnet
 *
 */
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
	
	void populateandTest(){
		List<TableRowDTO> rows = new ArrayList<TableRowDTO>();
		
		TableRowDTO row1 = new TableRowDTO();
		row1.setRowNumber(new BigInteger("0"));
		
		List<TableRowDTO.TableColumn> colsrow1 = new ArrayList<TableRowDTO.TableColumn>();
		
		colsrow1.add(new TableColumn("name", new String("Ana")));
		colsrow1.add(new TableColumn("age", new Integer(47)));
		colsrow1.add(new TableColumn("adress", new String("AA_street")));
		
		row1.setColumns(colsrow1);
		
		//row2
		TableRowDTO row2 = new TableRowDTO();
		row2.setRowNumber(new BigInteger("1"));
		
		List<TableRowDTO.TableColumn> colsrow2 = new ArrayList<TableRowDTO.TableColumn>();
		
		colsrow2.add(new TableColumn("name", new String("Pier")));
		colsrow2.add(new TableColumn("age", new Integer(22)));
		colsrow2.add(new TableColumn("adress", new String("BB_street")));
		
		row2.setColumns(colsrow2);
		
		//row3
		
		TableRowDTO row3 = new TableRowDTO();
		row3.setRowNumber(new BigInteger("2"));
		
		List<TableRowDTO.TableColumn> colsrow3 = new ArrayList<TableRowDTO.TableColumn>();
		
		colsrow3.add(new TableColumn("name", new String("Eve")));
		colsrow3.add(new TableColumn("age", new Integer(48)));
		colsrow3.add(new TableColumn("adress", new String("CC_street")));
		
		row3.setColumns(colsrow3);
		
		//list
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		
		//print result
		printRows(rows);
		
		//createPlainList and print it
		createPlainList(rows);
	}
	
	void printRows(List<TableRowDTO> rows){
		for (TableRowDTO tableRowDTO : rows) {
			System.out.println(tableRowDTO.getRowNumber());
			
			List<TableColumn> columnList = tableRowDTO.getColumns();
			for (TableColumn col : columnList) {
				System.out.print(" | " + col.getColumnName() + ": " + col.getVal());
			}
			System.out.println();
		}
		
	}
	
	//remove colNames just use values
	void createPlainList(List<TableRowDTO> rows){
		List<List<Object>> plainRowList = new ArrayList<List<Object>>();
		
		for (TableRowDTO tableRowDTO : rows) {
			
			List<Object> plainRow = new ArrayList<Object>();
			plainRow.add(tableRowDTO.getRowNumber());
			
			List<TableColumn> columnList = tableRowDTO.getColumns();
			for (TableColumn col : columnList) {
				plainRow.add(col.getVal());
			}
			plainRowList.add(plainRow);
		}
	
		printPlainRowList(plainRowList);
		
	}
	
	
	/*
	 *  0 Ana 47 AA_street
 		1 Pier 22 BB_street
 		2 Eve 48 CC_street
	 */
	private void printPlainRowList(List<List<Object>> plainRowList) {
		System.out.println("***** rows by values, no col names");
		for (List<Object> list : plainRowList) {
			for (Object object : list) {
				System.out.print(" | "+ object.toString());
			}
			System.out.println();
		}
		
	}

	public static void main(String[] args) {
		TableRowDTO t = new TableRowDTO();
		t.populateandTest();
	}
	
	
}
