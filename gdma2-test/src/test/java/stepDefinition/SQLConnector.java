package stepDefinition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import cucumber.api.java.Before;


public class SQLConnector {

	Connection con = null;
	Statement stmt = null;
	
	@Before
	public void setup() throws ClassNotFoundException, SQLException {
		
		

		try {
			// JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Database connection
			//Connection con = DriverManager.getConnection(dbUrl,username,password);
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "root");

			// Statement Object to send queries

			stmt = con.createStatement();
		     }
		
		     catch (Exception e)
	
		     {
		
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
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			// close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
