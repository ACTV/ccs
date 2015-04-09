package actv.ccs.model.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.media.opengl.awt.GLCanvas;

import actv.ccs.model.*;
import actv.ccs.model.IDrawable;


public class TankView extends JPanel implements IObserver {
	private SimulationWorld sW;
	AffineTransform worldToND, ndToScreen, theVTM, inverseVTM;
	private boolean pauseView;
	private double winLeft = 0;
	private double winWidth = 1000;
	private double winBottom = 0;
	private double winHeight = 500;
	public TankView(SimulationWorld s)
	{
            sW = s;
	}
	
// test
	/*
	public TankView(SimulationWorld s, GLCanvas glc)
	{
            add(glc);
            sW = s;
	}
	
      */  
        //NOT REALLY NEEDED. KEEPING IT HERE FOR REFERENCE -VICTOR HUBA
        
	public void update(IObservable o, Object obj)
	{
		obj = (SimulationWorldProxy) o;
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		 super.paintComponent(g); // paint objects
		 Graphics2D g2d = (Graphics2D) g;
		 AffineTransform saveAT = g2d.getTransform();
		 
		 // update vtm
		 worldToND = buildWorldToNDXform(winLeft, winWidth, winBottom, winHeight);
		 ndToScreen = buildNDToScreenXform(1000, 300);
		 theVTM = (AffineTransform) ndToScreen.clone();
		 theVTM.concatenate(worldToND);
	
		 g2d.transform(theVTM);
		 
		 Iterator it = sW.getIterator(); 
		 while (it.hasNext()) // get every game object
		 {
			 IDrawable obj = (IDrawable) it.getNext();
			 obj.draw(g2d); // draw the object
		 }
		 g2d.setTransform(saveAT);

	}
	public AffineTransform buildWorldToNDXform(double wiLeft, double wiWidth, double wiBottom, double wiHeight)
	{
		AffineTransform at = new AffineTransform();
	    at.scale(1/wiWidth, 1/wiHeight); 
		at.translate(-wiLeft, -wiBottom);

		return at;
	}
	public AffineTransform buildNDToScreenXform(int x, int y)
	{
		AffineTransform at = new AffineTransform();
		at.translate(0, y);
		at.scale(x, -y);
		return at;
	}	
        
	// here is where i want to have sage engine information connected to here
}

