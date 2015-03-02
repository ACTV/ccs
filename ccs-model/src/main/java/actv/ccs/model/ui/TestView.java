package actv.ccs.model.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import actv.ccs.model.*;
import actv.ccs.model.type.FishState;

public class TestView extends JFrame {
	
	private ConvictCichlid cichlid;
	
	private JButton pauseButton = new JButton("Pause");
	
	public TestView()
	{
		setTitle("Convict Cichlid Fish Simulator Test 1");
		setSize(1000,600);
		
		// create menu bar
		JMenuBar b = createJMenu();
		this.setJMenuBar(b);
		
		cichlid = getFromDB();
		
		TestController controller = new TestController(cichlid, this);
		
		controller.updateView(); // starting
		
		controller.setFishState(FishState.SWIM); // update data
		
		controller.updateView(); // data is updated
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(2,2));
		dataPanel.setBorder(new EtchedBorder());
		this.add(dataPanel, BorderLayout.SOUTH);
		
		// output fish data to one side ... this is good for now.
		JLabel printData = new JLabel("Data Output");
		dataPanel.add(printData);
		String dataOutput = getFromDB().toString();
		printData.setText(dataOutput);
		
		dataPanel.add(pauseButton);
		
		
		
		this.setVisible(true);
		
	}
	
	public void printData(FishState state, float[] location, float aggroLevel, float length, float height, float weight, String name){
		System.out.println("FishState is " + state);
		System.out.println("Fish location is " + location);
		System.out.println("Fish aggro is " + aggroLevel);
		System.out.println("Fish length is " + length);
		System.out.println("Fish height is " + height);
		System.out.println("Fish weight is " + weight);
		System.out.println("Fish name is " + name);
		
	}
	private static ConvictCichlid getFromDB()
	{
		ConvictCichlid c = new ConvictCichlid();
		c.setLocation(new float[] {1,1});
		c.setState(FishState.NONE);
		c.setLength(10);
		c.setHeight(5);
		c.setWeight(10);
		c.setName("Shark");
		return c;
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
