package actv.ccs.model.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

import actv.ccs.model.*;
import actv.ccs.model.type.FishState;

public class RunSimulation extends JFrame {
	
	private ConvictCichlid cichlid;
	private TankObject tank;
	private SimulationWorld world;
	private TankView tV;
	
	private JButton pauseButton = new JButton("Pause");
	
	public RunSimulation()
	{
		setTitle("Convict Cichlid Fish Simulator Test 1");
		setSize(1000,600);
		
		
		world = new SimulationWorld();
		tV = new TankView(world);
		
		
		// create menu bar
		JMenuBar b = createJMenu();
		this.setJMenuBar(b);
		
		
		// center panel for map
		tV.setBorder(new EtchedBorder());
		tV.setBackground(Color.orange);
		this.add(tV, BorderLayout.CENTER);
		
		// Bottom Panel for Data Output
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(2,2));
		dataPanel.setBorder(new EtchedBorder());
		getContentPane().add(dataPanel, BorderLayout.SOUTH);
		
		JTextPane outputDataHere = new JTextPane();
		dataPanel.add(outputDataHere);
		
		// output fish data to one side ... this is good for now.
		JLabel printData = new JLabel("Data Output");
		dataPanel.add(printData);
		
		dataPanel.add(pauseButton);
		
		
		
		this.setVisible(true);
		
	}


	private JMenuBar createJMenu() { 
		// creating menubar
		JMenuBar bar = new JMenuBar();
		// initializing commands
		// File
		JMenu file = new JMenu("File");
		// sub testing
		JMenuItem mItem1 = new JMenuItem("test");
		file.add(mItem1);
		bar.add(file);
		// Edit
		JMenu edit = new JMenu("Edit");
		JMenuItem mItem2 = new JMenuItem("hadooekn");
		edit.add(mItem2);
		bar.add(edit);
		// Save
		JMenu save = new JMenu("Save");
		JMenuItem mItem3 = new JMenuItem("stuff");
		save.add(mItem3);		
		bar.add(save);
		// Load
		JMenu load = new JMenu("Load");
		JMenuItem mItem4 = new JMenuItem("boo");
		load.add(mItem4);
		bar.add(load);
		// Help
		JMenu help = new JMenu("Help");
		JMenuItem mItem5 = new JMenuItem("bom");
		help.add(mItem5);
		bar.add(help);
		// About
		JMenu about = new JMenu("About");
		JMenuItem mItem6 = new JMenuItem("hi");
		about.add(mItem6);		
		bar.add(about);
		
		return bar;
	}

}