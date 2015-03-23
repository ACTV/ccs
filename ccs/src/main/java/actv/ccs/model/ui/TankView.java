package actv.ccs.model.ui;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.media.opengl.awt.GLCanvas;

import actv.ccs.model.*;
import actv.ccs.model.graphics.*;


public class TankView extends JPanel implements IObserver {
	private SimulationWorld sW;
	private GLCanvas glC;
	private MainGraphics mg;

	public TankView(SimulationWorld s, MainGraphics m)
	{
		sW = s;
		this.mg = m;

	}
	
	public void update(IObservable o, Object obj)
	{
		obj = (SimulationWorldProxy) o;
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		
	}
	// here is where i want to have sage engine information connected to here
}

