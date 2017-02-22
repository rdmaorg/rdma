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

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GDMAApp.class);
	}

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(GDMAApp.class, args);
	}

}
