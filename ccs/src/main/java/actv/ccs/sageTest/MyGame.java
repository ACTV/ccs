package actv.ccs.sageTest;

import sage.app.BaseGame;
import sage.display.IDisplaySystem;
import sage.scene.shape.Rectangle;

public class MyGame extends BaseGame {

	IDisplaySystem display;
	
	public void initGame()
	{
		display = getDisplaySystem();
		display.setTitle("sage implementation of the pain");
		
		Rectangle rect1 = new Rectangle(0.3f, 0.3f);
		addGameWorldObject(rect1);
		
	}
	public void update(float time)
	{
		super.update(time);
	}
}
