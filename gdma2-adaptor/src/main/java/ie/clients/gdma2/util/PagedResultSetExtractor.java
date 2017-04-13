package ie.clients.gdma2.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class PagedResultSetExtractor implements org.springframework.jdbc.core.ResultSetExtractor {

    private static Logger LOG = Logger.getLogger(PagedResultSetExtractor.class);

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

        if (LOG.isDebugEnabled()) {
            LOG.debug("rowsPerPage [" + rowsPerPage + "], rowOffset [" + rowOffset + "]");
        }
        List rows = new ArrayList(rowsPerPage);

        // only jump to a row when required
        try {
            if (rowOffset > 0)
                rs.absolute(rowOffset);
        } catch (Throwable e) {
            // absolute may not work sometimes
            while (rs.next() && rs.getRow() < rowOffset)
                ;
            LOG.error(e, e);
        }

        // rs.setFetchSize(30);

        // should be ok as rs starts at 1
        while (rs.next() && rs.getRow() <= maxRow) {
            rows.add(rowMapper.mapRow(rs, rs.getRow()));
        }

        return rows;
    }
}
