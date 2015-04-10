package actv.ccs.sageTest;

import java.awt.Canvas;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JFrame;

import sage.display.DisplaySystem;
import sage.display.IDisplaySystem;
import sage.renderer.IRenderer;
import sage.renderer.RendererFactory;

public class MyDisplaySystem implements IDisplaySystem
{
 private JFrame myFrame;
 private GraphicsDevice device;
 private IRenderer myRenderer;
 private int width, height, bitDepth, refreshRate;
 private Canvas rendererCanvas;
 private boolean isCreated;
 private boolean isFullScreen;
 public MyDisplaySystem(int w, int h, int depth, int rate, boolean isFS,
 String rName)
 { //save the input parameters for accessor queries
 width = w; height = h; bitDepth = depth; refreshRate = rate;
 this.isFullScreen = isFS;
 // get a renderer from the RendererFactory
 myRenderer = RendererFactory.createRenderer(rName);
 if (myRenderer == null)
 { throw new RuntimeException("Unable to find renderer"); }
 rendererCanvas = myRenderer.getCanvas();
 myFrame = new JFrame("Default Title");
 myFrame.add(rendererCanvas);
 //initialize the screen with the specified parameters
 DisplayMode displayMode =
 new DisplayMode(width, height, bitDepth, refreshRate);
 initScreen(displayMode, isFullScreen);
 // save DisplaySystem, show frame and indicate DisplaySystem is created
 DisplaySystem.setCurrentDisplaySystem(this);
 myFrame.setVisible(true);
 isCreated = true;
 }
 private void initScreen(DisplayMode dispMode, boolean FSRequested)
 {
 GraphicsEnvironment environment =
 GraphicsEnvironment.getLocalGraphicsEnvironment();
 device = environment.getDefaultScreenDevice();
 if (device.isFullScreenSupported() && FSRequested)
 { myFrame.setUndecorated(true); // suppress title bar, borders, etc.
 myFrame.setResizable(false); // full-screen so not resizeable
 myFrame.setIgnoreRepaint(true); // ignore AWT repaints
 // Put device in full-screen mode. This must be done before attempting
 // to change the DisplayMode; the application must first have FSEM
 device.setFullScreenWindow(myFrame);
 //try to set the full-screen device DisplayMode
 if (dispMode != null && device.isDisplayChangeSupported())
 { try
 { device.setDisplayMode(dispMode);
 myFrame.setSize(dispMode.getWidth(), dispMode.getHeight());
 } catch (Exception ex)
 { System.err.println("Exception setting DisplayMode: " + ex ); }
 } else
 { System.err.println ("Cannot set display mode"); }
 } else
 { //use windowed mode – set JFrame characteristics
 myFrame.setSize(dispMode.getWidth(),dispMode.getHeight());
 myFrame.setLocationRelativeTo(null); //centers window on screen
 } }
 
 public void setCustomCursor(String s){}
 public void setPredefinedCursor(int i){}
 public void convertPointToScreen(Point p){}
 public void addKeyListener(java.awt.event.KeyListener k){}; 
 public void addMouseListener(java.awt.event.MouseListener m){}
 public void addMouseMotionListener(java.awt.event.MouseMotionListener m){}
 //public void convertPointToScreen(java.awt.Point p) {};
 public int getBitDepth(){
    return bitDepth;
 }
 public int getHeight(){
    return height;
 }
 public int getRefreshRate() {
    return refreshRate;
 }
 public int getWidth(){
    return width;
 }
 public IRenderer getRenderer(){
    return myRenderer;
 }
 public boolean isCreated(){
    return isCreated; 
 }
 public boolean isFullScreen(){
    return isFullScreen;
 }
 public boolean isShowing(){
    return true;
 }
 public void setBitDepth(int numBits)  {}
 //public void setCustomCursor(java.lang.String fileName){};
 public void setHeight(int height) {} 
 //public void setPredefinedCursor(int Cursor) {};
 public void  setRefreshRate(int rate) {}
 public void setTitle(String newTitle)
 {
   this.myFrame.setTitle(newTitle);
 }
 public void  setWidth(int width)  {}
 // restores the system to non-FSEM
 public void close(){ 
    if (device != null){ 
       Window window = device.getFullScreenWindow();
       if (window != null){ 
          window.dispose();
       }
    device.setFullScreenWindow(null);
    } 
 } 
}
