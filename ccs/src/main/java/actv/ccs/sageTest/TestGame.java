package actv.ccs.sageTest;

import sage.app.BaseGame;
import sage.display.*;
import sage.scene.HUDString;
import sage.scene.shape.Rectangle;


public class TestGame extends BaseGame {
	
		IDisplaySystem display;
		HUDString testString;
	
		public void initGame()
		{
			display = getDisplaySystem();
			display.setTitle("testing");
			
			// Rectangle rect = new Rectangle();
			// addGameWorldObject(rect);
			
			testString = new HUDString("HELLO. JUST PUTTING THIS HERE SO TEXTURES CAN WORK!!! - albert");
			addGameWorldObject(testString);
			
			
			
			display.close();
		}
		public void update(float t)
		{
			super.update(t);
		}
		
		

}
