package ie.clients.gdma2.security.cas;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 02/09/2015
 */
@Component
@ConfigurationProperties(prefix = "cas", locations = "classpath:application.properties")
public class CasConfig {
	private String hostname;
	private String path;
	private String proxyReceptorUrl;
	private String entryPoint;
	private String logoutPath;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProxyReceptorUrl() {
		return proxyReceptorUrl;
	}

	public void setProxyReceptorUrl(String proxyReceptorUrl) {
		this.proxyReceptorUrl = proxyReceptorUrl;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	public String getLogoutPath() {
		return logoutPath;
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}
}
