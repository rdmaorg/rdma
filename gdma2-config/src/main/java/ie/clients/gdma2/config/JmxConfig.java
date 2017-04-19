package ie.clients.gdma2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.support.MBeanServerFactoryBean;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 16/09/2015
 */
//@Profile("wildflyJmxConfig")
//@Configuration
public class JmxConfig {
	@Bean
	public MBeanServerFactoryBean mBeanServerFactoryBean(){
		MBeanServerFactoryBean mBeanServerFactoryBean = new MBeanServerFactoryBean();
		mBeanServerFactoryBean.setLocateExistingServerIfPossible(true);

		return mBeanServerFactoryBean;
	}
}
