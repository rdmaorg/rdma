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

	public static void main(String[] args) {
		//UpdateDataRequestDummy.createDummyAddRecordsForNonAutoIncrementTable_new_table_test(6, 43);

		UpdateDataRequestDummy.createDummyAddRecordsForAutoIncrementTable_new_table_test_autoincrement(6,83);
	}

}
