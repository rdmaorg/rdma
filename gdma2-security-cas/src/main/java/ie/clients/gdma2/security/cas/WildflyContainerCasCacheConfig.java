package ie.clients.gdma2.security.cas;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jboss.as.clustering.infinispan.DefaultCacheContainer;
import org.kamranzafar.cas.client.infinispan.InfinispanProxyGrantingTicketStorage;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 27/09/2016
 */
@Profile("wildflyDistributedCachingCas")
@Configuration
public class WildflyContainerCasCacheConfig {
	@Value("${infinispan.cache.jndi.url}")
	private String infinispanCacheJndiUrl;

	@Bean
	public org.infinispan.Cache<String, Object> ssoCache() throws NamingException {
		return ((DefaultCacheContainer) new JndiTemplate().lookup(infinispanCacheJndiUrl)).getCache();
	}

	@Bean
	public ProxyGrantingTicketStorage proxyGrantingTicketStorage() throws NamingException {
		return new InfinispanProxyGrantingTicketStorage(ssoCache());
	}
}
