package actv.ccs.sageTest;

import sage.app.BaseGame;
import sage.display.*;
import sage.scene.shape.Rectangle;


public class TestGame extends BaseGame {
	
		IDisplaySystem display;
	
		public void initGame()
		{
			display = getDisplaySystem();
			display.setTitle("testing");
			
			Rectangle rect = new Rectangle();
			addGameWorldObject(rect);
			
		}
		public void update(float t)
		{
			super.update(t);
		}
		
}
