package ie.clients.gdma2.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.management.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kamran Zafar (N000989) Created on 02/09/2015
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {
	@Value("${application.contextPath}")
	private String applicationContextPath;

//	@Autowired
//	private JmxConfig jmxConfig;

	private Logger logger = LoggerFactory.getLogger(CacheConfig.class);

	@Bean(destroyMethod = "shutdown")
	public net.sf.ehcache.CacheManager ehCacheManager() {
		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		// cacheConfiguration.setName("ticketCache");
		cacheConfiguration.setName("applicationCacheManager-" + applicationContextPath);
		cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
		cacheConfiguration.setMaxEntriesLocalHeap(1000);

		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.setName(cacheConfiguration.getName());
		config.addCache(cacheConfiguration);

		net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance(config);

		cacheManager.addCache(casTicketCache());

		return cacheManager;
	}

	@Bean
	public Cache casTicketCache() {
		Cache cache = new Cache("casTickets", 50, false, false, 3600, 900);

		logger.info("Initialized casTickets Cache");

		return cache;
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new SimpleKeyGenerator();
	}

	@Override
	public CacheResolver cacheResolver() {
		return null;
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return null;
	}

//	@Bean(destroyMethod = "dispose")
//	public ManagementService managementService() {
//		ManagementService managementService = new ManagementService(ehCacheManager(),
//				jmxConfig.mBeanServerFactoryBean().getObject(), true, true, true, true);
//		managementService.init();
//
//		return managementService;
//	}
}
