package ie.clients.gdma2.util;

/**
 * @author RGILL
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class EscapeDBObject {

	public static String escapeObjectName(String serverConnectionURL, String objectName) {
		
		String initEscapeChar = "";
		String endEscapeChar = "";
		
		if(serverConnectionURL.toLowerCase().contains("sqlserver")) {
			initEscapeChar = "[";
			endEscapeChar = "]";
		}
		else if(serverConnectionURL.toLowerCase().contains("mysql")) {
			initEscapeChar = "`";
			endEscapeChar = "`";
		}
		else if(	serverConnectionURL.toLowerCase().contains("oracle")
				|| 	serverConnectionURL.toLowerCase().contains("teradata")) {
			initEscapeChar = "\"";
			endEscapeChar = "\"";
			
		}
		else if(serverConnectionURL.toLowerCase().contains("postgresql")) {
			initEscapeChar = "\"";
			endEscapeChar = "\"";
			
			int indexOfSeparator = objectName.indexOf(".");
			String schema = "";
			String tableName = "";
			
			if (indexOfSeparator != -1) 
			{
				schema = objectName.substring(0 , indexOfSeparator); 
				tableName = objectName.substring(indexOfSeparator + 1);
				
				objectName = schema + endEscapeChar + "." + initEscapeChar + tableName;
			}
		}		
		
		return initEscapeChar + objectName + endEscapeChar;
	}

	

}
