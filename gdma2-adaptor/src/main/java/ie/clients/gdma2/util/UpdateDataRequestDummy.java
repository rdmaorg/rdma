package ie.clients.gdma2.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * - run main method to makes sure all data is structured well
 * 
 *  preconditions - RUN metadata for testing table and columns
 *  then check:  get all Active columns for given Active table on Active server, based on userName :
 * 		http://localhost/gdma2/rest/column/data/table/83

 *  Insert some data into remote table manually and read: 
 *   https://localhost/gdma2/rest/column/data/read/table/83 
 *
 * TEST: add records to table  
 * pick specific table - 
 * PROBLEM 1: determine PK - autoincrement, sequencer or not
 * PROBLEM 2: composite PK (col1, col2)
 * 
 * 	per each type of this table create separate dummy logic
 * 
 * 	a)	https://localhost/gdma2/rest/column/data/add/table/43  NO AUTOINC	
 *  b) 	https://localhost/gdma2/rest/column/data/add/table/83  WITH AUTOINC
 * 
 * @author Avnet
 *
 */
public class UpdateDataRequestDummy{ 

	private static final Logger logger = LoggerFactory.getLogger(DataSourcePool.class);


	/**
		serverId=6
	 * tableId = 43  name 'new_table_test'
	 * 
		 LOCAL METADATA
		 id	active	column_alias
		163	true	year
		164	true	name
		165	true	id

		REMOTE DATA
		ID	NAME	YEAR
		'1', 'abv', '1989'
		'2', 'cdr', '1990'
		'3', 'bfg', '2008'

		NEW REMOTE DATA
		 method not working - try again when fixed!

	 * @param serverId
	 * @param tableId
	 * @return
	 */
	/*/*NO PK autoincrement nor sequencer for 'new_table_test'*/
	public static UpdateDataRequest createDummyAddRecordsForNonAutoIncrementTable_new_table_test(Integer serverId, Integer tableId){
		/*MYSQL table has NO autoincrement- this method will try to insert 2 rows in remote DB and fail!*/

		/*ROW 1*/
		ArrayList<ColumnDataUpdate> row1 = new ArrayList<ColumnDataUpdate>();

		/*column NAME ROW1*/
		ColumnDataUpdate col1Row1 = new ColumnDataUpdate();

		col1Row1.setColumnId(379);
		//col1Row1.setOldColumnValue("abv");
		col1Row1.setNewColumnValue("new data");

		/*column YEAR row1*/
		ColumnDataUpdate col2Row1 = new ColumnDataUpdate();
		col2Row1.setColumnId(378);
		//col2Row1.setOldColumnValue("");
		col2Row1.setNewColumnValue("1987");

		row1.add(col1Row1);
		row1.add(col2Row1);




		/*Row 2*/
		ArrayList<ColumnDataUpdate> row2 = new ArrayList<ColumnDataUpdate>();

		/*column NAME Row2*/
		ColumnDataUpdate col1Row2 = new ColumnDataUpdate();

		col1Row2.setColumnId(379);
		//col2Row1.setOldColumnValue("");
		col1Row2.setNewColumnValue("Mike");

		/*column Year Row2*/
		ColumnDataUpdate col2Row2 = new ColumnDataUpdate();
		col2Row2.setColumnId(378);
		//col2Row2.setOldColumnValue("");
		col2Row2.setNewColumnValue("1950");


		row2.add(col1Row2);
		row2.add(col2Row2);


		/*add rows to list*/
		List<List<ColumnDataUpdate>> insertRows = new ArrayList(new ArrayList<ColumnDataUpdate>());

		insertRows.add(row1);
		insertRows.add(row2);

		/*print created*/
		print(insertRows);

		/*create and return wrapper*/
		UpdateDataRequest updateReq = new UpdateDataRequest();
		updateReq.setServerId(serverId);
		updateReq.setTableId(tableId);
		updateReq.setUpdates(insertRows);

		return updateReq;
	}


	/*
	serverId=6
	 tableId = 83  name 'new_table_test_autoincrement'
	 user.username = 804427 has userAccess to tableId = 83
	 * 
		 LOCAL METADATA
		 id	active	column_alias
		378	true	year
		379	true	name
		380	true	id


	EXISTING 
		REMOTE DATA
		ID	NAME	YEAR
		1	abv	1989
		2	cdr	1990
		3	bfg	2008
		4	abv-new	
		5	2020	
		6	new data	
		7		2017



	AFTER DUMMY TEST
	ID	NAME	YEAR
		...
		8	new data	1987
		9	Mike	1950

	 */
	public static UpdateDataRequest createDummyAddRecordsForAutoIncrementTable_new_table_test_autoincrement(Integer serverId, Integer tableId){
		/*MYSQL table has autoincrement- this method will insert 2 rows in remote DB*/

		/*ROW 1*/
		ArrayList<ColumnDataUpdate> row1 = new ArrayList<ColumnDataUpdate>();

		/*column NAME ROW1*/
		ColumnDataUpdate col1Row1 = new ColumnDataUpdate();

		col1Row1.setColumnId(379);
		//col1Row1.setOldColumnValue("abv");
		col1Row1.setNewColumnValue("new data");

		/*column YEAR row1*/
		ColumnDataUpdate col2Row1 = new ColumnDataUpdate();
		col2Row1.setColumnId(378);
		//col2Row1.setOldColumnValue("");
		col2Row1.setNewColumnValue("1987");

		row1.add(col1Row1);
		row1.add(col2Row1);




		/*Row 2*/
		ArrayList<ColumnDataUpdate> row2 = new ArrayList<ColumnDataUpdate>();

		/*column NAME Row2*/
		ColumnDataUpdate col1Row2 = new ColumnDataUpdate();

		col1Row2.setColumnId(379);
		//col2Row1.setOldColumnValue("");
		col1Row2.setNewColumnValue("Mike");

		/*column Year Row2*/
		ColumnDataUpdate col2Row2 = new ColumnDataUpdate();
		col2Row2.setColumnId(378);
		//col2Row2.setOldColumnValue("");
		col2Row2.setNewColumnValue("1950");


		row2.add(col1Row2);
		row2.add(col2Row2);


		/*add rows to list*/
		List<List<ColumnDataUpdate>> insertRows = new ArrayList(new ArrayList<ColumnDataUpdate>());

		insertRows.add(row1);
		insertRows.add(row2);

		/*print created*/
		print(insertRows);

		/*create and return wrapper*/
		UpdateDataRequest updateReq = new UpdateDataRequest();
		updateReq.setServerId(serverId);
		updateReq.setTableId(tableId);
		updateReq.setUpdates(insertRows);

		return updateReq;
	}



	
	
	
	/*
	serverId=6
	 tableId = 136  name 'new_table_test_autoincrement'
	 user.username = 804427 has userAccess to tableId = 136

	 LOCAL METADATA 
	 
	 id		active	name	allow_update	is_nullable		is_primary_key
	 ----------------------------------------------------------
			652	true	year	true			true			false
			653	true	name	true			true			false
			654	true	id		false			false			true
	 
		REMOTE Data
		 id	active	column_alias
		
		654		653		652
		# id, name, year
		----------------------
		'1', 'abv', '1989'
		'2', 'cdr', '1990'
		'3', 'bfg', '2008'
		'4', 'abv-new', NULL


	BEFORE and AFTER DUMMY TEST DUMMY TEST
		ID	NAME	YEAR
	-----------------
	before
		2 	cdr			1990
	after
		2 	cdr_upate	1991
	
	before
		3 	bfg		2008	
	after	
		3	bfg_new	2009	
		

	 */
	public static UpdateDataRequest createDummyUpdateRequestForAutoIncrementTable_new_table_test_autoincrement(Integer serverId, Integer tableId){
		/*MYSQL table has autoincrement- this method will insert 2 rows in remote DB*/
		logger.info("createDummyUpdateRequest" + "for serverId: "  + serverId + " and tableId: " + tableId);
		logger.info("PK must be set!");
		/*ROW 1*/
		ArrayList<ColumnDataUpdate> row1 = new ArrayList<ColumnDataUpdate>();

		/*column NAME ROW1*/
		ColumnDataUpdate col1Row1 = new ColumnDataUpdate();

		col1Row1.setColumnId(653);
		col1Row1.setOldColumnValue("cdr");
		col1Row1.setNewColumnValue("cdr_new");

		/*column YEAR row1*/
		ColumnDataUpdate col2Row1 = new ColumnDataUpdate();
		col2Row1.setColumnId(652);
		col2Row1.setOldColumnValue("1990");
		col2Row1.setNewColumnValue("1991");

		/*column ID row1*/
		ColumnDataUpdate col3Row1 = new ColumnDataUpdate();
		col3Row1.setColumnId(654);
		col3Row1.setOldColumnValue("2");
		//col3Row1.setNewColumnValue("2");
		
		row1.add(col1Row1);
		row1.add(col2Row1);
		row1.add(col3Row1);
		
		/*Row 2*/
		ArrayList<ColumnDataUpdate> row2 = new ArrayList<ColumnDataUpdate>();

		/*column NAME Row2*/
		ColumnDataUpdate col1Row2 = new ColumnDataUpdate();

		col1Row2.setColumnId(653);
		col1Row2.setOldColumnValue("bfg");
		col1Row2.setNewColumnValue("bfg_new");

		/*column Year Row2*/
		ColumnDataUpdate col2Row2 = new ColumnDataUpdate();
		col2Row2.setColumnId(652);
		col2Row2.setOldColumnValue("2008");
		col2Row2.setNewColumnValue("2009");

		ColumnDataUpdate col3Row2 = new ColumnDataUpdate();
		col3Row2.setColumnId(654);
		col3Row2.setOldColumnValue("3");
		//col3Row2.setNewColumnValue("3");
		
		row2.add(col1Row2);
		row2.add(col2Row2);
		row2.add(col3Row2);

		/*add rows to list*/
		List<List<ColumnDataUpdate>> updateRows = new ArrayList(new ArrayList<ColumnDataUpdate>());

		updateRows.add(row1);
		updateRows.add(row2);

		/*print created*/
		print(updateRows);

		/*create and return wrapper*/
		UpdateDataRequest updateReq = new UpdateDataRequest();
		updateReq.setServerId(serverId);
		updateReq.setTableId(tableId);
		updateReq.setUpdates(updateRows);

		return updateReq;
	}

	
	
	private static void print(List<List<ColumnDataUpdate>> columns) {

		int columnsSize = columns.size();
		for(int i = 0; i < columnsSize; i ++){
			List<ColumnDataUpdate> col = columns.get(i);
			logger.info("row: " + (i+1));
			//System.out.println("col: " + (i+1));

			int colSize = col.size();

			for(int j=0; j < colSize; j ++){
				//System.out.println(col.get(i).getColumnId() + ", " + col.get(i).getOldColumnValue() + ", " + col.get(i).getNewColumnValue());
				logger.info( "columnId:" + 
						col.get(j).getColumnId() + ", " + col.get(j).getOldColumnValue() + ", " + col.get(j).getNewColumnValue());
			}
		}

	}
	
	/*
	 * DELETE records
	 * 
			 REMOTE DB
			 # id, name, year
		------------------
		'1', 'abv', '1989'
		'2', 'cdr_new', '1991'
		'3', 'bfg_new', '2009'
		'4', 'abv-new', NULL
		'5', '2020', NULL
		'6', 'new data', NULL
		'7', NULL, '2017'
		'8', 'new data', '1987'
		'9', 'Mike', '1950'
		'10', 'new data', '1987'
		'11', 'Mike', '1950'
		'12', 'new data', '1987'
		'13', 'Mike', '1950'

		DELETE RECORDS:
		'9', 'Mike', '1950'
		'10', 'new data', '1987'
			
	 * 
	 */
	public static UpdateDataRequest createDummyDeleteRecordsForAutoIncrementTable_new_table_test_autoincrement(int serverId, int tableId) {
		
		/*MYSQL table has autoincrement- this method will insert 2 rows in remote DB*/
		logger.info("createDummy DELETE request" + "for serverId: "  + serverId + " and tableId: " + tableId);
		logger.info("PK must be set!");
		logger.info("No other columns are used!");
		/*ROW 1*/
		ArrayList<ColumnDataUpdate> row1 = new ArrayList<ColumnDataUpdate>();

		/*column NAME ROW1*/
		ColumnDataUpdate col1Row1 = new ColumnDataUpdate();

		//col1Row1.setColumnId(653);
		//col1Row1.setOldColumnValue("cdr");
		//col1Row1.setNewColumnValue("cdr_new");

		/*column YEAR row1*/
		//ColumnDataUpdate col2Row1 = new ColumnDataUpdate();
		//col2Row1.setColumnId(652);
		//col2Row1.setOldColumnValue("1990");
		//col2Row1.setNewColumnValue("1991");

		/*column ID row1*/
		ColumnDataUpdate col3Row1 = new ColumnDataUpdate();
		col3Row1.setColumnId(654);
		col3Row1.setOldColumnValue("9");
		//col3Row1.setNewColumnValue("2");
		
		//row1.add(col1Row1);
		//row1.add(col2Row1);
		row1.add(col3Row1);
		
		/*Row 2*/
		ArrayList<ColumnDataUpdate> row2 = new ArrayList<ColumnDataUpdate>();

		/*column NAME Row2*/
		//ColumnDataUpdate col1Row2 = new ColumnDataUpdate();

		//col1Row2.setColumnId(653);
		//col1Row2.setOldColumnValue("bfg");
		//col1Row2.setNewColumnValue("bfg_new");

		/*column Year Row2*/
		//ColumnDataUpdate col2Row2 = new ColumnDataUpdate();
		//col2Row2.setColumnId(652);
		//col2Row2.setOldColumnValue("2008");
		//col2Row2.setNewColumnValue("2009");

		ColumnDataUpdate col3Row2 = new ColumnDataUpdate();
		col3Row2.setColumnId(654);
		col3Row2.setOldColumnValue("10");
		//col3Row2.setNewColumnValue("3");
		
		//row2.add(col1Row2);
		//row2.add(col2Row2);
		row2.add(col3Row2);

		/*add rows to list*/
		List<List<ColumnDataUpdate>> updateRows = new ArrayList(new ArrayList<ColumnDataUpdate>());

		updateRows.add(row1);
		updateRows.add(row2);

		/*print created*/
		print(updateRows);

		/*create and return wrapper*/
		UpdateDataRequest updateReq = new UpdateDataRequest();
		updateReq.setServerId(serverId);
		updateReq.setTableId(tableId);
		updateReq.setUpdates(updateRows);

		return updateReq;
		
	}

	public static void main(String[] args) {
		//UpdateDataRequestDummy.createDummyAddRecordsForNonAutoIncrementTable_new_table_test(6, 43);

		//ADD TEST
		//UpdateDataRequestDummy.createDummyAddRecordsForAutoIncrementTable_new_table_test_autoincrement(6,83);
		
		//UPDATE TEST
		//UpdateDataRequestDummy.createDummyUpdateRequestForAutoIncrementTable_new_table_test_autoincrement(6, 136);
		
		//DELETE TEST
		UpdateDataRequestDummy.createDummyDeleteRecordsForAutoIncrementTable_new_table_test_autoincrement(6,136);
	}


	

}
