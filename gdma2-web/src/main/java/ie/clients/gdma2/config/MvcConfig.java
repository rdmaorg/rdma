/**
 * @author Farrukh Mirza
 * 24/06/2016 
 * Dublin, Ireland
 */
package ie.clients.gdma2.config;

import java.util.List;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import ie.clients.gdma2.domain.app.PageEnum;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcAutoConfigurationAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(PageEnum.HOME.path()).setViewName("home");
		registry.addViewController(PageEnum.ADMIN_SERVER.path()).setViewName("servers");
		registry.addViewController(PageEnum.ADMIN_USER.path()).setViewName("users");
		registry.addViewController(PageEnum.ADMIN_CONNECTIONS.path()).setViewName("connectionTypes");
		registry.addViewController(PageEnum.ADMIN_TABLE.path()).setViewName("tables");
		registry.addViewController(PageEnum.ADMIN_COLUMNS.path()).setViewName("columns");
		registry.addViewController(PageEnum.ERROR.path()).setViewName("error");
		registry.addViewController(PageEnum.ADMIN_AUDIT_LOG.path()).setViewName("auditLog");
		registry.addViewController(PageEnum.ADMIN_ACTIVITY_LOG.path()).setViewName("activityLog");
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
		// TODO Auto-generated method stub
		super.configureContentNegotiation(arg0);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		super.configureMessageConverters(converters);
		boolean found = false;
		for (HttpMessageConverter<?> mc : converters) {
			if (mc instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter) mc).setObjectMapper(new JsonObjectMapper());
				((MappingJackson2HttpMessageConverter) mc).setPrettyPrint(false);

				found = true;
				break;
			}
		}

		if (!found) {
			MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter();
			conv.setObjectMapper(new JsonObjectMapper());

			converters.add(conv);
		}

		super.configureMessageConverters(converters);
	}
	@Bean
	public ServletListenerRegistrationBean httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
	}

}
