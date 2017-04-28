package ie.clients.gdma2;

import java.io.Serializable;
import java.util.List;

import ie.clients.gdma2.adaptor.MetaDataServiceImpl;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.spi.interfaces.MetaDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @author 805096
 * Farrukh Mirza
 *
 */
@SpringBootApplication
public class GDMAApp extends SpringBootServletInitializer {

	/*private static final Logger logger = LoggerFactory.getLogger(GDMAApp.class); */
	
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GDMAApp.class);
	}

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(GDMAApp.class, args);
		
	}
	
	
	
	/*
	@Bean
    public MetaDataService mtdService() {
        MetaDataService mtd = new MetaDataServiceImpl();
        return mtd;
    }
    */
	
	/*
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
	   
		Serializable versionString = org.hibernate.Version.getVersionString();
		System.out.println("*****HIBERNATE VERSION: " + versionString);
		CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
	    crlf.setIncludeClientInfo(true);
	    crlf.setIncludeQueryString(true);
	    crlf.setIncludePayload(true);
	
	    return crlf;
	}
*/
	
}
