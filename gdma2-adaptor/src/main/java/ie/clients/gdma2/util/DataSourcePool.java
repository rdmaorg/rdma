package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Server;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

/**
 * HELP 1 : http://stackoverflow.com/questions/1336885/how-do-i-manually-configure-a-datasource-in-java
 * HELP 2: http://blog.jhades.org/how-does-spring-transactional-really-work/
 * 
 * We can use 'Commons DBCP2' project.
 * TODO: prepare production ready config !
 * 
 * See: 
 * 	"29.1.2 Connection to a production database" on this link: 	https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html
 * 		"Additional connection pools can always be configured manually. If you define your own DataSource bean, auto-configuration will not occur."
 * 
 * It provides a BasicDataSource.
 * 
 * Update gdma2\pom.xml	
 * 
 * 1)
	 <dependency>
	    <groupId>org.apache.commons</groupId>
	    <artifactId>commons-dbcp2</artifactId>
	    <version>2.0</version>
	</dependency>
 * 
 * 
 * To use that you need : 
 *  2) the database vendor's JDBC JAR in your classpath. 
 *  To add support for e.g. MySQL do this:  
 *  		
 *  Update gdma2\pom.xml
 *  	 <dependency>
        	<groupId>mysql</groupId>
        	<artifactId>mysql-connector-java</artifactId>
    	</dependency>
 *  
 *  3) and you have to specify the vendor's driver class name and the database URL in the proper format.
 *  	This is part of registration of Server and ConenctionType in metadata DB, as precondition.
 *   
 * 
 * JAVADOC:	 javax.sql.DataSource
 * 
 * A factory for connections to the physical data source that this DataSource object represents.
 * An alternative to the DriverManager facility, a DataSource object is the preferred means of getting a connection.
 * An object that implements the DataSource interface will typically be registered with a naming service based on the Java™ Naming and Directory (JNDI) API. 

 * The DataSource interface is implemented by a driver vendor. There are three types of implementations: 
 * 1) Basic implementation -- produces a standard Connection object <BR>
 * 2) Connection pooling implementation -- produces a Connection object that will automatically participate in connection pooling. This implementation works with a middle-tier connection pooling manager. <BR>
 * 3) Distributed transaction implementation -- produces a Connection object that may be used for distributed transactions... 

 * Here we will use 2) Connection pooling implementation based on 'Commons DBCP2'
 *  
 *  REFRESH: 
 *  DataSource pool needs to be refreshed if server or connectionType properties has been changed after initial entry into pool (resynch attempts)
 *  
 * @author Avnet
 *
 */

@Component
public class DataSourcePool {
	private static final Logger logger = LoggerFactory.getLogger(DataSourcePool.class);

	/*each server will have associated DataSource and TxManager.
	 *  This is Map of created pairs <serverId, DSTM>. When needed, take DS from this Map or create new one. */
	private Map<Integer, DataSourceTransactionManager> dataSoucePool = new HashMap<Integer, DataSourceTransactionManager>();


	/*3 scenarios: 
	a) initial usage - create DataSource for server
	b) later usage - get existing DS (server and Connection type haven't been changed)
	c) later usage - get existing DS but server or connection type have been changed - refresh*/


	public DataSourceTransactionManager getTransactionManager(Server server) {
		logger.info("getTransactionManager(): ");
		if (!dataSoucePool.containsKey(server.getId()) ) {
			logger.info("datasource does not exist for this server, creating new one...");
			return createDataSource(server);
		} else {
			if (serverOrConnectionNotUpdated(server)){
				return dataSoucePool.get(server.getId());	
			} else{
				dataSoucePool.remove(server.getId());
				return createDataSource(server);

			}
		}
	}

	/*check if server or connection type properties have been changed after initial putting into datasource pool*/
	private boolean serverOrConnectionNotUpdated(Server server) {
		logger.info("was server Or Connection Updated... ?" + server.getId());
		DataSourceTransactionManager dataSourceTransactionManager = dataSoucePool.get(server.getId());
		DataSource dataSource = dataSourceTransactionManager.getDataSource();
		org.apache.commons.dbcp2.BasicDataSource basicDataSource = (BasicDataSource) dataSource; // GDMA.

		if ( basicDataSource.getUrl().equals(server.getConnectionUrl()) &&
				basicDataSource.getUsername().equals(server.getUsername()) &&
				basicDataSource.getPassword().equals(server.getPassword()) &&
				basicDataSource.getDriverClassName().equals(server.getConnectionType().getConnectionClass())) {
			logger.info("...no update, using existing fetching...");
			return true;
		} else{
			logger.info("...yes, datasource exists for this server, but needs refresh - server or connection was updated");
			return false;	
		}

	}


	private synchronized DataSourceTransactionManager createDataSource(Server server) {
		logger.info("createDataSource(), for server:  " + server.getId());
		// just in case ...
		//if (dataSoucePool.containsKey(server.getId())) {
		//	return dataSoucePool.get(server.getId());
		//}

		try {
			// TODO configure properly - i.e. maube add max and min to server
			// config

			//BasicDataSourc basicDataSource = new BasicDataSource(); // GDMA 1 version : org.apache.commons.dbcp.BasicDataSource;
			org.apache.commons.dbcp2.BasicDataSource basicDataSource = new BasicDataSource(); // GDMA

			basicDataSource.setUrl(server.getConnectionUrl());
			basicDataSource.setUsername(server.getUsername());
			basicDataSource.setPassword(server.getPassword());

			basicDataSource.setDriverClassName(server.getConnectionType().getConnectionClass());
			//BH todo get these from property file, just doing it fast now for o2
			// Teradata closes connection after 15 minuites idle time so we want to remove from pool before this.
			basicDataSource.setMinEvictableIdleTimeMillis(24000);//4 min *60*100
			basicDataSource.setTimeBetweenEvictionRunsMillis(60000);//10 min *60*100

			//If connecting to a Sybase database with a jTDS driver the ValidationQuery property of the data source must be set
			if(server.getConnectionUrl().toLowerCase().contains(":jtds:")) {
				basicDataSource.setValidationQuery("select 1");
			}
			
			
			// BH

			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(basicDataSource);

			dataSoucePool.put(server.getId(), transactionManager);

			server.setConnected(true);
			return transactionManager;
		} catch (Throwable e) {
			logger.error(e.getMessage());
			server.setConnected(false);
			server.setLastError(e.getMessage());
		}
		return null;
	}


	public JdbcTemplate getJdbcTemplateFromDataDource(Server server){
		DataSource dataSource = getTransactionManager(server).getDataSource();
		return new JdbcTemplate(dataSource);			
	}

}



