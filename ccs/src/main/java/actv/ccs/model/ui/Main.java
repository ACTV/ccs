package actv.ccs.model.ui;

import java.io.IOException;
import java.sql.*;

import actv.ccs.sageTest.MyGame;


public class Main {

	// so the model is the Convict Cichlid
	// view is user view
	// controller is the observer
	
	// changelog 2-26-15
	/*
	 *  going to add in later stuff for fish tank
	 *  and figure out to make a view.
	 *  this is pretty much run ui but i'm going to add a jframe later on.
	 */
	
	public static void main(String[] args) throws SecurityException, IOException {
	
	//	LoadWindow loadWindow = new LoadWindow();
	//	SaveWindow newSW = new SaveWindow();
	//	NewSimulation newS = new NewSimulation();
		SimulationPrompter prompterTest = new SimulationPrompter();
	//	new MyGame().start();
	//	RunSimulation testRun = new RunSimulation();
		
		// going to try ucanaccess fun 
		
		// alright so ucanaccess works. here's the link to download it, put it into your referenced libraries etc. 
		// http://ucanaccess.sourceforge.net/site.html
		//Connection conn;
		//try {
			//conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
	
		//Statement s = conn.createStatement();
		//ResultSet rs = s.executeQuery("SELECT * FROM [FishPool]");
		//while (rs.next())
			//System.out.println(rs.getString(1));
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		//Moved database connection to NewSimulation.java
	}
	

}
