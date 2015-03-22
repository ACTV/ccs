package actv.ccs.model.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;

import org.drools.runtime.StatefulKnowledgeSession;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.graphics.MainHub;
import actv.ccs.model.type.FishState;

public class RunSimulation extends JFrame{
	private String mainFilePath = "";
	private ConvictCichlid cichlid;
	private TankObject tank;
	private SimulationWorld world;
	private TankView tV;
	private GLCanvas glc; 
	private RuleEngineRunner runner;
	
	public RunSimulation() throws IOException
	{
		setTitle("Convict Cichlid Fish Simulator Test 1");
		setSize(1000,600);
		
	//	mH = new MainHub(mainFilePath);
		world = new SimulationWorld();
		glc = new GLCanvas();
		// create menu bar
		JMenuBar b = createJMenu();
		this.setJMenuBar(b);
		tV = new TankView(world, glc);
		
		// center panel for map
		tV.setBorder(new EtchedBorder());
		tV.setBackground(Color.orange);
		
		// Bottom Panel for Data Output
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(2,2));
		dataPanel.setBorder(new EtchedBorder());
		
		// output fish data to one side ... this is good for now.
		JLabel printData = new JLabel("Data Output");
		
		JTextPane outputDataHere = new JTextPane();
		outputDataHere.setText("Here would be where the fish hp goes... something along the lines of a Final Fantasy battle gui");
		
		JButton btnPauseButton = new JButton("Pause Button");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(outputDataHere, GroupLayout.PREFERRED_SIZE, 510, GroupLayout.PREFERRED_SIZE)
								.addComponent(printData))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnPauseButton))
						.addComponent(tV, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 982, GroupLayout.PREFERRED_SIZE))
					.addGap(137)
					.addComponent(dataPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(245)
							.addComponent(dataPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(145)
							.addComponent(printData, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(outputDataHere, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(tV, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnPauseButton)))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		
		this.setVisible(true);

		// Temporary creation of cichlid for testing
		cichlid = new ConvictCichlid();
		cichlid.setState(FishState.IDLE);
		cichlid.setIdleWaitTime(0);
		
		// Start the rule engine
		runner = RuleEngineRunner.getInstance();
		runner.newMap(cichlid, new Auditor());
		runner.start();
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