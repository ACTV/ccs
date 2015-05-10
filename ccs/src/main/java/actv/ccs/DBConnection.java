package actv.ccs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * Singleton interface for a connection to the Access database.
 * The user of this class is unable to explicitly retrieve the connection.
 *
 */
public class DBConnection {
	private static DBConnection instance;
	private static Connection connection;
	// May need to use java.util.Properties
	private static final String dbFile = "jdbc:ucanaccess://FishPool.accdb";
	
	private DBConnection(){}
	
	public static DBConnection getInstance(){
		if(instance == null){
			instance = new DBConnection();
		}
		return instance;
	}
	
	public synchronized void createConnection() throws SQLException{
		if(connection == null || connection.isClosed()){
			connection = DriverManager.getConnection(dbFile);
		}
	}
	
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException("Unable to close the database connection");
		}
	}
	
	public synchronized void executeUpdate(String query) throws SQLException{
		connection.createStatement().executeUpdate(query);
	}
	
	public ResultSet executeQuery(String query) throws SQLException{
		return connection.createStatement().executeQuery(query);
	}
	
}
