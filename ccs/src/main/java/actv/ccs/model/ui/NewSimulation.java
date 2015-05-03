package actv.ccs.model.ui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBaseBuilder;
import actv.ccs.model.*;
import actv.ccs.model.type.FishState;
import actv.ccs.model.TankObject;
import actv.ccs.sageTest.FishTank;
import actv.ccs.sageTest.FishTankImpl;
import actv.ccs.sageTest.MyGame;
import actv.ccs.sageTest.TestGame;

public class NewSimulation extends JFrame {
	private String cichlidNameA;
	private FishTank tank;
	private String cichlidNameZ, objectNameA;
	private MyGame myGame;
	private TestGame tg;
	
	private JTextField NameTextField;
	private JTextField WeightTextField;
	private JTextField WidthTextField;
	private JTextField HeightTextField;
	private JTextArea outputData;
	
	private ResultSet rs;
	
	private int tankFishCount;
	private int tankPlantCount;
	private int i = 0; // figure out where to add the item
	private JTextField tankWidthField;
	private JTextField tankLengthField;
	private JTextField tankHeightField;
	
	// need to fix logger
 	private static Logger logger = LoggerFactory.getLogger(NewSimulation.class);
 	private JTextField genderTextField;
 	private JTextField aggroLevelTextField;
 	private JTextField objectNameTextField;
 	private JTextField objectTypeTextField;
 	private JTextField objectWidthTextField;
 	private JTextField objectHeightTextField;
 	private JTextField objectLengthTextField;
 	
 	
/* updates are from latest to oldest (top to bottom)
 * 
 * 3-4-15
 * form is done. just need to make checks and flags and resize the width by height window
 * 
 * 3-4-15
 * need to do a button listener later on etc fish stuff.
 */
	
	public NewSimulation(MyGame mg) throws SecurityException, IOException
	{
		
		myGame = mg;
		//tank = new TankObject(20, 20, 20, 26, 0, 0, 0); // array value default tank
		tank = new FishTankImpl();
		
		tankFishCount = tank.getCichlidCount();
		tankPlantCount = tank.getCichlidCount();
		

		setTitle("Convict Cichlid Fish Simulator New Simulation Test");
		setSize(1000,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
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
		
		JComboBox comboBox = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.EAST, comboBox, -587, SpringLayout.EAST, getContentPane());
		getContentPane().add(comboBox);
		
		/*//http://stackoverflow.com/questions/17887927/adding-items-to-a-jcombobox
		
		class ComboItem
		{
		    private String key;
		    private String value;

		    public ComboItem(String key, String value)
		    {
		        this.key = key;
		        this.value = value;
		    }

		    @Override
		    public String toString()
		    {
		        return key;
		    }

		    public String getKey()
		    {
		        return key;
		    }

		    public String getValue()
		    {
		        return value;
		    }
		}*/
		
		Connection conn;
		comboBox.addItem("");
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
	
		Statement s = conn.createStatement();
		rs = s.executeQuery("SELECT * FROM [FishPool]");
		while (rs.next())
		{
			String name = rs.getString("Type"); //Field from database ex. FishA, FishB
        	//String value = rs.getString((1)); 
        //ComboItem comboItem = new ComboItem(name, value); 
        comboBox.addItem(name); 
		}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// find a way to get the database merger here
		
		
		// then it would put in the values for the fish and make the fields unchangeable for the time being. later on, will do ... do you want to add more?
		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					// show fish values
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				String selectedFish = (String) combo.getSelectedItem();
				
				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				Statement s = conn.createStatement();
				if (selectedFish.equals("Fish A"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
				}
				else if (selectedFish.equals("Fish B"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");
				}
				else if (selectedFish.equals("Fish C"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");
				}
				while (rs.next())
				{
					String id = rs.getString("ID"); // added new string for ID
					String name = rs.getString("Type"); //Field from database ex. FishA, FishB
		        	String weight = rs.getString("Weight");
		        	String width = rs.getString("Width");
		        	String height = rs.getString("Height");
		        	String gender = rs.getString("Gender");
		        	String aggro = rs.getString("AggroLevel"); //default to 10
		        	

		        	NameTextField.setText(name);
					String cichlidNameT = NameTextField.getText().toString();

					
				    WeightTextField.setText(weight);
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);

					
					WidthTextField.setText(width);
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);

					HeightTextField.setText(height);
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);

					
					genderTextField.setText(gender);
					String genderS = genderTextField.getText().toString();
					
					aggroLevelTextField.setText(aggro);
					String aggroS = aggroLevelTextField.getText().toString();
					float aggroC = Float.parseFloat(aggroS);
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					genderTextField.setEditable(false);
					
						
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
				
			}
		});
		
		JLabel lblCichlidName = new JLabel("Cichlid Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidName, 45, SpringLayout.SOUTH, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidName, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblCichlidName);
//		cichlidNameZ = NameTextField.getText().toString();
		
 		JLabel lblWeightkg = new JLabel("Weight (kg): ");
		springLayout.putConstraint(SpringLayout.NORTH, lblWeightkg, 6, SpringLayout.SOUTH, lblCichlidName);
		springLayout.putConstraint(SpringLayout.WEST, lblWeightkg, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblWeightkg);
		
		JLabel lblSizewidthX = new JLabel("Size (Width x Height ) (cm):");
		springLayout.putConstraint(SpringLayout.NORTH, lblSizewidthX, 6, SpringLayout.SOUTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, lblSizewidthX, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblSizewidthX);
		
		NameTextField = new JTextField();
		NameTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, NameTextField, 37, SpringLayout.SOUTH, comboBox);
		NameTextField.setText("");
		springLayout.putConstraint(SpringLayout.WEST, NameTextField, 75, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(NameTextField);
		NameTextField.setColumns(10);
		cichlidNameA = NameTextField.getText().toString();
		
		WeightTextField = new JTextField();
		WeightTextField.setEditable(false);
		WeightTextField.setText("");
		springLayout.putConstraint(SpringLayout.NORTH, WeightTextField, 0, SpringLayout.NORTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, WeightTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(WeightTextField);
		WeightTextField.setColumns(10);
		String weightS = WeightTextField.getText().toString();
//		final float weightC = Float.parseFloat(weightS);
		
		WidthTextField = new JTextField();
		WidthTextField.setEditable(false);
		WidthTextField.setText("");
		springLayout.putConstraint(SpringLayout.NORTH, WidthTextField, 0, SpringLayout.NORTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.EAST, WidthTextField, 0, SpringLayout.EAST, NameTextField);
		getContentPane().add(WidthTextField);
		WidthTextField.setColumns(10);
		String widthS = WidthTextField.getText().toString();
//		final float widthC = Float.parseFloat(widthS);
		
		HeightTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, HeightTextField, 0, SpringLayout.NORTH, lblSizewidthX);
		HeightTextField.setEditable(false);
		HeightTextField.setText("");
		getContentPane().add(HeightTextField);
		HeightTextField.setColumns(10);
		
		String heightS = HeightTextField.getText().toString();
	//	final float heightC = Float.parseFloat(heightS);
		
		JLabel x = new JLabel("X");
		springLayout.putConstraint(SpringLayout.WEST, HeightTextField, 16, SpringLayout.EAST, x);
		springLayout.putConstraint(SpringLayout.NORTH, x, 79, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.SOUTH, x, -412, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, x, 6, SpringLayout.EAST, WidthTextField);
		springLayout.putConstraint(SpringLayout.EAST, x, -734, SpringLayout.EAST, getContentPane());
		getContentPane().add(x);
		
		JLabel Genderlbl = new JLabel("Gender");
		springLayout.putConstraint(SpringLayout.NORTH, Genderlbl, 16, SpringLayout.SOUTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.WEST, Genderlbl, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(Genderlbl);
		
		JButton btnGenerateFish = new JButton("Generate Fish");
		springLayout.putConstraint(SpringLayout.WEST, btnGenerateFish, 165, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnGenerateFish);
		btnGenerateFish.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		

				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				Statement s = conn.createStatement();
				cichlidNameZ = NameTextField.getText().toString();

				if (cichlidNameZ.equals("Fish A"))
				{
					rs = s.executeQuery("SELECT ID FROM [FishPool] WHERE Type='Fish A'");
		        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
		        	tankFishCount++;
				}
				else if (cichlidNameZ.equals("Fish B"))
				{
					rs = s.executeQuery("SELECT ID FROM [FishPool] WHERE Type='Fish B'");
		        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
		        	tankFishCount++;
				}
				else if (cichlidNameZ.equals("Fish C"))
				{
					rs = s.executeQuery("SELECT ID FROM [FishPool] WHERE Type='Fish C'");
		        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 3 where ID = 3");
		        	tankFishCount++;
				}
				while (rs.next())
				{
					String id = rs.getString("ID"); // added new string for ID
				
		        	int fishIDc = Integer.parseInt(id);
		        	System.out.println("arr: " + i + " ID: " + id);
		        	i++;
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			outputData.setText("added fish to tank");

			}
		});
		
		
		outputData = new JTextArea();
		springLayout.putConstraint(SpringLayout.SOUTH, outputData, -32, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, outputData, -10, SpringLayout.EAST, getContentPane());
		outputData.setWrapStyleWord(true);
		outputData.setLineWrap(true);
		getContentPane().add(outputData);
		outputData.setEditable(false);
		
		JLabel lblTestingoutputData = new JLabel("testingOutput Data");
		springLayout.putConstraint(SpringLayout.WEST, lblTestingoutputData, 0, SpringLayout.WEST, outputData);
		springLayout.putConstraint(SpringLayout.SOUTH, lblTestingoutputData, -19, SpringLayout.NORTH, outputData);
		getContentPane().add(lblTestingoutputData);
		
		JSlider waterTemperatureSlider = new JSlider();
		getContentPane().add(waterTemperatureSlider);
		waterTemperatureSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				
				JSlider slider = (JSlider) e.getSource();
				float value = slider.getValue();
				
				if (!slider.getValueIsAdjusting())
				{
					
					tank.setTemperature(value);
					System.out.println("value: " + value);
					System.out.println("the value is changing " + tank.getTemperature());
				}
			}
		});
		
		JLabel lblWaterTemperaturecelsius = new JLabel("Water Temperature (Celsius)");
		springLayout.putConstraint(SpringLayout.WEST, waterTemperatureSlider, 29, SpringLayout.EAST, lblWaterTemperaturecelsius);
		springLayout.putConstraint(SpringLayout.WEST, lblWaterTemperaturecelsius, 6, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, waterTemperatureSlider, 0, SpringLayout.SOUTH, lblWaterTemperaturecelsius);
		getContentPane().add(lblWaterTemperaturecelsius);
		
		JLabel lblTankSpecifications = new JLabel("Tank Specifications:");
		springLayout.putConstraint(SpringLayout.NORTH, lblTankSpecifications, 63, SpringLayout.SOUTH, Genderlbl);
		springLayout.putConstraint(SpringLayout.WEST, lblTankSpecifications, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTankSpecifications);
		
		JLabel lblTankWidth = new JLabel("Tank Width:");
		springLayout.putConstraint(SpringLayout.NORTH, lblTankWidth, 13, SpringLayout.SOUTH, lblTankSpecifications);
		springLayout.putConstraint(SpringLayout.WEST, lblTankWidth, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTankWidth);
		
		JLabel lblTankLength = new JLabel("Tank Length: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblTankLength, 21, SpringLayout.SOUTH, lblTankWidth);
		springLayout.putConstraint(SpringLayout.EAST, lblTankLength, 0, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(lblTankLength);
		
		JLabel lblTankHeight = new JLabel("Tank Height: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblWaterTemperaturecelsius, 23, SpringLayout.SOUTH, lblTankHeight);
		springLayout.putConstraint(SpringLayout.NORTH, lblTankHeight, 20, SpringLayout.SOUTH, lblTankLength);
		springLayout.putConstraint(SpringLayout.EAST, lblTankHeight, 0, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(lblTankHeight);
		
		tankWidthField = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, btnGenerateFish, -33, SpringLayout.NORTH, tankWidthField);
		tankWidthField.setText("20.0");
		tankWidthField.setEditable(false);
		springLayout.putConstraint(SpringLayout.WEST, tankWidthField, 14, SpringLayout.EAST, lblTankWidth);
		springLayout.putConstraint(SpringLayout.NORTH, tankWidthField, 0, SpringLayout.NORTH, lblTankWidth);
		getContentPane().add(tankWidthField);
		tankWidthField.setColumns(10);
		String tankWidthS = tankWidthField.getText().toString();
		final float tankWidthC = Float.parseFloat(tankWidthS);
		
		tankLengthField = new JTextField();
		tankLengthField.setText("20.0");
		tankLengthField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, tankLengthField, -3, SpringLayout.NORTH, lblTankLength);
		springLayout.putConstraint(SpringLayout.WEST, tankLengthField, 6, SpringLayout.EAST, lblTankLength);
		getContentPane().add(tankLengthField);
		tankLengthField.setColumns(10);
		String tankLengthS = tankLengthField.getText().toString();
		final float tankLengthC = Float.parseFloat(tankLengthS);
		
		
		tankHeightField = new JTextField();
		tankHeightField.setText("20.0");
		tankHeightField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, tankHeightField, 16, SpringLayout.SOUTH, tankLengthField);
		springLayout.putConstraint(SpringLayout.WEST, tankHeightField, 6, SpringLayout.EAST, lblTankHeight);
		getContentPane().add(tankHeightField);
		tankHeightField.setColumns(10);
		String tankHeightS = tankHeightField.getText().toString();
		final float tankHeightC = Float.parseFloat(tankHeightS);
		
		JButton btnRunSimulation = new JButton("Run Simulation");
		springLayout.putConstraint(SpringLayout.NORTH, outputData, -1, SpringLayout.NORTH, btnRunSimulation);
		springLayout.putConstraint(SpringLayout.WEST, outputData, 538, SpringLayout.EAST, btnRunSimulation);
		springLayout.putConstraint(SpringLayout.WEST, btnRunSimulation, 0, SpringLayout.WEST, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.SOUTH, btnRunSimulation, -59, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(btnRunSimulation);
		
		genderTextField = new JTextField();
		genderTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.SOUTH, genderTextField, 0, SpringLayout.SOUTH, Genderlbl);
		springLayout.putConstraint(SpringLayout.EAST, genderTextField, 0, SpringLayout.EAST, NameTextField);
		getContentPane().add(genderTextField);
		genderTextField.setColumns(10);
		
		JLabel lblAggroLevel = new JLabel("Aggro Level");
		springLayout.putConstraint(SpringLayout.WEST, lblAggroLevel, 0, SpringLayout.WEST, lblPleasePickA);
		getContentPane().add(lblAggroLevel);
		
		aggroLevelTextField = new JTextField();
		aggroLevelTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.SOUTH, lblAggroLevel, 0, SpringLayout.SOUTH, aggroLevelTextField);
		springLayout.putConstraint(SpringLayout.NORTH, aggroLevelTextField, 6, SpringLayout.SOUTH, genderTextField);
		springLayout.putConstraint(SpringLayout.WEST, aggroLevelTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(aggroLevelTextField);
		aggroLevelTextField.setColumns(10);
		
		JLabel lblSimulationRunTime = new JLabel("Simulation Run Time (seconds):");
		springLayout.putConstraint(SpringLayout.NORTH, lblSimulationRunTime, 19, SpringLayout.SOUTH, lblWaterTemperaturecelsius);
		springLayout.putConstraint(SpringLayout.WEST, lblSimulationRunTime, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblSimulationRunTime);
		
		JSlider timerSlider = new JSlider();
		springLayout.putConstraint(SpringLayout.SOUTH, timerSlider, 0, SpringLayout.SOUTH, lblSimulationRunTime);
		springLayout.putConstraint(SpringLayout.EAST, timerSlider, 0, SpringLayout.EAST, waterTemperatureSlider);
		timerSlider.setValue(100);
		timerSlider.setMaximum(1000);
		timerSlider.setMinimum(1);
		getContentPane().add(timerSlider);
		
		JLabel lblScenarioStuffGoes = new JLabel("Scenario Stuff goes here ...");
		springLayout.putConstraint(SpringLayout.SOUTH, lblScenarioStuffGoes, 0, SpringLayout.SOUTH, waterTemperatureSlider);
		springLayout.putConstraint(SpringLayout.EAST, lblScenarioStuffGoes, -201, SpringLayout.EAST, getContentPane());
		getContentPane().add(lblScenarioStuffGoes);
		
		JLabel lblPleasePickA_1 = new JLabel("Please pick a plant or object to put into the tank: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblPleasePickA_1, 0, SpringLayout.NORTH, label);
		springLayout.putConstraint(SpringLayout.WEST, lblPleasePickA_1, 37, SpringLayout.EAST, comboBox);
		getContentPane().add(lblPleasePickA_1);
		
		JComboBox objects_comboBox = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, objects_comboBox, 0, SpringLayout.NORTH, label);
		springLayout.putConstraint(SpringLayout.WEST, objects_comboBox, 28, SpringLayout.EAST, lblPleasePickA_1);
		springLayout.putConstraint(SpringLayout.EAST, objects_comboBox, -55, SpringLayout.EAST, getContentPane());
		getContentPane().add(objects_comboBox);
		
		Connection connn;
		objects_comboBox.addItem("");
		
		JLabel lblObjectName = new JLabel("Object Name:");
		springLayout.putConstraint(SpringLayout.NORTH, lblObjectName, 0, SpringLayout.NORTH, lblCichlidName);
		springLayout.putConstraint(SpringLayout.WEST, lblObjectName, 270, SpringLayout.EAST, NameTextField);
		getContentPane().add(lblObjectName);
		
		JLabel lblType = new JLabel(" Type:");
		springLayout.putConstraint(SpringLayout.NORTH, lblType, 6, SpringLayout.SOUTH, lblObjectName);
		springLayout.putConstraint(SpringLayout.WEST, lblType, 270, SpringLayout.EAST, WeightTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, lblType, 20, SpringLayout.SOUTH, lblObjectName);
		getContentPane().add(lblType);
		
		JLabel lblWidth = new JLabel("Width:");
		springLayout.putConstraint(SpringLayout.NORTH, lblWidth, 3, SpringLayout.NORTH, genderTextField);
		springLayout.putConstraint(SpringLayout.WEST, lblWidth, 0, SpringLayout.WEST, lblObjectName);
		getContentPane().add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height:");
		springLayout.putConstraint(SpringLayout.NORTH, lblHeight, 3, SpringLayout.NORTH, aggroLevelTextField);
		springLayout.putConstraint(SpringLayout.WEST, lblHeight, 0, SpringLayout.WEST, lblObjectName);
		getContentPane().add(lblHeight);
		
		JButton btnGenerateObject = new JButton("Generate Object");
		springLayout.putConstraint(SpringLayout.NORTH, btnGenerateObject, 0, SpringLayout.NORTH, btnGenerateFish);
		springLayout.putConstraint(SpringLayout.WEST, btnGenerateObject, 242, SpringLayout.EAST, btnGenerateFish);
		getContentPane().add(btnGenerateObject);
		btnGenerateObject.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		

				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				Statement s = conn.createStatement();
				objectNameA = objectNameTextField.getText().toString();

				if (objectNameA.equals("Large Plant"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Large Plant'");
		        	int a = s.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
		        	tankPlantCount++;
				}
				else if (objectNameA.equals("Medium Plant"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Medium Plant'");
		        	int b = s.executeUpdate("UPDATE SimulationObjects set objID = 2 where ID = 2");
		        	tankPlantCount++;
				}
				else if (objectNameA.equals("Small Plant"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Small Plant'");
		        	int c = s.executeUpdate("UPDATE SimulationObjects set objID = 3 where ID = 3");
		        	tankPlantCount++;
				}
				else if (objectNameA.equals("Large Pot"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Large Pot'");
		        	int c = s.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
		        	tankPlantCount++;
				}
				else if (objectNameA.equals("Medium Pot"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Medium Pot'");
		        	int c = s.executeUpdate("UPDATE SimulationObjects set objID = 5 where ID = 5");
		        	tankPlantCount++;
				}
				else if (objectNameA.equals("Small Pot"))
				{
					rs = s.executeQuery("SELECT ID FROM [Objects] WHERE Name='Small Pot'");
		        	int c = s.executeUpdate("UPDATE SimulationObjects set objID = 6 where ID = 6");
		        	tankPlantCount++;
				}
				while (rs.next())
				{
					String id = rs.getString("ID"); // added new string for ID
				
		        	int objID = Integer.parseInt(id);
		        	System.out.println("obj arr: " + i + " ID: " + id);
		        	i++;
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			outputData.setText("added object to tank");

			}
		});
		
		objectNameTextField = new JTextField();
		objectNameTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, objectNameTextField, 39, SpringLayout.SOUTH, lblPleasePickA_1);
		springLayout.putConstraint(SpringLayout.WEST, objectNameTextField, 20, SpringLayout.EAST, lblObjectName);
		springLayout.putConstraint(SpringLayout.EAST, objectNameTextField, 119, SpringLayout.EAST, lblObjectName);
		getContentPane().add(objectNameTextField);
		objectNameTextField.setColumns(10);
		
		objectTypeTextField = new JTextField();
		objectTypeTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, objectTypeTextField, -3, SpringLayout.NORTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, objectTypeTextField, 0, SpringLayout.WEST, objectNameTextField);
		getContentPane().add(objectTypeTextField);
		objectTypeTextField.setColumns(10);
		
		objectWidthTextField = new JTextField();
		objectWidthTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, objectWidthTextField, -3, SpringLayout.NORTH, Genderlbl);
		springLayout.putConstraint(SpringLayout.WEST, objectWidthTextField, 0, SpringLayout.WEST, objectNameTextField);
		getContentPane().add(objectWidthTextField);
		objectWidthTextField.setColumns(10);
		
		objectHeightTextField = new JTextField();
		objectHeightTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, objectHeightTextField, 0, SpringLayout.NORTH, aggroLevelTextField);
		springLayout.putConstraint(SpringLayout.WEST, objectHeightTextField, 0, SpringLayout.WEST, objectNameTextField);
		getContentPane().add(objectHeightTextField);
		objectHeightTextField.setColumns(10);
		
		JLabel lblLength = new JLabel("Length:");
		springLayout.putConstraint(SpringLayout.NORTH, lblLength, 0, SpringLayout.NORTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.WEST, lblLength, 0, SpringLayout.WEST, lblObjectName);
		getContentPane().add(lblLength);
		
		objectLengthTextField = new JTextField();
		objectLengthTextField.setEditable(false);
		springLayout.putConstraint(SpringLayout.WEST, objectLengthTextField, 0, SpringLayout.WEST, objectNameTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, objectLengthTextField, 0, SpringLayout.SOUTH, WidthTextField);
		getContentPane().add(objectLengthTextField);
		objectLengthTextField.setColumns(10);
		try {
			connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
	
		Statement s = connn.createStatement();
		rs = s.executeQuery("SELECT * FROM [Objects]");
		while (rs.next())
		{
			String name = rs.getString("Name"); //Field from database ex. FishA, FishB
        	//String value = rs.getString((1)); 
        //ComboItem comboItem = new ComboItem(name, value); 
        objects_comboBox.addItem(name); 
		}
		connn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objects_comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					// show fish values
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				String selectedObjects = (String) combo.getSelectedItem();
				
				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				Statement s = conn.createStatement();
				if (selectedObjects.equals("Large Plant"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Plant'");
				}
				else if (selectedObjects.equals("Medium Plant"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Plant'");
				}
				else if (selectedObjects.equals("Small Plant"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Plant'");
				}
				else if (selectedObjects.equals("Large Pot"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Pot'");
				}
				else if (selectedObjects.equals("Medium Pot"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Pot'");
				}
				else if (selectedObjects.equals("Small Pot"))
				{
					rs = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Pot'");
				}
				while (rs.next())
				{
					String id = rs.getString("ID"); // added new string for ID
					String name = rs.getString("Name"); //Field from database ex. FishA, FishB
					String type = rs.getString("Type");
					String length = rs.getString("Length");
					String width = rs.getString("Width");
		        	String height = rs.getString("Height");
	

		        	objectNameTextField.setText(name);
			
					objectTypeTextField.setText(type);
			
				    objectLengthTextField.setText(length);
				
					objectWidthTextField.setText(width);
				
					objectHeightTextField.setText(height);
				

					
					
			//		controller.updateView();		
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
				
			}
		});
		timerSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				
				JSlider slider = (JSlider) e.getSource();
				int value = slider.getValue();
				
				if (!slider.getValueIsAdjusting())
				{
					
					tank.setTimer(value);
					System.out.println("value: " + value);
					System.out.println("the value is changing " + tank.getTimer());
				}
			}
		});
		
		
		btnRunSimulation.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				logger.info("Tank Information");
				tank.setCichlidCount(tankFishCount);
				tank.setObjectCount(0);
				System.out.println("time: " + tank.getTimer());
				System.out.println("Running Simulation");
				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
			
				Statement s = conn.createStatement();
				rs = s.executeQuery("SELECT ID FROM [TankData]");
				while (rs.next())
				{
					int a = s.executeUpdate("UPDATE TankData set Length = " + FishTank.DEPTH + " where ID = 1");
					int b = s.executeUpdate("UPDATE TankData set Width = " + FishTank.WIDTH + " where ID = 1");
					int c = s.executeUpdate("UPDATE TankData set Height = " + FishTank.HEIGHT + " where ID = 1");
					int d = s.executeUpdate("UPDATE TankData set Temperature = " + tank.getTemperature() + " where ID = 1");
					int z = s.executeUpdate("UPDATE TankData set cichlidCount = " + tank.getCichlidCount() + " where ID = 1");
					//TODO: update db table
					int f = s.executeUpdate("UPDATE TankData set plantCount = " + tank.getObjectCount() + " where ID = 1");
					int g = s.executeUpdate("UPDATE TankData set Time = " + tank.getTimer() + " where ID = 1");
						
				}
				conn.close();
				} catch (SQLException Ex) {
					// TODO Auto-generated catch block
					Ex.printStackTrace();
				}

				
			//	try {
				//	rS = new RunSimulation();
					System.out.println("trying a swap to 3d3d3d3d");
		//		} catch (IOException e1) {
			//		// TODO Auto-generated catch block
			//		e1.printStackTrace();
		//		}
			//	new MyGame().start();
				
				CloseJFrame();
				// +Exception in thread "AWT-EventQueue-0" java.lang.RuntimeException: Unable to create display
			}
		});
		
		this.setVisible(true);	
	}
	

	private JMenuBar createJMenu() { 
		// creating menubar
		JMenuBar bar = new JMenuBar();
		// initializing commands
		// File
		JMenu file = new JMenu("File");
		// sub testing
		JMenuItem mItem1 = new JMenuItem("exit");
		file.add(mItem1);
		bar.add(file);
		// Edit
		// Save
		// Load
		JMenu load = new JMenu("Load");
		JMenuItem mItem4 = new JMenuItem("Load a previous simulation");
		/*
		 * will have to ask later on how you guys want to load the data, is there a special format on how to read the file data?
		 */
		load.add(mItem4);
		bar.add(load);
		// Help
		JMenu help = new JMenu("Help");
		JMenuItem mItem5 = new JMenuItem("Manual");
		help.add(mItem5);
		bar.add(help);
		// About
		JMenu about = new JMenu("About");
		JMenuItem mItem6 = new JMenuItem("Read This!");
		about.add(mItem6);		
		bar.add(about);
		
		return bar;
	}
	
	public void CloseJFrame()
	{
		super.dispose();
		myGame.start();

	}
}
