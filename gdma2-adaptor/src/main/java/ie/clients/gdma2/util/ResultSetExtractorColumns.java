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
	
	/**
	 * 
	 */
	@Override
	public  Set<Column> extractData(ResultSet rs) throws SQLException, DataAccessException {
		logger.info("extractData");
		Set<Column> columns = new HashSet<Column>();
				
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {//starts from 1 not 0
			ColumnMetaData columnMetaData = readColumnMetaData(metaData,i);
			Column column = createColumnEntity(columnMetaData);
			columns.add(column);
		}
		return columns;
	}

	/**
	 * read ColumnMetaData and create ie.clients.gdma2.domain.Column Entity
	 * @param columnMetaData
	 * @return Column
	 */
	private Column createColumnEntity(ColumnMetaData columnMetaData) {
		logger.info("createColumnEntity");
		/* TODO
		
		set table;
		
		dropDownColumnDisplay;
		dropDownColumnStore;

		//none or special type User or special type Date
		special; NOT NULL
		
		private Integer orderby;
		*/
		
		Column column = new Column();
		
		column.setName(columnMetaData.getColumnName());
		column.setColumnType(columnMetaData.getColumnType());
		column.setColumnTypeString(columnMetaData.getColumnTypeName());
		
		//column.setDisplayed(displayed); FALSE by default
		//column.setAllowInsert(allowInsert); FALSE by default
		//column.setAllowUpdate(allowUpdate); FALSE by default
		column.setNullable(columnMetaData.getNullable() == 1 ? false : true);
		
		//column.setPrimarykey TODO
		
		//none or special type User or special type Date ???
		column.setSpecial("none");
		
		//column.setMinWidth(columnMetaData.getColumnDisplaySize()); TODO
		//column.setMaxWidth(maxWidth); TODO 
		column.setColumnSize(columnMetaData.getColumnDisplaySize());
		
		return column;
		
		
	}

	/**
	 * 	Read Column MetaData and crate helper ColumnMetaData object
	 * first column is 1, the second is 2, ...
	 * @param metaData
	 * @param i
	 * @return ColumnMetaData
	 * @throws SQLException
	 */
	private ColumnMetaData readColumnMetaData(ResultSetMetaData metaData, int i) throws SQLException {
		logger.info("readColumnMetaData");
		String columnClassName = metaData.getColumnClassName(i);
		int columnDisplaySize = metaData.getColumnDisplaySize(i);
		String columnLabel = metaData.getColumnLabel(i);
		String columnName = metaData.getColumnName(i);
		int columnType = metaData.getColumnType(i);
		String columnTypeName = metaData.getColumnTypeName(i);
		
		boolean autoIncrement = metaData.isAutoIncrement(i);

		//the nullability status of the given column; one of columnNoNulls, columnNullable or columnNullableUnknown
		int nullable = metaData.isNullable(i);
		
		boolean writable = metaData.isWritable(i);
		
		ColumnMetaData columnMetaData = new ColumnMetaData(columnClassName,	columnDisplaySize, columnLabel, columnName, columnType,
				columnTypeName, autoIncrement, nullable, writable);
		
		logger.info(columnMetaData.toString());
		return columnMetaData;
		
	}
}
