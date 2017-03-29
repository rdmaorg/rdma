package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Column;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * Implementations should not close resultSet: it will be closed by the calling JdbcTemplate.
 * @author Avnet
 *
 */
public class ResultSetExtractorColumns implements ResultSetExtractor<Set<Column>>{

	private static final Logger logger = LoggerFactory.getLogger(ResultSetExtractorColumns.class);
	
	//@Override TODO
	public  Set<Column> extractData(ResultSet rs) throws SQLException, DataAccessException {
		logger.info("extractData");
		Set<Column> columns = new HashSet<Column>();
				
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {//starts from 1 not 0
			Column column  = readColumnMetaData(metaData,i);
			columns.add(column);
		}
		return columns;
	}
	
	/**
	 * 	Read Column MetaData and crate helper ColumnMetaData object
	 * first column is 1, the second is 2, ...
	 * 
	 * set parent Table later in caller!
	 * 
	 * @param metaData
	 * @param i
	 * @return Column Entity
	 * @throws SQLException
	 */
	private Column readColumnMetaData(ResultSetMetaData metaData, int i) throws SQLException {
		
		logger.info("readColumnMetaData");
		//String columnLabel = metaData.getColumnLabel(i);
		//boolean autoIncrement = metaData.isAutoIncrement(i);
		
		//the nullability status of the given column; one of columnNoNulls, columnNullable or columnNullableUnknown
		//int nullable = metaData.isNullable(i);
		
		//boolean writable = metaData.isWritable(i);
		
		Column col = new Column();
		//col.setTable();//TODO for all columns in caller method for GDMATable
		col.setName(metaData.getColumnName(i));
		col.setAlias(metaData.getColumnName(i));
		col.setColumnType(metaData.getColumnType(i));
		col.setColumnTypeString(metaData.getColumnTypeName(i));
		
		col.setDisplayed(true);
		col.setAllowInsert(true);
		col.setAllowUpdate(true);
		col.setNullable(metaData.isNullable(i) == ResultSetMetaData.columnNullable);
		
		//col.setPrimarykey(primarykey); //TODO false by default??
		
		col.setSpecial("N");
		col.setActive(true);
		col.setColumnSize(metaData.getColumnDisplaySize(i));
		
		//TODO ???
		//col.setMaxWidth(maxWidth);
		//col.setMinWidth(minWidth);
		//col.setOrderby(orderby);
			
		logger.info(col.toString());
		return col;
		
	}
	
//	
}
