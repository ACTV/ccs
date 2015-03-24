package actv.ccs.model.ui;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.media.opengl.awt.GLCanvas;

import actv.ccs.model.*;
import actv.ccs.model.graphics.*;
import actv.ccs.model.IDrawable;


public class TankView extends JPanel implements IObserver {
	private SimulationWorld sW;
	private GLCanvas glC;
	private MainGraphics mg;

	public TankView(SimulationWorld s, MainGraphics m)
	{
		sW = s;
		System.out.println("ddw");

	}
	
	public void update(IObservable o, Object obj)
	{
		obj = (SimulationWorldProxy) o;
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		Iterator it = sW.getIterator();
		while (it.hasNext())
		{
			IDrawable obj = (IDrawable) it.getNext();
			obj.draw(g);
			System.out.println("drawww");
		}

	}
	// here is where i want to have sage engine information connected to here
}

