package ie.clients.gdma2.test.step;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import cucumber.api.java.Before;
import ie.clients.gdma2.test.utility.PropertyHandler;


public class SQLConnector {

	Connection con = null;
	Statement stmt = null;
	
	@Before
	public void setup() throws ClassNotFoundException, SQLException {
		try {
			String jdbcDriver = PropertyHandler.getProperty("test.jdbc.driver");
			String jdbcUrl = PropertyHandler.getProperty("test.jdbc.url");
			String jdbcUser = PropertyHandler.getProperty("test.jdbc.user");
			String jdbcPassword = PropertyHandler.getProperty("test.jdbc.password");

			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
			// Statement Object to send queries
			stmt = con.createStatement();
	    } catch (Exception e){
		    e.printStackTrace();
	    }
	}

      @Test
      public void test() {
         try{
			// Execute the SQL query. Store results in ResultSet
    	    String query ="UPDATE table_gdma2 SET active=false WHERE id=189";
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			// close resources
			try {
				if (stmt != null){
					stmt.close();
				}
				if (con != null){
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
