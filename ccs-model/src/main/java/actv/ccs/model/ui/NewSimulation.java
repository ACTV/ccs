package actv.ccs.model.ui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import actv.ccs.model.*;
import actv.ccs.model.type.FishState;

public class NewSimulation extends JFrame {
	
	private ConvictCichlid cichlid;
	private SimulationWorld world;
	private ConvictCichlidController controller;
	
	private JButton pauseButton = new JButton("Pause");
	private JTextField NameTextField;
	private JTextField WeightTextField;
	private JTextField WidthTextField;
	private JTextField HeightTextField;
	private String [] poolOfFish;
	
	

/* updates are from latest to oldest (top to bottom)
 * 
 * 3-4-15
 * form is done. just need to make checks and flags and resize the width by height window
 * 
 * 3-4-15
 * need to do a button listener later on etc fish stuff.
 */
	
	public NewSimulation()
	{
		
		cichlid = new ConvictCichlid();
		world = new SimulationWorld();
		cichlid = getFromDB();
		
		controller = new ConvictCichlidController(cichlid, world);
		
		setTitle("Convict Cichlid Fish Simulator New Simulation Test");
		setSize(1000,600);
		poolOfFish = new String [] {"addyourownfish", "Stringer Bell", "Marlo Stanfield", "James McNulty", "The Bunk"};
		
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
		springLayout.putConstraint(SpringLayout.EAST, comboBox, -642, SpringLayout.EAST, getContentPane());
		getContentPane().add(comboBox);
		
		// find a way to get the database merger here
		
		
		// then it would put in the values for the fish and make the fields unchangeable for the time being. later on, will do ... do you want to add more?
		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					// show fish values
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				String selectedFish = (String) combo.getSelectedItem();
				
				if (selectedFish.equals("Stringer Bell"))
				{
					NameTextField.setText("Stringer Bell");
					String cichlidNameT = NameTextField.getText().toString();
					controller.setName(cichlidNameT);
					
					
					WeightTextField.setText("10.0");
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);
					controller.setWeight(weightC);
					
					WidthTextField.setText("1.0");
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);
					controller.setLength(widthC);
					
					HeightTextField.setText("3.0");
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);
					controller.setHeight(heightC);	
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					
					
					controller.updateView();					
				}
				else if (selectedFish.equals("Marlo Stanfield"))
				{
					NameTextField.setText("Marlo Stanfield");
					String cichlidNameT = NameTextField.getText().toString();
					controller.setName(cichlidNameT);
					
					
					WeightTextField.setText("100.0");
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);
					controller.setWeight(weightC);
					
					WidthTextField.setText("10.0");
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);
					controller.setLength(widthC);
					
					HeightTextField.setText("30.0");
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);
					controller.setHeight(heightC);
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					
					
					controller.updateView();					
					
				}
				else if (selectedFish.equals("James McNulty"))
				{
					NameTextField.setText("James McNulty");
					String cichlidNameT = NameTextField.getText().toString();
					controller.setName(cichlidNameT);
					
					
					WeightTextField.setText("101.0");
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);
					controller.setWeight(weightC);
					
					WidthTextField.setText("11.0");
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);
					controller.setLength(widthC);
					
					HeightTextField.setText("31.0");
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);
					controller.setHeight(heightC);
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					
					
					controller.updateView();					
				
				}
				else if (selectedFish.equals("The Bunk"))
				{
					NameTextField.setText("The Bunk");
					String cichlidNameT = NameTextField.getText().toString();
					controller.setName(cichlidNameT);
					
					
					WeightTextField.setText("102.0");
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);
					controller.setWeight(weightC);
					
					WidthTextField.setText("12.0");
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);
					controller.setLength(widthC);
					
					HeightTextField.setText("32.0");
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);
					controller.setHeight(heightC);
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					
					
					controller.updateView();					
				
				}
				else if (selectedFish.equals("addyourownfish"))
				{
					NameTextField.setEditable(true);
					WeightTextField.setEditable(true);
					WidthTextField.setEditable(true);
					HeightTextField.setEditable(true);
					
					
					System.out.println("do nothing");
				}
				
			}
		});
	
		
		
		
		
		
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
		NameTextField.setText("Goddamn Sharks");
		springLayout.putConstraint(SpringLayout.NORTH, NameTextField, 6, SpringLayout.SOUTH, lblIfNot);
		springLayout.putConstraint(SpringLayout.WEST, NameTextField, 75, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(NameTextField);
		NameTextField.setColumns(10);
		final String cichlidNameT = NameTextField.getText().toString();
		
	//	controller.setName(cichlidNameT);

		
		WeightTextField = new JTextField();
		WeightTextField.setText("150.0");
		springLayout.putConstraint(SpringLayout.NORTH, WeightTextField, 0, SpringLayout.NORTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, WeightTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(WeightTextField);
		WeightTextField.setColumns(10);
		String weightS = WeightTextField.getText().toString();
		final float weightC = Float.parseFloat(weightS);
	//	System.out.println(weightC);
		
	//	controller.setWeight(weightC);	
		
		WidthTextField = new JTextField();
		WidthTextField.setText("12.0");
		springLayout.putConstraint(SpringLayout.NORTH, WidthTextField, 0, SpringLayout.NORTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.EAST, WidthTextField, 0, SpringLayout.EAST, NameTextField);
		getContentPane().add(WidthTextField);
		WidthTextField.setColumns(10);
		String widthS = WidthTextField.getText().toString();
		final float widthC = Float.parseFloat(widthS);
	//	System.out.println(widthC);
		
	//	controller.setLength(widthC);			
		
		HeightTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, HeightTextField, 0, SpringLayout.NORTH, WidthTextField);
		HeightTextField.setText("100.0");
		getContentPane().add(HeightTextField);
		HeightTextField.setColumns(10);
		
		String heightS = HeightTextField.getText().toString();
		final float heightC = Float.parseFloat(heightS);
	//	System.out.println(heightC);
		
	//	controller.setHeight(heightC);	
		
		JLabel x = new JLabel("X");
		springLayout.putConstraint(SpringLayout.NORTH, x, 79, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.SOUTH, x, -412, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, HeightTextField, 38, SpringLayout.EAST, x);
		springLayout.putConstraint(SpringLayout.WEST, x, 6, SpringLayout.EAST, WidthTextField);
		springLayout.putConstraint(SpringLayout.EAST, x, -734, SpringLayout.EAST, getContentPane());
		getContentPane().add(x);
		
		JLabel lblAddInGender = new JLabel("add in gender, age later on.");
		springLayout.putConstraint(SpringLayout.EAST, lblAddInGender, 0, SpringLayout.EAST, lblSizewidthX);
		getContentPane().add(lblAddInGender);
		
		JButton btnGenerateFish = new JButton("Generate Fish");
		springLayout.putConstraint(SpringLayout.NORTH, lblAddInGender, 0, SpringLayout.NORTH, btnGenerateFish);
		springLayout.putConstraint(SpringLayout.WEST, btnGenerateFish, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(btnGenerateFish);
		btnGenerateFish.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
			String cichlidNameT = NameTextField.getText().toString();
			controller.setName(cichlidNameT);
			String weightS = WeightTextField.getText().toString();
			float weightC = Float.parseFloat(weightS);
			controller.setWeight(weightC);
			String widthS = WidthTextField.getText().toString();
			float widthC = Float.parseFloat(widthS);
			controller.setLength(widthC);
			String heightS = HeightTextField.getText().toString();
			float heightC = Float.parseFloat(heightS);
			controller.setHeight(heightC);	
			
			controller.updateView();
			}
		});
		
		
		JTextArea outputData = new JTextArea();
		springLayout.putConstraint(SpringLayout.SOUTH, btnGenerateFish, -31, SpringLayout.NORTH, outputData);
		springLayout.putConstraint(SpringLayout.NORTH, outputData, 288, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, outputData, 51, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, outputData, -84, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, outputData, -587, SpringLayout.EAST, getContentPane());
		getContentPane().add(outputData);
		
		JLabel lblTestingoutputData = new JLabel("testingOutput Data");
		springLayout.putConstraint(SpringLayout.SOUTH, lblTestingoutputData, -17, SpringLayout.NORTH, outputData);
		springLayout.putConstraint(SpringLayout.EAST, lblTestingoutputData, 0, SpringLayout.EAST, lblSizewidthX);
		getContentPane().add(lblTestingoutputData);
		
		JSlider waterTemperatureSlider = new JSlider();
		springLayout.putConstraint(SpringLayout.NORTH, waterTemperatureSlider, 6, SpringLayout.SOUTH, x);
		springLayout.putConstraint(SpringLayout.EAST, waterTemperatureSlider, 0, SpringLayout.EAST, HeightTextField);
		getContentPane().add(waterTemperatureSlider);
		
		JLabel lblWaterTemperaturecelsius = new JLabel("Water Temperature (Celsius)");
		springLayout.putConstraint(SpringLayout.NORTH, lblWaterTemperaturecelsius, 27, SpringLayout.SOUTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.WEST, lblWaterTemperaturecelsius, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblWaterTemperaturecelsius);
		
	//	controller.updateView(); // data is updated
		
		

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