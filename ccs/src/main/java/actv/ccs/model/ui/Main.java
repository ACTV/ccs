package actv.ccs.model.ui;

import java.io.IOException;

import actv.ccs.sageTest.TestGame;


public class Main {

	
	public static void main(String[] args) throws SecurityException, IOException {
	
		TestGame tg = new TestGame();
		SimulationPrompter prompterTest = new SimulationPrompter();	
		tg.start();
	}
	

}
