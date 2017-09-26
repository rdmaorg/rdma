package ie.clients.gdma2.domain.ui;

import java.util.List;
import java.util.Map;

public class PaginatedEditableTableResponse<T,K,V> extends PaginatedTableResponse<T> {

	private Map<K, List<V>> options;

	public Map<K, List<V>> getOptions() {
		return options;
	}

	public void setOptions(Map<K, List<V>> options) {
		this.options = options;
	}

}
