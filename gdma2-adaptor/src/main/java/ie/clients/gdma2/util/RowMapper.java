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
 * see: http://stackoverflow.com/questions/15118630/some-doubts-about-rowmapper-use-in-jdbc-in-a-spring-framework-application
 * @author Ronan Gill
 * 
 */
public class RowMapper implements org.springframework.jdbc.core.RowMapper {
    
	private static Logger LOG = Logger.getLogger(RowMapper.class);

    /**
     * return List of objects representing all values in one column of resultset
     */
    @SuppressWarnings("unchecked")
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        
    	ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        
        List columnValues = new ArrayList(columnCount + 1);
        columnValues.add(BigInteger.valueOf(rowNum));
        
        for (int i = 1; i <= columnCount; i++) {
            columnValues.add(JdbcUtils.getResultSetValue(rs, i)); 
            //Object obj = rs.getObject(index); //starting 1... Generically fetches value on unknown type in column=> Object is used
        }
        return columnValues;
    }
}
