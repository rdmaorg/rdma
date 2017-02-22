package ie.clients.gdma2.rest;

/**
 * @author Farrukh Mirza (805096)
 *         Created on 22/02/2017
 */
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDataTableResource extends BaseResource {
	private static Logger logger = LoggerFactory.getLogger(BaseDataTableResource.class);

	public static final String SORT_ASCENDING = "asc";
	public static final String SORT_DESCENDING = "desc";

	private static final String QUERY_PARAM_DRAW = "draw";
	private static final String QUERY_PARAM_PAGINATION_START_INDEX = "start";
	private static final String QUERY_PARAM_PAGINATION_LENGTH = "length";
	private static final String QUERY_PARAM_PAGINATION_ORDERBY_COL_INDEX = "order[0][column]";
	private static final String QUERY_PARAM_PAGINATION_ORDERBY_DIR = "order[0][dir]";
	private static final String QUERY_PARAM_TABLE_SEARCH_TERM = "search[value]";

	protected int getDraw(Map<String, String> reqParams) {
		int val = Integer.parseInt(reqParams.get(QUERY_PARAM_DRAW) == null ? "0" : reqParams.get(QUERY_PARAM_DRAW));
		logger.debug(QUERY_PARAM_DRAW + ": " + val);
		return val;
	}

	protected int getStartIndex(Map<String, String> reqParams) {
		int val = Integer.parseInt(reqParams.get(QUERY_PARAM_PAGINATION_START_INDEX) == null ? "0"
				: reqParams.get(QUERY_PARAM_PAGINATION_START_INDEX));
		logger.debug(QUERY_PARAM_PAGINATION_START_INDEX + ": " + val);
		return val;
	}

	protected int getLength(Map<String, String> reqParams) {
		int val = Integer.parseInt(reqParams.get(QUERY_PARAM_PAGINATION_LENGTH) == null ? "10"
				: reqParams.get(QUERY_PARAM_PAGINATION_LENGTH));
		logger.debug(QUERY_PARAM_PAGINATION_LENGTH + ": " + val);
		return val;
	}

	protected int getOrderByColumn(Map<String, String> reqParams) {
		int val = Integer.parseInt(reqParams.get(QUERY_PARAM_PAGINATION_ORDERBY_COL_INDEX) == null ? "0"
				: reqParams.get(QUERY_PARAM_PAGINATION_ORDERBY_COL_INDEX));
		logger.debug(QUERY_PARAM_PAGINATION_ORDERBY_COL_INDEX + ": " + val);
		return val;
	}

	protected String getOrderByDirection(Map<String, String> reqParams) {
		String val = (reqParams.get(QUERY_PARAM_PAGINATION_ORDERBY_DIR) == null ? SORT_ASCENDING
				: reqParams.get(QUERY_PARAM_PAGINATION_ORDERBY_DIR));
		logger.debug(QUERY_PARAM_PAGINATION_ORDERBY_DIR + ": " + val);
		return val;
	}

	protected String getSearchValue(Map<String, String> reqParams) {
		String val = (reqParams.get(QUERY_PARAM_TABLE_SEARCH_TERM) == null ? ""
				: reqParams.get(QUERY_PARAM_TABLE_SEARCH_TERM));
		logger.debug(QUERY_PARAM_TABLE_SEARCH_TERM + ": " + val);
		return val;
	}

}
