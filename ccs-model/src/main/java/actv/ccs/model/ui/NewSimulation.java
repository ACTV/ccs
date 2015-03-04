package actv.ccs.model.ui;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import actv.ccs.model.*;
import actv.ccs.model.type.FishState;

public class NewSimulation extends JFrame {
	
	private ConvictCichlid cichlid;
	private TestView test;
	
	private JButton pauseButton = new JButton("Pause");
	private JTextField NameTextField;
	private JTextField WeighTextField;
	private JTextField WidthTextField;
	private JTextField HeightTextField;
	private String [] poolOfFish;
	
	public NewSimulation()
	{
		
		cichlid = new ConvictCichlid();
		test = new TestView();
		
		setTitle("Convict Cichlid Fish Simulator New Simulation Test");
		setSize(1000,600);
		poolOfFish = new String [] {"Stringer Bell", "Marlo Stanfield", "James McNulty", "The Bunk"};
		
		// create menu bar
		JMenuBar b = createJMenu();
		this.setJMenuBar(b);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel label = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, label, 984, SpringLayout.WEST, getContentPane());
		getContentPane().add(label);
		
		JLabel lblPleasePickA = new JLabel("Please pick a cichlid from the current pool: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblPleasePickA, 0, SpringLayout.NORTH, label);
		springLayout.putConstraint(SpringLayout.WEST, lblPleasePickA, 10, SpringLayout.WEST, label);
		getContentPane().add(lblPleasePickA);
		
		JComboBox comboBox = new JComboBox(poolOfFish);
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.EAST, comboBox, 70, SpringLayout.EAST, lblPleasePickA);
		getContentPane().add(comboBox);
		
		// then it would put in the values for the fish and make the fields unchangeable for the time being. later on, will do ... do you want to add more?
		
		JLabel lblIfNot = new JLabel("if not ... we can make a cichlid now!");
		springLayout.putConstraint(SpringLayout.NORTH, lblIfNot, 23, SpringLayout.SOUTH, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.WEST, lblIfNot, 0, SpringLayout.WEST, lblPleasePickA);
		getContentPane().add(lblIfNot);
		
		JLabel lblCichlidName = new JLabel("Cichlid Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidName, 8, SpringLayout.SOUTH, lblIfNot);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidName, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblCichlidName);
		
		JLabel lblWeightkg = new JLabel("Weight (kg): ");
		springLayout.putConstraint(SpringLayout.NORTH, lblWeightkg, 6, SpringLayout.SOUTH, lblCichlidName);
		springLayout.putConstraint(SpringLayout.WEST, lblWeightkg, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblWeightkg);
		
		JLabel lblSizewidthX = new JLabel("Size (Width x Height ) (cm):");
		springLayout.putConstraint(SpringLayout.NORTH, lblSizewidthX, 6, SpringLayout.SOUTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, lblSizewidthX, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblSizewidthX);
		
		NameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, NameTextField, 6, SpringLayout.SOUTH, lblIfNot);
		springLayout.putConstraint(SpringLayout.WEST, NameTextField, 75, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(NameTextField);
		NameTextField.setColumns(10);
		
		WeighTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, WeighTextField, 0, SpringLayout.NORTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, WeighTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(WeighTextField);
		WeighTextField.setColumns(10);
		
		WidthTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, WidthTextField, 0, SpringLayout.NORTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.EAST, WidthTextField, 0, SpringLayout.EAST, NameTextField);
		getContentPane().add(WidthTextField);
		WidthTextField.setColumns(10);
		
		HeightTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, HeightTextField, 0, SpringLayout.NORTH, WidthTextField);
		getContentPane().add(HeightTextField);
		HeightTextField.setColumns(10);
		
		JLabel x = new JLabel("X");
		springLayout.putConstraint(SpringLayout.WEST, HeightTextField, 6, SpringLayout.EAST, x);
		springLayout.putConstraint(SpringLayout.NORTH, x, 79, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.WEST, x, 6, SpringLayout.EAST, WidthTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, x, 0, SpringLayout.SOUTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.EAST, x, -734, SpringLayout.EAST, getContentPane());
		getContentPane().add(x);
		
		JLabel lblAddInGender = new JLabel("add in gender, age later on.");
		springLayout.putConstraint(SpringLayout.NORTH, lblAddInGender, 33, SpringLayout.SOUTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.WEST, lblAddInGender, 0, SpringLayout.WEST, lblPleasePickA);
		getContentPane().add(lblAddInGender);
		
		JButton btnGenerateFish = new JButton("Generate Fish");
		springLayout.putConstraint(SpringLayout.NORTH, btnGenerateFish, 0, SpringLayout.NORTH, lblAddInGender);
		springLayout.putConstraint(SpringLayout.WEST, btnGenerateFish, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(btnGenerateFish);
		
		JTextArea outputData = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, outputData, 71, SpringLayout.SOUTH, btnGenerateFish);
		springLayout.putConstraint(SpringLayout.WEST, outputData, 53, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, outputData, 239, SpringLayout.SOUTH, btnGenerateFish);
		springLayout.putConstraint(SpringLayout.EAST, outputData, 399, SpringLayout.WEST, getContentPane());
		getContentPane().add(outputData);
		
		JLabel lblTestingoutputData = new JLabel("testingOutput Data");
		springLayout.putConstraint(SpringLayout.WEST, lblTestingoutputData, 51, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblTestingoutputData, -20, SpringLayout.NORTH, outputData);
		getContentPane().add(lblTestingoutputData);
		
		cichlid = getFromDB();
		
		TestController controller = new TestController(cichlid, test);
		
		controller.updateView(); // starting
		
		controller.setFishState(FishState.SWIM); // update data
		
		controller.updateView(); // data is updated

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