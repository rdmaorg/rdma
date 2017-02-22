package ie.clients.gdma2.rest;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 16/10/2015
 */
public class JsonWrapper {
	private final String value;

	public JsonWrapper(String value) {
		this.value = value;
	}

	@JsonValue
	@JsonRawValue
	public String value() {
		return value;
	}
}
