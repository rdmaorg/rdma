package ie.clients.gdma2.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Avnet
 *
 */
public class TableRowDTO {

	private BigInteger rowNumber;
	private Map<String,Object> columns = new LinkedHashMap<String,Object>();
	
	public BigInteger getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(BigInteger rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	public Map<String,Object> getColumns() {
		return columns;
	}

	public void setColumns(Map<String,Object> columns) {
		this.columns = columns;
	}

	void populateandTest(){
		List<TableRowDTO> rows = new ArrayList<TableRowDTO>();
		
		TableRowDTO row1 = new TableRowDTO();
		row1.setRowNumber(new BigInteger("0"));
		
		Map<String,Object> colsrow1 = new LinkedHashMap<String,Object>();
		
		colsrow1.put("name", new String("Ana"));
		colsrow1.put("age", new Integer(47));
		colsrow1.put("adress", new String("AA_street"));
		
		row1.setColumns(colsrow1);
		
		//row2
		TableRowDTO row2 = new TableRowDTO();
		row2.setRowNumber(new BigInteger("1"));
		
		Map<String,Object> colsrow2 = new LinkedHashMap<String,Object>();
		
		colsrow2.put("name", new String("Pier"));
		colsrow2.put("age", new Integer(22));
		colsrow2.put("adress", new String("BB_street"));
		
		row2.setColumns(colsrow2);
		
		//row3
		
		TableRowDTO row3 = new TableRowDTO();
		row3.setRowNumber(new BigInteger("2"));
		
		Map<String,Object> colsrow3 = new LinkedHashMap<String,Object>();
		
		colsrow3.put("name", new String("Eve"));
		colsrow3.put("age", new Integer(48));
		colsrow3.put("adress", new String("CC_street"));
		
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
			
			Map<String,Object> columnList = tableRowDTO.getColumns();
			System.out.println();
		}
		
	}
	
	//remove colNames just use values
	void createPlainList(List<TableRowDTO> rows){
		List<List<Object>> plainRowList = new ArrayList<List<Object>>();
		
		for (TableRowDTO tableRowDTO : rows) {
			
			List<Object> plainRow = new ArrayList<Object>();
			plainRow.add(tableRowDTO.getRowNumber());
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
