package ie.clients.gdma2.security.cas;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.EhCacheBasedTicketCache;
import org.springframework.security.cas.authentication.StatelessTicketCache;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import ie.clients.gdma2.config.CacheConfig;
import ie.clients.gdma2.domain.app.PageEnum;
import ie.clients.gdma2.spi.interfaces.UserVerificationFilter;

/**
 * @author Kamran Zafar (N000989) Created on 01/09/2015
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${application.protocol}")
	private String applicationProtocol;
	@Value("${application.hostname}")
	private String applicationHostname;
	@Value("${application.contextPath}")
	private String applicationContextPath;

	@Autowired
	private AuthenticationUserDetailsService userDetailsService;
	@Autowired
	private UserVerificationFilter userVerificationFilter;
	
	@Autowired
	private CacheConfig cacheConfig;

	@Autowired
	private CasConfig casConfig;


	@Autowired
	private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

	private Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private String casUrl() {
		return applicationProtocol + "://" + casConfig.getHostname();
	}

	private String baseUrl() {
		return applicationProtocol + "://" + applicationHostname;
	}

	private String applicationBaseUrl() {
		return baseUrl() + "/" + applicationContextPath;
	}

	private String logoutUrl() {
		return baseUrl() + casConfig.getLogoutPath() + "?service=" + applicationBaseUrl();
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(applicationBaseUrl() + PageEnum.CAS_LOGIN.path());
		// serviceProperties.setSendRenew(false);
		serviceProperties.setAuthenticateAllArtifacts(true);
		return serviceProperties;
	}

	@Bean
	public StatelessTicketCache statelessTicketCache() {
		EhCacheBasedTicketCache ehCacheBasedTicketCache = new EhCacheBasedTicketCache();
		ehCacheBasedTicketCache.setCache(cacheConfig.casTicketCache());

		return ehCacheBasedTicketCache;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ProxyTicketValidator());
		casAuthenticationProvider.setStatelessTicketCache(statelessTicketCache());
		casAuthenticationProvider.setKey("casAuthProviderKey");
		return casAuthenticationProvider;
	}

//	@Bean
	public AuthenticationUserDetailsService authenticationUserDetailsService() {
//		return new UserDetailsService();
		return userDetailsService;
	}

	@Bean
	public Cas20ProxyTicketValidator cas20ProxyTicketValidator() {
		Cas20ProxyTicketValidator cas20ProxyTicketValidator = new Cas20ProxyTicketValidator(
				casUrl() + casConfig.getPath());
		cas20ProxyTicketValidator.setProxyCallbackUrl(
				casUrl() + "/" + applicationContextPath + casConfig.getProxyReceptorUrl());
		cas20ProxyTicketValidator.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
		cas20ProxyTicketValidator.setAcceptAnyProxy(true);
		cas20ProxyTicketValidator.setURLConnectionFactory(httpURLConnectionFactory());

		return cas20ProxyTicketValidator;
	}

	@Bean
	public HttpURLConnectionFactory httpURLConnectionFactory() {
		return new HttpURLConnectionFactory() {
			@Override
			public HttpURLConnection buildHttpURLConnection(URLConnection url) {
				if ("https".equalsIgnoreCase(url.getURL().getProtocol())) {
					HttpsURLConnection conn = (HttpsURLConnection) url;
					SSLContext context;

					try {
						context = SSLContext.getInstance("TLS");
						context.init(new KeyManager[0], new TrustManager[] { new X509TrustManager() {
							public void checkClientTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {
							}

							public void checkServerTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {
							}

							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}
						} }, new SecureRandom());

						conn.setSSLSocketFactory(context.getSocketFactory());
						conn.setHostnameVerifier(new HostnameVerifier() {
							public boolean verify(String hostname, SSLSession session) {
								return true;
							}
						});

						return conn;
					} catch (NoSuchAlgorithmException e) {
						logger.error(e.getMessage(), e);
					} catch (KeyManagementException e) {
						logger.error(e.getMessage(), e);
					}
				}

				return (HttpURLConnection) url;
			}
		};
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setServiceProperties(serviceProperties());
		casAuthenticationFilter.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
		casAuthenticationFilter.setProxyReceptorUrl(casConfig.getProxyReceptorUrl());
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setAuthenticationDetailsSource(serviceAuthenticationDetailsSource());
		casAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

		return casAuthenticationFilter;
	}

	@Bean
	public ServiceAuthenticationDetailsSource serviceAuthenticationDetailsSource() {
		return new ServiceAuthenticationDetailsSource(serviceProperties());
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/casfailed.jsp");

	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(baseUrl() + casConfig.getEntryPoint());
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(logoutUrl(), securityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl(PageEnum.CAS_LOGOUT.path());

		return logoutFilter;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		return new SingleSignOutFilter();
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");

		return characterEncodingFilter;
	}

//	@Bean
	public UserVerificationFilter userProcessFilter() {
//		return new UserVerificationFilter();
		return userVerificationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest().fullyAuthenticated();
		http.addFilterBefore(characterEncodingFilter(), LogoutFilter.class);
		http.addFilter(logoutFilter());
		http.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
		http.addFilter(casAuthenticationFilter());
		http.addFilterAfter(userProcessFilter(), CasAuthenticationFilter.class);
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
		http.logout().logoutSuccessUrl(PageEnum.CAS_LOGOUT.path());
		http.csrf().disable();
	}

	@Bean
	public SpringSecurityDialect getSpringSecurityDialectBean(){
		return new SpringSecurityDialect();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(casAuthenticationProvider());
	}
	
	@Bean
	public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
		return new ServletListenerRegistrationBean(new SingleSignOutHttpSessionListener());
	}

}
