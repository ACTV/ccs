package actv.ccs.sageTest;

import sage.app.BaseGame;
import sage.display.*;
import sage.scene.HUDString;
import sage.scene.shape.Rectangle;


public class TestGame extends BaseGame {
	
		IDisplaySystem display;
		HUDString testString;
		HUDString whatToDOString;
		HUDString whodunnit;
		public void initGame()
		{
			display = getDisplaySystem();
			display.setTitle("testing");
			
			// Rectangle rect = new Rectangle();
			// addGameWorldObject(rect);
			
			testString = new HUDString("HELLO. So this works at the start but when you try to call a new gameworld, the game doesn't instantiate? asked gordon but nothing concrete - albert");
			whatToDOString = new HUDString("Things to work on: Documentation, documentation, SDS STS UM etc, the pause/resume button. so far the hangup is with the resume button.\n another thing"
					+ "is the jankiness of the fish moving. animation doesn't look like ass. \nneed to add textures to the walls and shit. rules need to clearly show ai behavior when they're in each"
					+ "other sphere's of influence etc.");
			whatToDOString.setLocation(0, .09);
			whodunnit = new HUDString("ACT - Albert, Christian, Thomas, but where's Victor? - need to add the manual here if there's no real workaround.");
			whodunnit.setLocation(.05,.05);
			addGameWorldObject(testString);
			addGameWorldObject(whatToDOString);
			addGameWorldObject(whodunnit);
			
			
			display.close();
		}
		public void update(float t)
		{
			super.update(t);
		}
		
		

}
