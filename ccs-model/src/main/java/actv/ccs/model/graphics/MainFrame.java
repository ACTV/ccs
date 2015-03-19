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
    private JPanel buttonPanel, glVeiwPanel;
    private MainFunction ptrFunc;
    
    MainFrame(MainFunction ptrFunc, GLCanvas GLc)
    {
        
        setTitle("a1");
        setSize(800,500);
        this.ptrFunc = ptrFunc;
        setVisible(true);   
        createButtons();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        glVeiwPanel = new JPanel();
        glVeiwPanel.setSize(600,600);
        //glVeiwPanel.add(GLc);
        //glVeiwPanel.setVisible(true);
        //add(glVeiwPanel);
        add(GLc);//add OPENGL wingow to frame
        glVeiwPanel.addMouseWheelListener(ptrFunc);
        glVeiwPanel.addMouseMotionListener(ptrFunc);
        
        
        
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


