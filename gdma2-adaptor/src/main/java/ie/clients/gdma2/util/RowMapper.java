package ie.clients.gdma2.util;



import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * Utility method to map a row in a result set to a list of objects.
 * 
 * @author Ronan Gill
 * 
 */
public class RowMapper implements org.springframework.jdbc.core.RowMapper {
    private static Logger LOG = Logger.getLogger(RowMapper.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @SuppressWarnings("unchecked")
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List columnValues = new ArrayList(columnCount + 1);
        columnValues.add(BigInteger.valueOf(rowNum));
        for (int i = 1; i <= columnCount; i++) {
            columnValues.add(JdbcUtils.getResultSetValue(rs, i));
        }
        return columnValues;
    }
}
