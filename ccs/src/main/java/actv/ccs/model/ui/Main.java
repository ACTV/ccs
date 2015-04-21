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
	
		
		MyGame mg = new MyGame();
		SimulationPrompter prompterTest = new SimulationPrompter(mg);
		mg.start();

	}
	

}
