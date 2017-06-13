package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.ui.DropDownColumn;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* READ data and include proper dropdown values:
 * 
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
	               "columnName":"postalCode",
			...
			   ],
	   "draw":0,
 */   

public class CsvDownloader {

	private static final Logger logger = LoggerFactory.getLogger(CsvDownloader.class);

	private static final String CONTENT_TYPE = "text/csv";
	private final String EXTENSION = ".csv";

	public static String createCSV(List<String> headers, List<TableRowDTO> rows) {

		//create header first
		StringBuilder sbTemp = new StringBuilder();
		if (rows != null && rows.size() > 0) {
			int colIdx = 0;
			boolean addComma = false;
			for (String column : headers) {
				if (addComma) {
					sbTemp.append(',');
				} else {
					addComma = true;
				}
				sbTemp.append(column);
			}
			sbTemp.append("\r\n");

			//create body
			for (TableRowDTO tableRowDTO : rows) {
				logger.info("row number:" + tableRowDTO.getRowNumber());

				addComma = false;
				//If the column value contains a comma(s), then surround the value with double quotes so the comma(s) are escaped 
				//to ensure that the resultant CSV file will not be 'skewed'.
				for (int j = 0; j <tableRowDTO.getColumns().size(); j++){
					if (addComma) {
						sbTemp.append(',');
					} else {
						addComma = true;
					}


					logger.info(tableRowDTO.getColumns().get(j).getColumnName());
					String colValue = tableRowDTO.getColumns().get(j).getVal() == null ? " " : getValue(tableRowDTO.getColumns().get(j).getVal());
					if (colValue.indexOf(",") != -1) {
						colValue = colValue.replaceAll("\"", "\"\"");
						sbTemp.append("\"");
						sbTemp.append(colValue);
						sbTemp.append("\"");
					} else {
						sbTemp.append(colValue);
					}

				}//for j	

				addComma = false;
				sbTemp.append("\r\n");
			}	//for rows
		}  else {
			sbTemp.append("No records found\r\n");
		}

		return sbTemp.toString();
	}	


	private static String getValue(Object object) {
		if(isDropDown(object)){
			return getDropDownValue((DropDownColumn)object);
		};
		if (object instanceof Timestamp || object instanceof java.sql.Date || object instanceof Date) {
			return getDateValue(object);
		} else {
			return object == null ? "" : object.toString();
		}
	}

	private static boolean isDropDown(Object object){
		logger.info("isDropDown");
		return (object instanceof ie.clients.gdma2.domain.ui.DropDownColumn);
	}
	
	
	
	/* dropdown format : col1 = row number starting at 0, DD Store column, DD Display Column 
	 * 							[0, 103, gold], 
	 * 							[1, 101, ordinary],
	 * 							[2, 102, silver] 
	 *  based on: 
	 *  	SELECT table.storeColumn, table.displayColumn from serverPrefix.table order by table.displayColumn asc
	 *  
	 *  to determine value - mapping in reverse DD store key to the memeber of List containing that key and extract value:  
	 *  
	 *  	 
	 * EXAMPLE DROPDOWN FORMAT
		 
		"val":{  
        "value":"101",			//MAP THIS value (if empty return ' ')
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
	              101,				//TO THIS value and 
                  "ordinary"		//AND EXTRACT value 'ordinary'
           ],
           [  
              2,
              102,
              "silver"
           ]
        ]
     }
	 */


	private static String getDropDownValue(DropDownColumn dropDown){
		logger.info("getDropDownValue");
		
		String value = dropDown.getValue(); //value : 101 
		//if dropdown value not set return ' '
		if(value == null || StringUtils.isBlank(value)){
			return " ";
		}

		String valueLocated = null;
		boolean isLocated = false;
		
		for (Object ddLists : dropDown.getDropdownOptions()) {
			List ddList = (List) ddLists;
			if (!isLocated){
				for (int i = 0; i < ddList.size(); i++) {
					Object rowNumber = ddList.get(0);
					Object store = ddList.get(1);
					Object val = ddList.get(2);

					if(value.equalsIgnoreCase(store.toString())){
						logger.info("MATCH!");
						valueLocated = val.toString();
						isLocated = true;
					}
				}//for inner
			}

		}//for outter
		logger.info("value:" + valueLocated);
		return valueLocated;

	}

	


	private static String getDateValue(Object object) {
		Date date;
		if (object instanceof Timestamp) {
			date = new Date(((Timestamp) object).getTime());
		} else if (object instanceof java.sql.Date) {
			date = new Date(((java.sql.Date) object).getTime());
		} else {
			date = (Date) object;
		}
		return Formatter.formatDate(date);
	}

}
