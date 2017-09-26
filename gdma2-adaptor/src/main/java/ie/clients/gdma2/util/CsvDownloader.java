package ie.clients.gdma2.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.clients.gdma2.domain.ui.DropDownColumn;

/* READ data and include proper dropdown values:
 */   
public class CsvDownloader {

	private static final String EOL = "\r\n";

	private static final String COLUMN_VALUE_DELIMITER = ",";

	private static final String COLUMN_VALUE_PRE_SUFFIX = "\"";

	private static final Logger logger = LoggerFactory.getLogger(CsvDownloader.class);

	public static String createCSV(List<String> headers, List<TableRowDTO> rows) {

		//create header first
		StringBuilder sbTemp = new StringBuilder();
		if (rows != null && rows.size() > 0) {
			boolean addComma = false;
			for (String column : headers) {
				if (addComma) {
					sbTemp.append(',');
				} else {
					addComma = true;
				}
				sbTemp.append(column);
			}
			sbTemp.append(EOL);

			//create rows
			for (TableRowDTO tableRowDTO : rows) {
				StringJoiner rowDataJoiner = new StringJoiner(COLUMN_VALUE_DELIMITER);
				logger.info("row number:" + tableRowDTO.getRowNumber());
				tableRowDTO.getColumns().forEach((k,v)->{
					logger.info(k);
					String colValue = v == null ? " " : getValue(v);
					//If the column value contains a comma(s), then surround the value with double quotes so the comma(s) are escaped 
					//to ensure that the resultant CSV file will not be 'skewed'.
					if (colValue.indexOf(COLUMN_VALUE_DELIMITER) != -1) {
						StringBuilder columnValueBuilder = new StringBuilder("");
						columnValueBuilder.append(COLUMN_VALUE_PRE_SUFFIX);
						columnValueBuilder.append(colValue.replaceAll(COLUMN_VALUE_PRE_SUFFIX, "\"\""));
						columnValueBuilder.append(COLUMN_VALUE_PRE_SUFFIX);
						colValue = columnValueBuilder.toString(); 
					} 
					rowDataJoiner.add(colValue);
				});
				sbTemp.append(rowDataJoiner);
				sbTemp.append(EOL);
			}
		}  else {
			sbTemp.append("No records found\r\n");
		}
		return sbTemp.toString();
	}	

	private static String getValue(Object object) {
		if(isDropDown(object)){
			return getDropDownValue((DropDownColumn)object);
		};
		if (object instanceof Date) {
			return getDateValue(object);
		} else {
			return object == null ? "" : object.toString();
		}
	}

	private static boolean isDropDown(Object object){
		logger.info("isDropDown");
		return (object instanceof ie.clients.gdma2.domain.ui.DropDownColumn);
	}
	
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
			return object.toString();
		} else if (object instanceof java.sql.Date) {
			date = new Date(((java.sql.Date) object).getTime());
		} else {
			date = (Date) object;
		}
		return Formatter.formatDate(date);
	}

}
