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
 * @author Victor
 */
public class MainFrame extends JFrame
{
    private JPanel buttonPanel, glViewPanel;
    private MainFunction ptrFunc;
    
    MainFrame(MainFunction ptrFunc, GLCanvas GLc)
    {
        setTitle("Victor's Simulation Window");
        setSize(800,300);
        this.ptrFunc = ptrFunc;
        setVisible(true);   
      //  createButtons();
        glViewPanel = new JPanel();
        glViewPanel.setSize(600,600);
        //glViewPanel.add(GLc);
        //glViewPanel.setVisible(true);
        //add(glVeiwPanel);
        add(GLc);//add OPENGL window to frame
      //  glViewPanel.addMouseWheelListener(ptrFunc);
      //  glViewPanel.addMouseMotionListener(ptrFunc);
        
    }
 
    
    private void createButtons()
    {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7,1));
        //setup and add buttons
        JButton hndlJButton = new JButton("Toggle up/down or left or right");
        hndlJButton.addActionListener(ptrFunc.getCommand(1));
        hndlJButton.addKeyListener(ptrFunc);
        hndlJButton.addMouseMotionListener(ptrFunc);
        hndlJButton.addMouseWheelListener(ptrFunc);
        buttonPanel.add(hndlJButton);
        
        hndlJButton = new JButton("Toggle BG color");
        hndlJButton.addActionListener(ptrFunc.getCommand(0));
        buttonPanel.add(hndlJButton);
        
        hndlJButton = new JButton("Change triangle color");
        hndlJButton.addActionListener(ptrFunc.getCommand(2));
        buttonPanel.add(hndlJButton);
        
        add(buttonPanel,BorderLayout.WEST);
        
    }
}


