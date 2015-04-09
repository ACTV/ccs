/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * GUI functions are found here
 * 
 * This is where the main jframe or jpanel GLC handle is defined. A few inner 
 * J parameters can be accessed through here.
 * 
 * 
 * 
 * @author Victor
 */
public class MainFrame extends JFrame
{
    private JPanel buttonPanel, glViewPanel;
    private MainFunction ptrFunc;
    private GLCanvas Glc;
    
    MainFrame(GLCanvas GLc)
    {
        setTitle("Victor's Simulation Window");
        setSize(800,300);
        this.ptrFunc = ptrFunc;
        //setVisible(true);   
      //  createButtons();
        glViewPanel = new JPanel();
        glViewPanel.setSize(600,600);
        glViewPanel.add(GLc);
        glViewPanel.setVisible(true);
        add(glViewPanel);
        add(GLc);//add OPENGL window to frame
        this.Glc = GLc;
      //  glViewPanel.addMouseWheelListener(ptrFunc);
      //  glViewPanel.addMouseMotionListener(ptrFunc);
        
    }
 
    
    public JPanel getGLCPanel()   //get the panel that can SEE the graghics
    {
        JPanel ret = new JPanel();
        
        ret.add(Glc);
        
        return ret;
    }
    
    public JFrame getGLCFrame()   //get the Frame that can SEE the graghics
    {   
        return this;
    }
    
    public void visible(boolean x)
    {
        setVisible(x);
    }
}


