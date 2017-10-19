package appModules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLConnection{

	public static void update(String id) throws ClassNotFoundException, SQLException {
		
		Connection con = null;
		Statement stmt = null;

		try {
			// JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Database connection
			//Connection con = DriverManager.getConnection(dbUrl,username,password);
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","root");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gdma2","postgres","admin");
			// Statement Object to send queries

			stmt = con.createStatement();
		     
			
		   String query ="UPDATE table_gdma2 SET active=false WHERE id=" +id;
		   stmt.executeUpdate(query);
		   con.close();
	       }
		
		 catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			} 
		
		 catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			} 
		
		finally {
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

     public static void delete(String id) throws ClassNotFoundException, SQLException {
		
		Connection con = null;
		Statement stmt = null;

		try {
			//load JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Database connection
			//Connection con = DriverManager.getConnection(dbUrl,username,password);
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","root");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gdma2","postgres","admin");
			// Statement Object to send queries

			stmt = con.createStatement();
		     
			
		   String query ="DELETE FROM server_gdma2 WHERE id=" +id;
		   stmt.executeUpdate(query);
		   con.close();
	       }
		
		 catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			} 
		
		 catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			} 
		
		finally {
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
