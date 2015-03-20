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
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import org.apache.commons.lang.ArrayUtils;

import actv.ccs.model.*;
import actv.ccs.model.type.FishState;
import actv.ccs.model.TankObject;

public class NewSimulation extends JFrame {
	
	private ConvictCichlid cichlid;
	private TankObject tank;
	private String cichlidNameZ;
	private SimulationWorld world;
	private ConvictCichlidController controller;
	private RunSimulation rS;
	
	private JTextField NameTextField;
	private JTextField WeightTextField;
	private JTextField WidthTextField;
	private JTextField HeightTextField;
	private JTextArea outputData;
	
	private ResultSet rs;
	
	private int tankFishCount;
	private int tankPlantCount;
	private int fishID = 0;
	private int fishIDList [];
	private int i = 0; // figure out where to add the item
	private String [] poolOfFish;
	private String cichlidNameA;
	private String cichlidNameB;
	private JTextField tankWidthField;
	private JTextField tankLengthField;
	private JTextField tankHeightField;
	
	// need to fix logger
 	private static Logger logger = Logger.getLogger("LoggingToFile");
 	private JTextField genderTextField;
 	private JTextField aggroLevelTextField;
 	
/* updates are from latest to oldest (top to bottom)
 * 
 * 3-4-15
 * form is done. just need to make checks and flags and resize the width by height window
 * 
 * 3-4-15
 * need to do a button listener later on etc fish stuff.
 */
	
	public NewSimulation() throws SecurityException, IOException
	{
		
		cichlid = new ConvictCichlid();
		tank = new TankObject(20, 20, 20, 26, 0, 0); // array value default tank
		world = new SimulationWorld();
		cichlid = getFromDB();
		
		// adding new int
		fishIDList = new int [3];
		
		FileHandler logFile = new FileHandler("Log2File.txt");
		logFile.setFormatter(new SimpleFormatter());
		logger.addHandler(logFile);
		logger.info("Starting logging data");
		
		tankFishCount = tank.getCichlidCount();
		tankPlantCount = tank.getCichlidCount();
		
		controller = new ConvictCichlidController(cichlid, world);
		
		setTitle("Convict Cichlid Fish Simulator New Simulation Test");
		setSize(1000,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
			conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
	
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
					conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
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
					controller.setName(cichlidNameT);
					
				    WeightTextField.setText(weight);
					String weightS = WeightTextField.getText().toString();
					float weightC = Float.parseFloat(weightS);
					controller.setWeight(weightC);
					
					WidthTextField.setText(width);
					String widthS = WidthTextField.getText().toString();
					float widthC = Float.parseFloat(widthS);
					controller.setLength(widthC);
					
					HeightTextField.setText(height);
					String heightS = HeightTextField.getText().toString();
					float heightC = Float.parseFloat(heightS);
					controller.setHeight(heightC);
					
					genderTextField.setText(gender);
					String genderS = genderTextField.getText().toString();
					controller.setGender(genderS);
					
					aggroLevelTextField.setText(aggro);
					String aggroS = aggroLevelTextField.getText().toString();
					float aggroC = Float.parseFloat(aggroS);
					controller.setAggroLevel(aggroC);
					
					NameTextField.setEditable(false);
					WeightTextField.setEditable(false);
					WidthTextField.setEditable(false);
					HeightTextField.setEditable(false);
					genderTextField.setEditable(false);
					
					
			//		controller.updateView();		
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//if (selectedFish.equals("Stringer Bell"))
				//{
					//NameTextField.setText("Stringer Bell");
					//String cichlidNameT = NameTextField.getText().toString();
					//controller.setName(cichlidNameT);
					
					
					//WeightTextField.setText("10.0");
					//String weightS = WeightTextField.getText().toString();
					//float weightC = Float.parseFloat(weightS);
					//controller.setWeight(weightC);
					
					//WidthTextField.setText("1.0");
					//String widthS = WidthTextField.getText().toString();
					//float widthC = Float.parseFloat(widthS);
					//controller.setLength(widthC);
					
					//HeightTextField.setText("3.0");
					//String heightS = HeightTextField.getText().toString();
					//float heightC = Float.parseFloat(heightS);
					//controller.setHeight(heightC);	
					
					//NameTextField.setEditable(false);
					//WeightTextField.setEditable(false);
					//WidthTextField.setEditable(false);
					//HeightTextField.setEditable(false);
					
					//controller.updateView();					
					
				//}
				/*else if (selectedFish.equals("Marlo Stanfield"))
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
				}*/
				
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
		NameTextField.setText("");
		springLayout.putConstraint(SpringLayout.NORTH, NameTextField, 6, SpringLayout.SOUTH, lblIfNot);
		springLayout.putConstraint(SpringLayout.WEST, NameTextField, 75, SpringLayout.EAST, lblCichlidName);
		getContentPane().add(NameTextField);
		NameTextField.setColumns(10);
		cichlidNameA = NameTextField.getText().toString();
		
		WeightTextField = new JTextField();
		WeightTextField.setText("");
		springLayout.putConstraint(SpringLayout.NORTH, WeightTextField, 0, SpringLayout.NORTH, lblWeightkg);
		springLayout.putConstraint(SpringLayout.WEST, WeightTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(WeightTextField);
		WeightTextField.setColumns(10);
		String weightS = WeightTextField.getText().toString();
//		final float weightC = Float.parseFloat(weightS);
		
		WidthTextField = new JTextField();
		WidthTextField.setText("");
		springLayout.putConstraint(SpringLayout.NORTH, WidthTextField, 0, SpringLayout.NORTH, lblSizewidthX);
		springLayout.putConstraint(SpringLayout.EAST, WidthTextField, 0, SpringLayout.EAST, NameTextField);
		getContentPane().add(WidthTextField);
		WidthTextField.setColumns(10);
		String widthS = WidthTextField.getText().toString();
//		final float widthC = Float.parseFloat(widthS);
		
		HeightTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, HeightTextField, 0, SpringLayout.NORTH, WidthTextField);
		HeightTextField.setText("");
		getContentPane().add(HeightTextField);
		HeightTextField.setColumns(10);
		
		String heightS = HeightTextField.getText().toString();
	//	final float heightC = Float.parseFloat(heightS);
		
		JLabel x = new JLabel("X");
		springLayout.putConstraint(SpringLayout.NORTH, x, 79, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.SOUTH, x, -412, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, HeightTextField, 38, SpringLayout.EAST, x);
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
		
/*			fishID++;	
			controller.setID(fishID);	
			String cichlidNameT = NameTextField.getText().toString();
			controller.setName(cichlidNameT);
			logger.info("ID #" + fishID + " Name:" + cichlidNameT);
			String weightS = WeightTextField.getText().toString();
			float weightC = Float.parseFloat(weightS);
			controller.setWeight(weightC);
			logger.info("ID #" + fishID + " weight:" + weightC);
			String widthS = WidthTextField.getText().toString();
			float widthC = Float.parseFloat(widthS);
			controller.setLength(widthC);
			logger.info("ID #" + fishID + " width:" + widthC);
			String heightS = HeightTextField.getText().toString();
			float heightC = Float.parseFloat(heightS);
			controller.setHeight(heightC);	
			logger.info("ID #" + fishID + " height:" + heightC);
			
			logger.info("ID #" + fishID + " baseAggro:" + controller.getAggro());
			
			
		
			cichlidNameB = controller.getName();
	*/	

				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
				Statement s = conn.createStatement();
				cichlidNameZ = NameTextField.getText().toString();

				if (cichlidNameZ.equals("Fish A"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
				}
				else if (cichlidNameZ.equals("Fish B"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");
				}
				else if (cichlidNameZ.equals("Fish C"))
				{
					rs = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");
				}
				while (rs.next())
				{
					String id = rs.getString("ID"); // added new string for ID
				
		        	int fishIDc = Integer.parseInt(id);
			        	
		        		fishIDList = ArrayUtils.add(fishIDList, i, fishIDc);
		        		System.out.println("arr: " + i + " ID: " + fishIDList[i]);
		        		i++;
		        	
//					controller.updateView();		
				}
				conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	//		controller.updateView();
			tank.setCichlidCount(tankFishCount++);
		
			int tankOutputCichlidTA = tank.getCichlidCount()+1;
			String outputToTextArea = ("added " + cichlidNameB + " to the tank, therefore there are " + tankOutputCichlidTA +
					" convict cichlids in the tank.");
			
			outputData.setText(outputToTextArea);

			}
		});
		
		
		outputData = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, outputData, 279, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, outputData, 616, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, outputData, -59, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, outputData, -22, SpringLayout.EAST, getContentPane());
		outputData.setWrapStyleWord(true);
		outputData.setLineWrap(true);
		getContentPane().add(outputData);
		outputData.setEditable(false);
		
		JLabel lblTestingoutputData = new JLabel("testingOutput Data");
		springLayout.putConstraint(SpringLayout.SOUTH, lblTestingoutputData, -14, SpringLayout.NORTH, outputData);
		springLayout.putConstraint(SpringLayout.EAST, lblTestingoutputData, -291, SpringLayout.EAST, getContentPane());
		getContentPane().add(lblTestingoutputData);
		
		JSlider waterTemperatureSlider = new JSlider();
		springLayout.putConstraint(SpringLayout.EAST, waterTemperatureSlider, 0, SpringLayout.EAST, HeightTextField);
		getContentPane().add(waterTemperatureSlider);
		waterTemperatureSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				
				JSlider slider = (JSlider) e.getSource();
				float value = slider.getValue();
				
				if (!slider.getValueIsAdjusting())
				{
					
					tank.setTankTemperature(value);
					System.out.println("value: " + value);
					System.out.println("the value is changing " + tank.getTankTemperature());
				}
			}
		});
		
		JLabel lblWaterTemperaturecelsius = new JLabel("Water Temperature (Celsius)");
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
		
		
		JLabel lblAddPlantsLater = new JLabel("add Plants later.");
		springLayout.putConstraint(SpringLayout.NORTH, lblAddPlantsLater, 30, SpringLayout.SOUTH, lblWaterTemperaturecelsius);
		springLayout.putConstraint(SpringLayout.WEST, lblAddPlantsLater, 0, SpringLayout.WEST, lblPleasePickA);
		getContentPane().add(lblAddPlantsLater);
		
		JButton btnRunSimulation = new JButton("Run Simulation");
		springLayout.putConstraint(SpringLayout.WEST, btnRunSimulation, 0, SpringLayout.WEST, lblPleasePickA);
		springLayout.putConstraint(SpringLayout.SOUTH, btnRunSimulation, 0, SpringLayout.SOUTH, outputData);
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
		springLayout.putConstraint(SpringLayout.SOUTH, lblAggroLevel, 0, SpringLayout.SOUTH, aggroLevelTextField);
		springLayout.putConstraint(SpringLayout.NORTH, aggroLevelTextField, 6, SpringLayout.SOUTH, genderTextField);
		springLayout.putConstraint(SpringLayout.WEST, aggroLevelTextField, 0, SpringLayout.WEST, NameTextField);
		getContentPane().add(aggroLevelTextField);
		aggroLevelTextField.setColumns(10);
		btnRunSimulation.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				logger.info("Tank Information");
				tank.setCichlidCount(tankFishCount);
				logger.info("Cichlid Count: " + tank.getCichlidCount());
				tank.setPlantCount(0);
				logger.info("Plant Count: " + tank.getPlantCount());
				tank.setTankWidth(tankWidthC);
				logger.info("Tank Width: " + tank.getTankWidth());	
				tank.setTankLength(tankLengthC);
				logger.info("Tank Length: " + tank.getTankLength());
				tank.setTankHeight(tankHeightC);
				logger.info("Tank Temperature: " + tank.getTankTemperature());
				System.out.println("Running Simulation");
				
				try {
					rS = new RunSimulation();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CloseJFrame();
			}
		});
		
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
		c.setCichlidID(0);
		return c;
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
	}
}
