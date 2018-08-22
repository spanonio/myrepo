package mysql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class mysqlConn{

    private Connection connection;

	public Connection getInstance(String dbipandport, String dbName)
	{
	 if (connection == null)
	 {
		 connection = getConnection(dbipandport, dbName);
		 }
	 return connection;
	 }
	
	
	private Connection getConnection(String dbipandport, String dbName) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e1.printStackTrace();
			return null;
		}

		//System.out.println("MySQL JDBC Driver Registered!");

		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://" + dbipandport +"/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root", "ppKscKhmM8FPP8mH");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}

		if (connection != null) {
			System.out.println("Connected...");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	}
	
	public boolean testConnection(String dbipandport,String dbname) {
		
		if (getConnection(dbipandport,dbname) != null)
			return true;
		else return false;
		
		}
}


