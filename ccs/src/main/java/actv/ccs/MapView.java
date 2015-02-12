package actv.ccs;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MapView extends JPanel implements IObserver, MouseListener, MouseWheelListener, MouseMotionListener { 
	private GameWorld gw;
	AffineTransform worldToND, ndToScreen, theVTM, inverseVTM;
	private boolean pauseView;
	private double winLeft = 0;
	private double winWidth = 1000;
	private double winBottom = 0;
	private double winHeight = 600;
	private Point2D mouseLoc;
	
	public MapView(GameWorld g)
	{
		gw = g;
		pauseView = false;
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		addMouseMotionListener(this);
		mouseLoc = this.getMousePosition();
		
	}
	 public void paintComponent(Graphics g) // override as result of calling repaint();
	 {
		 super.paintComponent(g); // paint objects
		 Graphics2D g2d = (Graphics2D) g;
		 AffineTransform saveAT = g2d.getTransform();
		 
		 // update vtm
		 worldToND = buildWorldToNDXform(winLeft, winWidth, winBottom, winHeight);
		 ndToScreen = buildNDToScreenXform(1000, 600);
		 theVTM = (AffineTransform) ndToScreen.clone();
		 theVTM.concatenate(worldToND);
	
		 g2d.transform(theVTM);
		 
		 Iterator it = gw.getIterator(); 
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
	 public void update(IObservable o, Object obj) { 
		 obj = (GameWorldProxy) o;
		 repaint(); // call repaint on itself 
	}

	public void mousePressed(MouseEvent e) { 	

	 }

	public void mouseClicked(MouseEvent e) { // click on several objects to become selected

	 }
	 public void mouseEntered(MouseEvent e) { 
		 // TODO Auto-generated method stub 
	 }
	 public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub		
	 }
	 public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub	
	 }

	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

		
	}
	public void mouseMoved(MouseEvent e) {

	}
}