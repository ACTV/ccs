package actv.ccs.model.ui;

import javax.swing.JPanel;

import javax.media.opengl.awt.GLCanvas;

import actv.ccs.model.*;
import actv.ccs.model.graphics.*;


public class TankView extends JPanel {
	private SimulationWorld sW;
	private GLCanvas glC;
	private MainGraphics mg;

	public TankView(SimulationWorld s)
	{
		glC = new GLCanvas();
		sW = s;
		// mg = new MainGraphics(glC, null, null, null, null, name);
	}
	
	// here is where i want to have sage engine information connected to here
}

