/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

//import CSS.graghics.commands.ChangeBGcolor;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.Timer;

import actv.ccs.model.ui.IObservable;
import actv.ccs.model.ui.IObserver;
import actv.ccs.model.ui.SimulationWorldProxy;

/**
 *
 * @author Victor
 */
public class MainFunction extends JPanel implements ActionListener, MouseWheelListener, KeyListener, MouseMotionListener, IObserver 
{
    private MainGraphics mg;
    private AbstractAction cmds[];
    private Timer gameTick;
    private boolean swapDirection, paused, flagSaveLastLoc;
    private float dx, dy, dz, xaxis, yaxis, zaxis, speed;
    private final float db;
    public MainFunction(MainGraphics mg)
    {
        this.mg = mg;
        flagSaveLastLoc = false;
        cmds = new AbstractAction[5];
        paused = false;
        
        //cmds[0] = new ChangeBGcolor(mg,"change BG Color"); //setup command objects and store them in this object
        //cmds[1] = new ChangeDirection(this,"change direction");
        //cmds[2] = new ChangeTriangleColor(mg,"change triangle color");
        speed = 1.6f;
        dx = 3;
        dy = 0;
        dz = 0;
        db = 0.05f;
        xaxis = 0;
        yaxis = 1;
        zaxis = 0;
        gameTick = new Timer(15,this); //start the animation
        swapDirection = false;
        gameTick.start();
    }
    //pos is the init position
    static int pos = -900, dt = 2, mod = -1; //dt is the speed of the movment
    static int lastValue = 0;
    private void offSetTriangle()
    {
        if(pos > 950 || pos < -950)   //handles the movement of the triangle back and fort
            mod *= -1;
        
        pos += dt * mod; //mod reverses direction
        
        if(!swapDirection)
        {
            if(flagSaveLastLoc)
            {
                lastValue = pos;
                flagSaveLastLoc = false;
                
            }
            mg.offSet((float)((pos)/1000.00),(float)(lastValue/1000.00));
        } //up/down or left/right?
        else
        {
            if(flagSaveLastLoc)
            {
                lastValue =  pos;
                flagSaveLastLoc = false;
                if(mod == 1)
                    pos -= lastValue;
            }
            
            mg.offSet((float)( lastValue/1000.00),(float)((pos)/1000.00));  
        }
               
    }
 
    public void actionPerformed(ActionEvent e)
    {
 
   //     moveObjects();
        mg.paintScreen(); //refresh/update the opengl screen
        //System.out.printf("ffff");
    }
    
    public void playPause() //unused pause function
    {
        if(!paused)
        {
            gameTick.stop();
            paused = true;
        }
        else
        {
            gameTick.start();
            paused = false;
        }
    }
    //change the triangle from sidewats to skyways
    public void setSwapDirection(boolean swapDirection) //change the triangl
    {
        this.swapDirection = swapDirection;
        flagSaveLastLoc = true;
    }
    
    public AbstractAction getCommand(int index)
    {
        return cmds[index]; //returns command object
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        Point3D pt = mg.getLightPos();
        mg.incLightPos(0,0,(float)( pt.getZ() /100)); //adds or subtracts from the speed varible dt
    }

    public void keyTyped(KeyEvent e)
    {
        //System.out.printf("ffff");
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    private static boolean showAxis = false;
    public void keyPressed(KeyEvent e)
    {

       
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_W:
                mg.fowardSelectedCam(0,0.15f);
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_S:
                mg.fowardSelectedCam(0,-0.15f);
                //System.out.printf("ffff\n");
            break;
            
            case KeyEvent.VK_D:
                mg.straefSelectedCam(0,-0.15f);
            break;
            case KeyEvent.VK_A:
                mg.straefSelectedCam(0,0.15f);
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_UP:
                mg.pitchSelectedCam(0,1.75f);
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_DOWN:
                mg.pitchSelectedCam(0,-1.75f);
            break;    
                
            case KeyEvent.VK_LEFT:
                mg.panSelectedCam(0,1.75f);
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_RIGHT:
                mg.panSelectedCam(0,-1.75f);
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_E:
                mg.vertmoveSelectedCam(0,0.15f);
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_Q:
                mg.vertmoveSelectedCam(0,-0.15f);
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_NUMPAD4:
                dx += db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_NUMPAD5:
                dy += db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
            break;    
                
            case KeyEvent.VK_NUMPAD6:
                dz += db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_NUMPAD1:
                dx -= db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_NUMPAD2:
                dy -= db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_NUMPAD3:
                dz -= db;
                System.out.printf("\ndx:" + dx + " dy:" + dy + " dz:" + dz + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_INSERT:
                xaxis += db;
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_HOME:
                yaxis += db;
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
            break;    
                
            case KeyEvent.VK_PAGE_UP:
                zaxis += db;
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_DELETE:
                xaxis -= db;
                mg.setLightPos(xaxis,xaxis,xaxis);
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
                //System.out.printf("ffff\n");
            break;
                
                
            case KeyEvent.VK_END:
                yaxis -= db;
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_PAGE_DOWN:
                zaxis -= db;
                System.out.printf("\nxaxis:" + xaxis + " yaxis:" + yaxis + " zaxis:" + zaxis + "\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_PLUS:
                speed += db;
                System.out.printf("\nspeed:" + speed +"\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_MINUS:
                speed -= db;
                System.out.printf("\nspeed:" + speed +"\n");
                //System.out.printf("ffff\n");
            break;
                
            case KeyEvent.VK_SPACE:
                showAxis = !showAxis;
                mg.setShowAxis(showAxis);
                //System.out.printf("ffff\n");
            break;
        }
        
        
    }

    public void keyReleased(KeyEvent e)
    {
        //System.out.printf("ffff");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    private void moveObjects()
    {
        
        
        //mg.setObjectPos(0, (float) Math.sin((float)System.currentTimeMillis()) * 10000,0,(float) Math.sin((float)System.currentTimeMillis()));
        //mg.translateObject(0, 0.0005f,0,0);
        //mg.rotateObject(0,0.5f,new Vector3D(0,0,1));
        //mg.rotateObject(0, 0.24f, 0, 0);
        //mg.setRotationAxis(0, 0.5f, 0, 0);
        
        //mg.setRotationAxis(2, -0.85f, new Vector3D(3.249998,0.14999984,0.8500002),0.1500001f,-2.9999993f,-0.249998f);
        //mg.setRotationAxis(1, speed, new Vector3D(xaxis,yaxis,zaxis),dx,dy,dz);
        
        //mg.setRotationAxis(2, speed, new Vector3D(xaxis,yaxis,zaxis),dx,dy,dz);
        //mg.setRotation(0,0,0,0.25f,0,0,0);
        
       // mg.rotateObject(0,0.5f,new Vector3D(0,1,0));
        
        //mg.rotateObject(3,-0.5f,new Vector3D(0,1,0));
        
      // mg.setRotationAxisA(0, 1.8f,new Vector3D(0,1,0.0),0,0,2);
        
        //predefined orbits
        //dx:-0.8500002 dy:-3.399998 dz:-7.7500114
        //xaxis:0.0 yaxis:1.0 zaxis:0.45000005
        
        
        //dx:0.8500001 dy:1.9999993 dz:3.249998
        //xaxis:3.249998 yaxis:0.14999984 zaxis:0.8500002
        
    }
    
   // private void orbitOjectYaxis()


    public void mouseDragged(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseMoved(MouseEvent e)
    {
        Point2D p =  e.getPoint();
        Point3D pt = mg.getLightPos();
        mg.setLightPos(e.getX() /100.0f  ,e.getY()/100.0f ,(float) mg.getLightPos().getZ());
        System.out.println("mouse");
    }
	public void update(IObservable o, Object obj)
	{
		obj = (SimulationWorldProxy) o;
	//	repaint();
	}
}
