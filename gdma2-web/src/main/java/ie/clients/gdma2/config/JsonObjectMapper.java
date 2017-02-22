/**
 * 
 */
package ie.clients.gdma2.config;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 805096
 * 
 */
public class JsonObjectMapper extends ObjectMapper {

	public JsonObjectMapper() {
		this.setDateFormat(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS"));
	}

}
