package ie.clients.gdma2.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * To be used by DynamicDAOImpl before returning Table Data response
 * 
 * methods:
 *   getTableData()	
 * 	 getTableDataWithColumnNamesAndDropdowns() 
 *   getTableDataWithColumnMetadata()
 *   
 *   ...while calling jdbcTemplate to execute dynamic SQLs for data fetch,
 *    this methods can use  RowMapper(used in GDMA 1 app) and return just plain values, or
 *    or new TableDataRowMapper can be used and return data in format that wrapps every table cell with {"k":"v"})
 *  
 *  a)
 *   
 *   List records =  (List)jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
				startIndex, length));
 *  
 *  RESULT:
 *  
		 *  	{  
		   "data":[  
		      [  
		         1,
		         "France",
		         "Nantes",
		         "Carine ",
		         "44000",
		         1370,
		         103,
		         "Atelier graphique",
		         "40.32.2555",
		         "54, rue Royale",
		         21000,
		         null,
		         "Schmitt",
		         null
		      ],
		      [  
		         2,
		         "USA",
		         "Las Vegas",
 *  
 *  b) 
 *   
 *   List<TableRowDTO> rows =(List<TableRowDTO>) jdbcTemplate.query(psc.newPreparedStatementCreator(params), 
				new PagedResultSetExtractor(new TableDataRowMapper(),startIndex,length));
 *  
 *  RESULT:
 *  
 *  {  
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
 *   
 *   -----------------
 *   
 *   
 *    
 *   
 *  
 * @author Avnet
 *This helper class can be used to  POSTPROCESS Results created with b) approach if data grid API on UI demands it
 */
public class TableDataFormatUtil {

	
	/** 
	 *  
	 * 
	 *  {  
   "data":[  
      [  
         1,
         "France",
         "Nantes",
         "Carine ",
         "44000",
         1370,
         103,
         "Atelier graphique",
         "40.32.2555",
         21000,
         "Schmitt",
         null,
         {  
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
         },
      ],
      [  
         2,
         "USA",
         "Las Vegas",
         "Jean",
         "83030",
         1166,
         ...
         
	 *  remove column names from TableRowDTO (see example b) above) and just return data in format
	 *  To use it simply add call to this method as latest line in e.g.	DynamicDAOImpl.getTableData() method and replace
		  	....
	  			return rows; }
	  	with
	  			return TableDataFormatUtil.createListWithMap(rows);
	
	  	*/
	public static List<List<Object>> createPlainList(List<TableRowDTO> rows){
		List<List<Object>> plainRowList = new ArrayList<List<Object>>();
		
		for (TableRowDTO tableRowDTO : rows) {
			
			List<Object> plainRow = new ArrayList<Object>();
			plainRow.add(tableRowDTO.getRowNumber());
			
			tableRowDTO.getColumns().forEach((k,v)->{
				plainRow.add(v);				
			});
			plainRowList.add(plainRow);
		}
	
		return plainRowList;
		
	}
	
	
	
	
	/**
	 * FORMAT:
	 *
	 * {  
   	"data":[  
      {  
         "rowNumber":1,
         "country":"France",
         "city":"Nantes",
         "contactFirstName":"Carine ",
         "postalCode":"44000",
         "salesRepEmployeeNumber":1370,
         "customerNumber":103,
         "customerName":"Atelier graphique",
         "phone":"40.32.2555",
         "creditLimit":21000,
         "contactLastName":"Schmitt",
         "addressLine2": "blah",
         "state":null,
         "membership_id":{  
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
                  "silver",
               
         "gender_id":{  
            "value":"",
            "did":2257,
            "sid":2256,
            "dropdownOptions":[  
               [  
                  0,
                  23,
                  "Female"
               ],
               [  
                  1,
                  22,
                  "Male"
               ],
               [  
                  2,
                  25,
                  "Not Aplicable"
               ],
               [  
                  3,
                  24,
                  "Unkown"
               ]
            ]
         }
      },
      {  
         "rowNumber":2,
         "country":"USA",
         "city":"Las Vegas",
         "contactFirstName":"Jean",
       ...
	 * 
	 * @param rows
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> createListWithMap(List<TableRowDTO> rows){
		List<LinkedHashMap<String, Object>> plainRowList = new ArrayList<LinkedHashMap<String,Object>>();
		
		for (TableRowDTO tableRowDTO : rows) {
			
			//List<Object> plainRow = new ArrayList<Object>();
			LinkedHashMap<String, Object> plainRow= new LinkedHashMap<String,Object>();
			plainRow.put("rowNumber",tableRowDTO.getRowNumber());
			
			plainRow.putAll(tableRowDTO.getColumns());
			plainRowList.add(plainRow);
		}
	
		return plainRowList;
		
	}
	
}
