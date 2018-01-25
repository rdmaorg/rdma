package ie.clients.gdma2.domain.ui;



/**
 * Filter for all TEXT columns in Data Module incremental Search field <BR>
 * 
 * PREDEFINED Filter values :
 * 		 Operator: 	6
 * 		 Operator text: Contains
 * 		 OR	: True
 *       NULL: false
 * 
 * DYNAMIC values to be set from metadata:
 * 		filter value	(searchTerm.trim() - from UI request, param 	search[value]: "search_value_here" )
 *      columnId
 *      columnName
 *      columnType (MUST BE SQLUtil.isText() - 
 *      					case Types.CHAR:		1
							case Types.VARCHAR:		12
							case Types.LONGVARCHAR:	-1	
							)
 * 
 * @author Avnet
 *
 */
public class IncrementalMatchTypeSearchFilter extends Filter{
	
	public IncrementalMatchTypeSearchFilter(String searchTerm, Integer colId, String colName, int colType) {
		super();
		
		setFilterOperator(OPERATOR_EQUAL_TO); //0
		setFilterOperatorText(OPERATOR_EQUAL_TO_TEXT); //Equal To
		setOrValue(true);
		setNotValue(false);
		
		setFilterValue(searchTerm.trim());
		setColumnId(colId);
		setColumnName(colName);
		setColumnType(colType);
		
	}

	
}
