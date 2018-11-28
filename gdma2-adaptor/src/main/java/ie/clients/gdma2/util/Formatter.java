package ie.clients.gdma2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @author RGILL
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class Formatter {

	// java.sql.Date = 91
	public static String dateFormat = "yyyy-MM-dd";
	public static String dateFormat2 = "dd/MM/yyyy";

	// java.sql.Time = 92
	public static String timeFormat = "HH:mm:ss";

	// java.sql.Timestamp = 93
	// (at some DB's also called DATETIME),
	public static String timeStampFormat = "yyyy-MM-dd HH:mm:ss";
	public static String timeStampFormat_ExcelWorkaround = "dd/MM/yyyy HH:mm:ss";

	private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

	private static SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);

	private static SimpleDateFormat sdf3 = new SimpleDateFormat(timeFormat);

	private static SimpleDateFormat sdf4 = new SimpleDateFormat(timeStampFormat);

	private static SimpleDateFormat sdf4_ExcelWorkaround = new SimpleDateFormat(timeStampFormat_ExcelWorkaround);

	private static String frontEndTimeStampFormat = "dd-MMM-yyyy HH:mm:ss.SSS";

	public static String escapeQuotes(String in) {
		if (in == null)
			return null;
		else
			return in.replaceAll("[\"']", "\\\"");
	}

	public static String escapeNewLines(String in) {
		if (in == null)
			return null;
		else
			return in.replaceAll("[\r\n]+", "\\\\n");
	}

	public static String escapeAll(String in) {
		in = escapeQuotes(in);
		in = escapeNewLines(in);
		return in;
	}

	public static String formatDate(Date date) {
		if (date == null)
			return "";
		else
			return sdf.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null)
			return "";
		else
			return sdf3.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null)
			return "";
		else
			return sdf4.format(date);
	}

	// Try 2 ways of parsing
	// First - treat it as a long
	// Second - treat it as a string
	public static Date parseDate(String value) throws Exception {
		Date date = null;
		try {
			date = new Date(Long.parseLong(value));
		} catch (Exception e) {
			date = null;
		}
		if (date == null) {
			try {
				date = sdf.parse(value);
			} catch (Exception e) {
				try {
					date = sdf2.parse(value);
				} catch (Exception e2) {
					throw new Exception("Could not parse value [" + value + "] into a date");
				}
			}
		}

		return date;
	}

	public static Date parseDateTime(String value) throws Exception {
		Date date = null;
		try {
			date = new Date(Long.parseLong(value));
		} catch (Exception e) {
			date = null;
		}
		if (date == null) {
			try {
				date = sdf4.parse(value);
			} catch (Exception e) {
				try {
					date = sdf4_ExcelWorkaround.parse(value);
				} catch (Exception e2) {
					date = null;
					throw new Exception("Could not parse value [" + value + "] into a datetime");
				}
			}
		}
		return date;
	}

	public static Timestamp parseTimeStamp(String value) throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(frontEndTimeStampFormat);
		try {
			LocalDateTime formatDateTime = LocalDateTime.parse(value, dateTimeFormatter);
			dateTimeFormatter = DateTimeFormatter.ofPattern(timeStampFormat);
			return Timestamp.valueOf(formatDateTime.format(dateTimeFormatter));
		} catch (Exception e) {
			throw new Exception("Could not pase value [" + value + "] into a TimeStamp");
		}
	}

	public static String getDateStringFromTimestamp(String timestamp) throws ParseException {
		String value = "";

		if (timestamp != null)
			value = formatDate(new Date(Long.parseLong(timestamp)));

		return value;
	}

	// Try 2 ways of parsing
	// First - treat it as a long
	// Second - treat it as a string
	public static String parseTime(String value) throws Exception {
		Date date = null;
		try {
			date = new Date(Long.parseLong(value));
		} catch (Exception e) {
			date = null;
		}
		if (date == null) {
			try {
				date = sdf3.parse(value);
			} catch (Exception e) {
				throw new Exception("Could not pase value [" + value + "] into a time");
			}
		}

		return value;
	}

}
