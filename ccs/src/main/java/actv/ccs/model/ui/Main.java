package actv.ccs.model.ui;

import java.io.IOException;

import actv.ccs.sageTest.MyGame;
import actv.ccs.sageTest.TestGame;


public class Main {

	
	public static void main(String[] args) throws SecurityException, IOException {
	
		MyGame mg = new MyGame();
		SimulationPrompter prompterTest = new SimulationPrompter(mg);	

		mg.start();
	}
	

}
