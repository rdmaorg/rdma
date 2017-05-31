package ie.clients.gdma2.domain.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
	GDMA 1
 * a) EXAMPLE FOR LOOKUP column 'ACTIVE' of TABLE 'Mail': 
 * 
 * 	c0-e8=Object_Object:{
      
      				columnId:reference:c0-e9,				// 1775			METADATA
					columnName:reference:c0-e10,			// Active			private String columnName;	METADATA
					columnType:reference:c0-e11,			// 4				private int columnType;	METADATA

					filterValue:reference:c0-e12,			// 0				private String filterValue;
					filterOperator:reference:c0-e13, 		//  0				private int filterOperator;
					filterOperatorText:reference:c0-e14, 	// Equal%20To		private String filterOperatorText;

					orValue:reference:c0-e15, 				//	false			private boolean orValue = false; 
					notValue:reference:c0-e16}				//	false			private boolean notValue = false; 

  a) EXAMPLE for REGULAR column 'Body' of TABLE 'Mail':
  
  	c0-e17=Object_Object:{
          columnId:reference:c0-e18,	//1774		METADATA
		  columnName:reference:c0-e19,	//Body		private String columnName	METADATA
		  columnType:reference:c0-e20,  //12				private int columnType;		METADATA
		
					  
         filterValue:reference:c0-e21, //Hi			private String filterValue	
		 filterOperator:reference:c0-e22, //5		private int filterOperator	
													<option value="5">Begins With</option>
		filterOperatorText:reference:c0-e23,// Begins%20With filterOperatorText
					  
        orValue:reference:c0-e24, 	// boolean:false			private boolean orValue = false;
		 notValue:reference:c0-e25}		// boolean:false			private boolean notValue = false;


   
 * @author Avnet
 *
 */

public class FilterDummy {
	private static final Logger logger = LoggerFactory.getLogger(FilterDummy.class);
	
	/*dummy filters to be tested from DataTableResource when calling getTableData()*/
	public static List<Filter> createdummyFilters(){
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(createFilterColumnCOUNTRY());
		
		//print filters
		for (Filter f : filters) {
			printFilter(f);
		}
		
		return filters;
	}
	
	/* FILTER TO BE CRTEATED: SELECT * FROM classicmodels.customers WHERE Country='Germany'  
	 *
	 * METADATA SQLs
	 * 
	 * 	SQL:	select * from column_gdma2 
				where table_id = (select id from table_gdma2 where name = 'customers' and server_id = 6) and active = true and displayed = true;

		SQL: 	select id, name, column_type from column_gdma2 where id = 628;//SELECTING COLUMN 'country'

				id		name		column_type
				---------------------
				628		country		12
	*/
	private static Filter createFilterColumnCOUNTRY(){
		Filter filter = new Filter();
		
		//metadata
		filter.setColumnId(628); //TODO provide columnID from local DB
		filter.setColumnName("country"); 
		filter.setColumnType(12);
				
		//filters
		filter.setFilterValue("Germany");
		filter.setFilterOperatorText("0");
		filter.setFilterOperatorText("Equal%20To");
		
		//NOT, OR
		//orValue:reference:c0-e15, 				//	false			private boolean orValue = false; 
		//notValue:reference:c0-e16}				//	false			private boolean notValue = false; 
		filter.setNotValue(false);
		filter.setOrValue(false);
	
		
		return filter;
	}
	
	private static  void printFilter(Filter f){
		logger.info("FILTER VALUES");
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
		new FilterDummy().createdummyFilters();
	}
}
