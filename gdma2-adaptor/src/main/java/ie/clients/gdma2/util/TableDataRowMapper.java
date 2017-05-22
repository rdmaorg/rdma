package ie.clients.gdma2.util;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

public class TableDataRowMapper implements org.springframework.jdbc.core.RowMapper<TableRowDTO>{

	@Override
	public TableRowDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		TableRowDTO tableRowDTO = new TableRowDTO();
		tableRowDTO.setRowNumber(BigInteger.valueOf(rowNum));

		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnName(i);
			Object resultSetValue = JdbcUtils.getResultSetValue(rs, i);
			TableRowDTO.TableColumn column =  tableRowDTO.new TableColumn(columnName, resultSetValue);
			tableRowDTO.getColumns().add(column);
		}
		return tableRowDTO;
	}
}




