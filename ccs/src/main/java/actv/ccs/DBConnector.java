package actv.ccs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Singleton interface for a connection to the Access database.
 *
 */
public class DBConnector {
	private static Connection connection;
	// May need to use java.util.Properties
	private static final String dbFile = "jdbc:ucanaccess://FishPool.accdb";
	
	private DBConnector(){}
	
	public static Connection getConnection(){
		try {
			if(connection == null || connection.isClosed()){
				connection = DriverManager.getConnection(dbFile);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Unable to connect to the database!");
		}
		
		return connection;
	}
	
	public static void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException("Unable to close the database connection");
		}
	}
	
}
