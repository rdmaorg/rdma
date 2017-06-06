package ie.clients.gdma2.domain.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  DB
 * 
 * SQL: 	SELECT * FROM classicmodels.customers;  //tableID = 133
 * 
 * SQL:		SELECT id, active, name, column_type_str FROM column_gdma2 where table_id = 133;      	
 * 

			id	active	name			column_type_str		column_type
			----------------------------------------------
			630	true	contactFirstName		VARCHAR	12
			631	true	postalCode				VARCHAR	12
			632	true	salesRepEmployeeNumber	INT	4
			633	true	customerNumber			INT	4
			634	true	customerName			VARCHAR	12
			635	true	phone					VARCHAR	12
			637	true	creditLimit				DOUBLE	8
			638	true	contactLastName			VARCHAR	12
			640	true	state					VARCHAR	12
			639	true	addressLine2			VARCHAR	12
			628	true	country					VARCHAR	12
			629	true	city					VARCHAR	12
			636	true	addressLine1			VARCHAR	12
			2252	true	gender_id			INT	4
			2251	true	membership_id		INT	4

--------------------------------------------------------------------------------------

GDMA1

	http://10.252.222.120:8080/GDMA/js/dwr/call/plaincall/GdmaAjax.getData.dwr
	FILTER info on submit:

					c0-e8=Object_Object:{
					columnId:reference:c0-e9, 			//c0-e9=number:1774
					columnName:reference:c0-e10,		//c0-e10=string:Body
					columnType:reference:c0-e11,		//c0-e11=number:12
					filterValue:reference:c0-e12, 		//c0-e12=string:ABBA
					filterOperator:reference:c0-e13, 	//c0-e13=string:6
					filterOperatorText:reference:c0-e14,	//c0-e14=string:Contains
					orValue:reference:c0-e15, 				//c0-e15=boolean:false
					notValue:reference:c0-e16}				//c0-e16=boolean:false

------------------------------------------------------

UI

  <select id="selOperator" style="width: 200px;">
	<option value="-1">Please Select</option>
	<option value="0">Equal To</option>
	<option value="1">Less Than</option>
	<option value="2">Less Than Or Equal To</option>
	<option value="3">Greater Than</option>
	<option value="4">Greater Than Or Equal To</option>
	<option value="5">Begins With</option>
	<option value="6">Contains</option>
	<option value="7">Ends With</option>
	<option value="8">Is Null</option>
	<option value="9">Is Blank</option>
</select>



-----------------------
 * @author Avnet
 *
 */

public class FilterTextColumnsDummy {

	private static final Logger logger = LoggerFactory.getLogger(FilterTextColumnsDummy.class);

	/*dummy filters to be tested from DataTableResource when calling getTableData()*/
	public static List<Filter> createdummyFilters(String searchTerm){
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(createFilterColumn_contactFirstName(searchTerm));
		filters.add(createFilterColumn_city(searchTerm));

		//print filters
		for (Filter f : filters) {
			printFilter(f);
		}

		return filters;
	}

	private static Filter createFilterColumn_city(String searchTerm) {
		Filter filter = new Filter();

		//metadata
		filter.setColumnId(629); //TODO provide columnID from local DB
		filter.setColumnName("city"); 
		filter.setColumnType(12);//INT

		//filters
		filter.setFilterValue(searchTerm); //VALUE coming from UI - SEARCH TERM (must be the same across all filters)
		filter.setFilterOperator(6); // <option value="6">Contains</option>
		filter.setFilterOperatorText("Contains");

		//NOT, OR
		//orValue:reference:c0-e15, 				//	false			private boolean orValue = false; 
		//notValue:reference:c0-e16}				//	false			private boolean notValue = false; 
		filter.setNotValue(false);
		filter.setOrValue(true);	//SET OR ON EACH FILTER

		return filter;
	}

	private static Filter createFilterColumn_contactFirstName(String searchTerm) {

		Filter filter = new Filter();

		//metadata
		filter.setColumnId(634); //TODO provide columnID from local DB
		filter.setColumnName("contactFirstName"); 
		filter.setColumnType(12);//INT

		//filters
		filter.setFilterValue(searchTerm); ////VALUE coming from UI - SEARCH TERM (must be the same across all filters)
		filter.setFilterOperator(6);  //<option value="6">Contains</option>
		filter.setFilterOperatorText("Contains");

		//NOT, OR
		//orValue:reference:c0-e15, 				//	false			private boolean orValue = false; 
		//notValue:reference:c0-e16}				//	false			private boolean notValue = false; 
		filter.setNotValue(false);
		filter.setOrValue(true);	////SET OR ON EACH FILTER

		return filter;

	}

	private static  void printFilter(Filter f){

		
		logger.info("colID: " + f.getColumnId());
		logger.info("colType:" + f.getColumnType());
		logger.info("colName: " + f.getColumnName());

		logger.info("FilterOperator: " + f.getFilterOperator());
		logger.info("FilterValue: " + f.getFilterValue());
		logger.info("filterOperatorText: " + f.getfilterOperatorText());

		logger.info("isNull: " + f.isNullValue());
		logger.info("isBlank :" + f.isBlank());

		logger.info("isNotValue: " + f.isNotValue());
		logger.info("isOrValue: "  + f.isOrValue());
		logger.info("isValid(): " + f.isValid());


	}


	public static void main(String[] args) {
		
		String searchTerm = "an"; 
		
		logger.info("Searching: " + searchTerm);
		logger.info("Single filter value and criteria is used accross all TEXT columns - CONTAINS" + searchTerm);
		logger.info("SELECT *  FROM customers WHERE "
				+ " (customers.contactFirstName LIKE ? ) OR  (customers.city LIKE ? )"
				 );

		
		logger.info("this filters are te be used only on columns of TEXT type and this means \n"
				+ "java.sql.Types.CHAR	= 1 \n"
				+ "java.sql.Types.VARCHAR = 12 \n"
				+ "java.sql.Types.LONGVARCHAR = -1 \n"
				+ ""
				+ "and those values are mapped to column_gdma2.column_type in GDMA2 metadata DB"
				);
		
		
		new FilterTextColumnsDummy().createdummyFilters(searchTerm);
	}

}
