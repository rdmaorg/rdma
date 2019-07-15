package ie.clients.gdma2.util;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.util.StringUtils;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.ui.Filter;

public class SQLUtil {

	private static final Logger logger = LoggerFactory.getLogger(SQLUtil.class);

	/*
		Selects DISPLAYABLE columns for table
		
		URL: https://localhost/gdma2/rest/column/data/read/table/133?order[0][column]=629 
	
	creates SELECT colum_list from TABLE 
		<where clause using filters > 
		 <order by clause>
	
		SELECT customers.country, customers.city, customers.contactFirstName, customers.postalCode, customers.salesRepEmployeeNumber,
				customers.customerNumber, customers.customerName, customers.phone, customers.addressLine1, customers.creditLimit, customers.contactLastName, 
				customers.addressLine2, customers.state 
				FROM customers 
				ORDER BY customers.city asc
	
	 */
	public static String createSelect(Server server, Table table, Column sortColumn, String dir, List<Filter> filters, Long recordCount) {
		StringBuilder stringBuilder = new StringBuilder(table.getColumns().size() * 20);
		String tableName = table.getName();
		boolean addComma = false;

		stringBuilder.append("SELECT TOP " + recordCount + " ");

		///// Farrukh Changes - START /////
		// Sort the columns of a table by orderBy field, so that the display becomes sane.
		List<Column> columns = new ArrayList<>(table.getColumns());
		Collections.sort(columns, (c1, c2) -> {
			return c1.getSortOrder() - c2.getSortOrder();
		} );
		///// Farrukh Changes - END   /////
		
		for (Column column : columns) {
			if (column.isDisplayed()) {
				if (addComma) {
					stringBuilder.append(", ");
				} else {
					addComma = true;
				}
				stringBuilder.append(tableName);
				stringBuilder.append('.');
				stringBuilder.append(column.getName());
			}
		}

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		String whereClause = createFilterWhereClause(table, filters);
		if (whereClause.length() > 0) {
			stringBuilder.append(" WHERE ");
			stringBuilder.append(whereClause);
		}

		stringBuilder.append(createOrderbyClause(table, sortColumn, dir));

		return stringBuilder.toString();
	}
	
	/*Get the number of records in the table so we can use TOP <record count> in the select to retrieve the data from the table
	 * It was not possible to retrieve the data from tables with CLUSTERED COLUMNSTORE INDEXes without using TOP*/
	public static String createSelectCount(Server server, Table table) {
		StringBuilder stringBuilder = new StringBuilder();
		String tableName = table.getName();
		
		stringBuilder.append("SELECT COUNT(*) ");

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		
		return stringBuilder.toString();
	}

	/* input : 2 columns (PK of columns) coming from UI
	 * select Customers.storeColumn, Customers.displayColumn from prefix.Customer order by Customers.displayColumn asc
	 * 
	 * Select ALL values from config table on Remote server,  for DD and DS columns:
	 * 
	 * SQL Structure: SELECT + ORDER BY
	 * SELECT table.storeColumn, table.displayColumn from serverPrefix.table order by table.displayColumn asc
	 * 
	 * SELECT br_transaction.REQUEST_DATE, br_transaction.STATUS FROM ADPR_TEST.br_transaction ORDER BY br_transaction.STATUS asc;
	 * */
	public static String createDropDownSelect(Server server, Table table, Column display, Column store) {

		StringBuilder stringBuilder = new StringBuilder(60);
		String tableName = table.getName();

		stringBuilder.append("SELECT ");

		stringBuilder.append(tableName);
		stringBuilder.append('.');
		stringBuilder.append(store.getName());
		stringBuilder.append(", ");
		stringBuilder.append(tableName);
		stringBuilder.append('.');
		stringBuilder.append(display.getName());

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		stringBuilder.append(createOrderbyClause(table, display, "asc"));

		return stringBuilder.toString();
	}

	public static String createOrderbyClause(Table table, Column sortColumn, String dir) {
		if (sortColumn == null) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" ORDER BY ");
		stringBuilder.append(table.getName());
		stringBuilder.append('.');
		stringBuilder.append(sortColumn.getName());
		stringBuilder.append(' ');
		stringBuilder.append(dir);

		return stringBuilder.toString();
	}

	public static String createFilterWhereClause(Table table, List<Filter> filters) {
		StringBuilder stringBuilder = new StringBuilder();

		boolean addAnd = false;

		boolean addOrColumn = false;

		if (filters != null) {
			for (Filter filter : filters) {
				boolean addOr = false;
				if (filter.isValid()) {
					logger.error("filter = "  + filter.toString() );
					if(addOrColumn)
					{						
						stringBuilder.append(" OR ");
						addOrColumn = false;
					}
					else
					{
						if (addAnd) {
							stringBuilder.append(" AND ");
						} else {
							addAnd = true;
						}
					}
					stringBuilder.append(" (");

					//if (filter.isNullValue()) {
					if (filter.getFilterOperator() == 8) {
						stringBuilder.append(table.getName());
						stringBuilder.append('.');
						stringBuilder.append(filter.getColumnName());
						stringBuilder.append(" IS NULL ");
						addOr = true;
					}

					//if (filter.isBlank()) {
					if (filter.getFilterOperator() == 9) {
						stringBuilder.append(table.getName());
						stringBuilder.append('.');
						stringBuilder.append(filter.getColumnName());
						stringBuilder.append(" = '' ");
					}

					if (StringUtils.hasText(filter.getFilterValue())) {
						if (addOr) {
							stringBuilder.append(" OR ");
						} else {
							addOr = true;
						}
						//deal with NOT queries, i.e. if the Not checkbox is ticked
						if (filter.isNotValue())
						{
							stringBuilder.append(" NOT ");
						}


						//Author: Farrukh Mirza
						//The syntax for Type Casting and Concatenation are different for each database.
						//Therefore, they are fetched from ConnectionType configuration.
						//This method is only applied for Like FilterOperation
						//For all other operation like =, <, >, <=, >= absolute table name is used only.
						if(filter.getFilterOperator() == 0 || filter.getFilterOperator() == 1 || filter.getFilterOperator() == 2 || filter.getFilterOperator() == 3 || filter.getFilterOperator() == 4){
							stringBuilder.append(table.getName());
							stringBuilder.append('.');
							stringBuilder.append(filter.getColumnName());
						}	else {
							stringBuilder.append(MessageFormat.format(table.getServer().getConnectionType().getSqlCastColumnForSearch(), table.getName()+'.'+filter.getColumnName()));
						}

						//////// OLD CODE - Start //////
						//Commenting Author: Farrukh Mirza
//						stringBuilder.append(table.getName());
//						stringBuilder.append('.');
//						stringBuilder.append(filter.getColumnName());
						//////// OLD CODE - End //////

						switch (filter.getFilterOperator()) {
						case 0:  stringBuilder.append(" = ?"); break; 
						case 1:  stringBuilder.append(" < ?"); break; 
						case 2:  stringBuilder.append(" <= ?"); break; 
						case 3:  stringBuilder.append(" > ?"); break; 
						case 4:  stringBuilder.append(" >= ?"); break; 
						case 5:  stringBuilder.append(" LIKE ?"); break; 
						case 6:  stringBuilder.append(" LIKE ?"); break; 
						case 7:  stringBuilder.append(" LIKE ?"); break;			            	
						}

					}
					if(filter.isOrValue())
					{
						addOrColumn = true;
					}

					stringBuilder.append(" )");

				}
			}
		}
		return stringBuilder.toString();
	}

	public static String createCount(Server server, Table table, List<Filter> filters) {
		StringBuilder stringBuilder = new StringBuilder(100);
		String tableName = table.getName();
		boolean addComma = false;

		stringBuilder.append("SELECT COUNT_BIG(1) ");

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		String whereClause = createFilterWhereClause(table, filters);
		if (whereClause.length() > 0) {
			stringBuilder.append(" WHERE ");
			stringBuilder.append(whereClause);
		}

		return stringBuilder.toString();
	}

	public static String createInsertStatement(Server server, Table table, List<Column> columns) {

		StringBuilder sbInsert = new StringBuilder(columns.size() * 30);
		sbInsert.append("INSERT INTO ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbInsert.append(server.getPrefix());
			sbInsert.append('.');
		}
		sbInsert.append(table.getName());
		
		StringJoiner columnNamesJoiner = new StringJoiner(","," ( "," )");
		StringJoiner columnValuesJoiner = new StringJoiner(","," VALUES ( "," )");
		for (Column column : columns) {
			columnNamesJoiner.add(column.getName());
			columnValuesJoiner.add("?");
		}
		sbInsert.append(columnNamesJoiner.toString());
		sbInsert.append(columnValuesJoiner.toString());
		return sbInsert.toString();
	}

	/*UPDATE FOR TABLE: 
	 * new_table_test_autoincrement (
  			id int(11) NOT NULL AUTO_INCREMENT,
  			name varchar(45) DEFAULT NULL,
  			year varchar(45) DEFAULT NULL,
  		PRIMARY KEY (id)
  	* 
  	* Update SQL query: 
	 * 	UPDATE new_table_test_autoincrement SET name = ?, year = ? WHERE  (id = ?) 
	 */
	public static String createUpdateStatement(Server server, Table table, List<Column> columns) {
		StringBuilder sbUpdate = new StringBuilder(columns.size() * 30);
		sbUpdate.append("UPDATE ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbUpdate.append(server.getPrefix());
			sbUpdate.append('.');
		}

		sbUpdate.append(table.getName());
		sbUpdate.append(" SET ");
		boolean needComma = false;
		for (Column column : columns) {
			if (!column.isPrimarykey()) {
				if (needComma) {
					sbUpdate.append(", ");
				} else {
					needComma = true;
				}
				sbUpdate.append(column.getName());
				sbUpdate.append(" = ?");
			}
		}

		sbUpdate.append(createWhereClause(server, table, columns));

		return sbUpdate.toString();
	}

	public static String createDeleteStatement(Server server, Table table, List<Column> columns) {
		StringBuilder sbDelete = new StringBuilder(columns.size() * 15);
		sbDelete.append("DELETE FROM ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbDelete.append(server.getPrefix());
			sbDelete.append('.');
		}

		sbDelete.append(table.getName());

		sbDelete.append(createWhereClause(server, table, columns));

		return sbDelete.toString();
	}

	public static String createWhereClause(Server server, Table table, List<Column> columns) {
		StringBuilder sbWhere = new StringBuilder(columns.size() * 15);
		sbWhere.append(" WHERE ");

		boolean needAnd = false;
		for (Column column : columns) {
			if (column.isPrimarykey()) {
				if (needAnd) {
					sbWhere.append(" AND ");
				} else {
					needAnd = true;
				}
				sbWhere.append(" (");
				sbWhere.append(column.getName());
				sbWhere.append(" = ?");
				sbWhere.append(") ");
			}
		}
		return sbWhere.toString();
	}

	public static Object convertToType(String data, int sqlDataType) {
		return convertToType(data, sqlDataType, null);
	}
	/**
	 * ColumnDataUpdate uses : String newColumnValue
	 * before preparedStatement is executed, this String value needs to be converted into proper type to match DB type, 
	 * for this purpose metadata: Column.getColumnType() is used
	 * 
	 * returns Object of proper data type: String, Integer, Long...Date...Time
	 * @param data
	 * @param sqlDataType
	 * @return
	 */
	public static Object convertToType(String data, int sqlDataType, Column column) {
			
		logger.info("SQLUtil.convertToType data in: **" + data + "**");
		
		Object oReturn = null;
		
		if (StringUtils.hasText(data)) {

			switch (sqlDataType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				oReturn = data==""?"":data;
				break;
			case Types.TINYINT:
			case Types.INTEGER:
				try {
					if (StringUtils.hasText(data)) {
						oReturn = Integer.parseInt(data);
					} else {
						return null;
					}
				} catch (Exception e) {
				    throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as an Integer");
				}
				break;
			case Types.BIT:
				try {
					if (StringUtils.hasText(data)) {
						oReturn = Integer.parseInt(data);
					} else {
						return null;
					}
				} catch (Exception e) {
					logger.error("Value [" + data + "] is BIT and could not be parsed as an integer. trying to parse to boolean");
					try {
						oReturn = Boolean.parseBoolean(data);	
					} catch (Exception e2) {
						throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as an boolean");
					}
				}
				break;
			case Types.SMALLINT:
				try {
					if (StringUtils.hasText(data)) {
						oReturn = Integer.parseInt(data);
					} else {
						return null;
					}
				} catch (Exception e) {
					throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as an integer");
				}
				break;
			case Types.BIGINT:
				try {
					if (StringUtils.hasText(data)) {
						oReturn = new Long(data);
					} else {
						return null;
					}
				} catch (Exception e) {
					throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as an integer");
				}
				break;
			case Types.DECIMAL:
			case Types.FLOAT:
			case Types.NUMERIC:
			case Types.REAL:
			case Types.DOUBLE:
				try {
					if (StringUtils.hasText(data)) {
						oReturn = new BigDecimal(data);
					} else {
						return null;
					}
				} catch (Exception e) {
					throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as a number");
				}
				break;
			case Types.DATE:
			//case Types.TIMESTAMP:
				try {
					if (StringUtils.hasText(data)) {
						if (column != null && column.getColumnTypeString().equalsIgnoreCase("YEAR")){
							oReturn = new Integer(data);
						} else {
							oReturn = Formatter.parseDate(data);
						}
					} else {
						return null;
					}
				} catch (Exception e) {
					if(column != null){
						throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as " + column.getColumnTypeString());
					} else {
						throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as a date. ");
					}
				}
				break;
			case Types.TIMESTAMP:
			case -155:
				try {
					if (StringUtils.hasText(data)) {
						try {
							oReturn = Formatter.parseDateTime(data);//chech wheater to return String or Date?
						} catch (Exception e) {
							oReturn = Formatter.parseTimeStamp(data);
						}
					} else {
						return null;
					}
				} catch (Exception e) {
					throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as a timestamp. ");
				}
				break;	
			case Types.TIME:
				try {
					if (StringUtils.hasText(data)) {
						Formatter.parseTime(data);
						oReturn = data;  // If no exception thrown from above then we have a valid time format.
					} else {
						return null;
					}
				} catch (Exception e) {
					throw new TypeMismatchDataAccessException("Value [" + data + "] could not be parsed as a time. ");
				}
				break;
			default:
				logger.error("Unknow datatype[" + sqlDataType + "] - using String");
				oReturn = data;
				break;
			}

		}else{//deal with empty strings(not nulls) being passed in not nullable character fields
			
			oReturn = data;
			switch (sqlDataType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
			case Types.NVARCHAR:
			case Types.NCHAR:
				oReturn = data;
				break;
			default:
				oReturn = null;
				break;
			}
		}
		
		logger.info("SQLUtil.convertToType oReturn in: **" + oReturn + "**");
		
		return oReturn;
	}

	public static boolean isNumeric(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
		case Types.NUMERIC:
		case Types.TINYINT:
		case Types.INTEGER:
		case Types.BIT:
		case Types.SMALLINT:
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.FLOAT:
		case Types.REAL:
		case Types.DOUBLE:
			blnReturn = true;
			break;
		default:
			blnReturn = false;
			break;
		}
		return blnReturn;
	}

	public static boolean isText(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
		case Types.CHAR:
		case Types.NCHAR:
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
			blnReturn = true;
			break;
		default:
			blnReturn = false;
			break;
		}

		return blnReturn;

	}

	public static boolean isDate(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
		case Types.DATE:
		//case Types.TIMESTAMP:
			blnReturn = true;
			break;
		default:
			blnReturn = false;
			break;
		}

		return blnReturn;

	}
	
	public static boolean isDateTime(int sqlDataType) {
		boolean blnReturn = false;

			switch (sqlDataType) {
			case Types.TIMESTAMP:
				blnReturn = true;
				break;
			default:
				blnReturn = false;
				break;
			}

			return blnReturn;

		}
		

	public static boolean isTime(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
		case Types.TIME:
			blnReturn = true;
			break;
		default:
			blnReturn = false;
			break;
		}

		return blnReturn;

	}	
	public static boolean isNumeric(String sqlDataType) {
		boolean blnReturn = false;

		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = "NUMERIC".equals(sqlDataType) || "TINYINT".equals(sqlDataType) || "INTEGER".equals(sqlDataType) || "BIT".equals(sqlDataType)
					|| "SMALLINT".equals(sqlDataType) || "BIGINT".equals(sqlDataType) || "DECIMAL".equals(sqlDataType) || "FLOAT".equals(sqlDataType)
					|| "REAL".equals(sqlDataType) || "DOUBLE".equals(sqlDataType);
		}
		return blnReturn;
	}

	public static boolean isText(String sqlDataType) {
		boolean blnReturn = false;
		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = sqlDataType.toUpperCase().startsWith("CHAR") || sqlDataType.toUpperCase().startsWith("NCHAR") || sqlDataType.toUpperCase().startsWith("VARCHAR") || sqlDataType.toUpperCase().startsWith("NVARCHAR") || sqlDataType.toUpperCase().startsWith("LONGVARCHAR") ;
		}
		return blnReturn;
	}

	public static boolean isDate(String sqlDataType) {
		sqlDataType = sqlDataType.toUpperCase();
		boolean blnReturn = false;

		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = "DATE".equals(sqlDataType) || "DATETIME".equals(sqlDataType) || "TIME".equals(sqlDataType) || "TIMESTAMP".equals(sqlDataType) || "DATETIME2".equals(sqlDataType) || "DATETIMEOFFSET".equals(sqlDataType);
		}
		return blnReturn;
	}


	//GDMA2 adding few util methods 


	/**
	 * EXAMPLE: 	
	 * 	SELECT * from dbo.tableFoo where 1 = 0;
	 * @param server
	 * @param tableName
	 * @return
	 */
	public static String createSelectColumnMetadata(Server server, String tableName){
		logger.info("createSelectColumnMetadata");
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select * from ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}
		stringBuilder.append(tableName);
		stringBuilder.append("  where 1 = 0");

		logger.info("SQL Select query: " + stringBuilder.toString());
		return stringBuilder.toString();
	}

	



}
