package ie.clients.gdma2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

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
