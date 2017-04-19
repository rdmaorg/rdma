package ie.clients.gdma2.security.cas;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 27/09/2016
 */
@Profile("localCachingCas")
@Configuration
public class LocalCasCacheConfig {
	@Bean
	public ProxyGrantingTicketStorage proxyGrantingTicketStorage(){
		return new ProxyGrantingTicketStorageImpl();
	}
}
