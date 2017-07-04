package ie.clients.gdma2.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * Note: if UI ever uses dropdown value 'All' then values for OFFSET and LIMIT are sent as 
 * 		start:0
		length:-1

	This will brake current impl because :
	 	a) List rows = new ArrayList(rowsPerPage);  //rowsPerPage = -1 produces Exception:  ' Illegal Capacity: -1'
	 	b)  
	 	
	 	 	 this.rowOffset = rowOffset;  // 0
        	this.rowsPerPage = rowsPerPage;  // -1
        	maxRow = rowOffset + rowsPerPage; // -1
        	
        	later
        	
        	  while (rs.next() && rs.getRow() <= maxRow) {  // is never executed => empty result
        	  
      CONCLUSION: Using 'All' in dropdown value in page size is never to be used, use value 100 instead.
      If in future there is a need to set  'All' all problems above needs to be fixed to avoid illegal capacity of Lista and to extract values from RS 	  
		
 * @author Avnet
 *
 */
public class PagedResultSetExtractor implements org.springframework.jdbc.core.ResultSetExtractor {

    
    private static final Logger logger = LoggerFactory.getLogger(PagedResultSetExtractor.class);

    private final RowMapper rowMapper;

    private final int rowOffset;

    private final int rowsPerPage;

    private final int maxRow;

    /**
     * Create a new paged extractor
     * 
     * @param rowMapper
     *            the row mapper to user
     * @param rowOffset
     *            the starting row - note this is zero based but results sets
     *            use 1
     * @param rowsPerPage
     *            the number of rows per page
     */
    public PagedResultSetExtractor(RowMapper rowMapper, int rowOffset, int rowsPerPage) {
        this.rowMapper = rowMapper;
        this.rowOffset = rowOffset;
        this.rowsPerPage = rowsPerPage;
        maxRow = rowOffset + rowsPerPage;
    }

    /**
     * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
     */
    @SuppressWarnings("unchecked")
    public Object extractData(ResultSet rs) throws SQLException {

        if (logger.isDebugEnabled()) {
        	logger.debug("rowsPerPage [" + rowsPerPage + "], rowOffset [" + rowOffset + "]");
        }
        List rows;
        if(rowsPerPage > 0){
        	rows = new ArrayList(rowsPerPage);
        } else {
        	rows = new ArrayList();
        }

        // only jump to a row when required
        try {
            if (rowOffset > 0)
                rs.absolute(rowOffset);
        } catch (Throwable e) {
            // absolute may not work sometimes
            while (rs.next() && rs.getRow() < rowOffset)
                ;
            logger.error(e.getMessage());
        }

        // rs.setFetchSize(30);

        // should be ok as rs starts at 1
        if(maxRow > 0) {
        	while (rs.next() && rs.getRow() <= maxRow) {
        		rows.add(rowMapper.mapRow(rs, rs.getRow()));
        	}
        } else {
        	//retrieve all rows
        	while (rs.next()) {
        		rows.add(rowMapper.mapRow(rs, rs.getRow()));
        	}
        }

        return rows;
    }
}
