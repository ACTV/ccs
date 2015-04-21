package actv.ccs.model.ui;

import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import actv.ccs.sageTest.MyGame;
import actv.ccs.sageTest.TestGame;

public class SimulationPrompter extends JFrame {
	
	private ResultSet rs; 
	public SimulationPrompter()
	{

		setTitle("Convict Cichlid Fish Simulator Simulation Prompter");
		setSize(1000,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblHelloWelcomeTo = new JLabel("Hello! Welcome to this humble version of a Convict Cichlid Simulator! Please choose your poison!");
		springLayout.putConstraint(SpringLayout.NORTH, lblHelloWelcomeTo, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblHelloWelcomeTo, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblHelloWelcomeTo);
		
		JLabel lblPickAndStart = new JLabel("PICK AND START THY SIMULATION!");
		springLayout.putConstraint(SpringLayout.NORTH, lblPickAndStart, 19, SpringLayout.SOUTH, lblHelloWelcomeTo);
		springLayout.putConstraint(SpringLayout.WEST, lblPickAndStart, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblPickAndStart);
		
		JButton btnCreateYourOwn = new JButton("Create your Own Simulation (hah)");
		springLayout.putConstraint(SpringLayout.NORTH, btnCreateYourOwn, 18, SpringLayout.SOUTH, lblPickAndStart);
		springLayout.putConstraint(SpringLayout.WEST, btnCreateYourOwn, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnCreateYourOwn);
		btnCreateYourOwn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				try {
					NewSimulation newSim = new NewSimulation();
					Connection conn;
					try {
						conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				
					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next())
					{
						int a = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 1");		
					}
					conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				shutDown();
				
			}
		});
		
		JLabel lblIfNewSimulation = new JLabel("If new simulation ... then autoclose this and go to new simulation");
		springLayout.putConstraint(SpringLayout.NORTH, lblIfNewSimulation, 70, SpringLayout.SOUTH, btnCreateYourOwn);
		springLayout.putConstraint(SpringLayout.WEST, lblIfNewSimulation, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblIfNewSimulation);
		
		JLabel lblScenario = new JLabel("Scenario 1");
		springLayout.putConstraint(SpringLayout.NORTH, lblScenario, 107, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblScenario, -280, SpringLayout.EAST, getContentPane());
		getContentPane().add(lblScenario);
		
		
		JLabel lblScenario_1 = new JLabel("Scenario 2");
		springLayout.putConstraint(SpringLayout.NORTH, lblScenario_1, 28, SpringLayout.SOUTH, lblScenario);
		springLayout.putConstraint(SpringLayout.WEST, lblScenario_1, 0, SpringLayout.WEST, lblScenario);
		getContentPane().add(lblScenario_1);
		
		JLabel lblScenario_2 = new JLabel("Scenario 3");
		springLayout.putConstraint(SpringLayout.NORTH, lblScenario_2, 29, SpringLayout.SOUTH, lblScenario_1);
		springLayout.putConstraint(SpringLayout.WEST, lblScenario_2, 0, SpringLayout.WEST, lblScenario);
		getContentPane().add(lblScenario_2);
		
		JLabel lblScenario_3 = new JLabel("Scenario 4");
		springLayout.putConstraint(SpringLayout.NORTH, lblScenario_3, 30, SpringLayout.SOUTH, lblScenario_2);
		springLayout.putConstraint(SpringLayout.WEST, lblScenario_3, 0, SpringLayout.WEST, lblScenario);
		getContentPane().add(lblScenario_3);
		
		JButton btnScenario1 = new JButton("Scenario 1");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario1, 22, SpringLayout.SOUTH, lblIfNewSimulation);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario1, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario1);
		btnScenario1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				try {
					Connection conn;
					try {
						conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				
					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next())
					{
						int a = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 2");		
					}
					conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
					
					// then initialize the fish and objects from here
				try {
					Connection connn;
					try
					{
						connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
				        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
				        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");
				        	
						}
						connn.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
				} catch (SecurityException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				scenarioShutDown();
			}
		});
		JButton btnScenario2 = new JButton("Scenario 2");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario2, 20, SpringLayout.SOUTH, btnScenario1);
		springLayout.putConstraint(SpringLayout.EAST, btnScenario2, 0, SpringLayout.EAST, btnScenario1);
		getContentPane().add(btnScenario2);
		btnScenario2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				try {
					Connection conn;
					try {
						conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				
					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next())
					{
						int a = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 3");		
					}
					conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					Connection connn;
					try
					{
						connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
				        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
				        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");
				        	
						}
						connn.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
			try {
					Connection conne;
					try
					{
						conne = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 4");
				        	
						}
						conne.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
				scenarioShutDown();
				
			}
		});
		JButton btnScenario3 = new JButton("Scenario 3");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario3, 19, SpringLayout.SOUTH, btnScenario2);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario3, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario3);
		btnScenario3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				try {
					Connection conn;
					try {
						conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				
					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next())
					{
						int a = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 4");		
					}
					conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				try {
					Connection connn;
					try
					{
						connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
				        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
				        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");
				        	
						}
						connn.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
			try {
					Connection conne;
					try
					{
						conne = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
				        	int b = s.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
						}
						conne.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
				
				scenarioShutDown();
				
			}
		});
		JButton btnScenario4 = new JButton("Scenario 4");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario4, 16, SpringLayout.SOUTH, btnScenario3);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario4, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario4);
		btnScenario4.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
		
				try {
					Connection conn;
					try {
						conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
				
					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next())
					{
						int a = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 5");		
					}
					conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e5) {
					// TODO Auto-generated catch block
					e5.printStackTrace();
				}
				try {
					Connection connn;
					try
					{
						connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
				        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
				        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 3 where ID = 3");
				        	
						}
						connn.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
			try {
					Connection conne;
					try
					{
						conne = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs =s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next())
						{
				        	int a = s.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
				        	int b = s.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 2");
				        	int c = s.executeUpdate("UPDATE SimulationObjects set objID = 3 where ID = 3");
				        	int d = s.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
				        	int g = s.executeUpdate("UPDATE SimulationObjects set objID = 5 where ID = 5");
				        	int f = s.executeUpdate("UPDATE SimulationObjects set objID = 6 where ID = 6");
				        	
						}
						conne.close();
					} catch (Exception p1)
					{
						p1.printStackTrace();
					}
				
					
				} catch(Exception pp)
				{
					pp.printStackTrace();
				}
				scenarioShutDown();
				
			}
		});
		
		JLabel lblCichlidsOn = new JLabel("2 Cichlids");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidsOn, 4, SpringLayout.NORTH, btnScenario1);
		getContentPane().add(lblCichlidsOn);
		
		JLabel lblCichlids = new JLabel("2 Cichlids, 1 Large Pot");
		springLayout.putConstraint(SpringLayout.WEST, lblCichlids, 24, SpringLayout.EAST, btnScenario2);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidsOn, 0, SpringLayout.WEST, lblCichlids);
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlids, 4, SpringLayout.NORTH, btnScenario2);
		getContentPane().add(lblCichlids);
		
		JLabel lblCichlids_1 = new JLabel("2 Cichlids, 1 Large Pot, 1 Large Plant");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlids_1, 4, SpringLayout.NORTH, btnScenario3);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlids_1, 24, SpringLayout.EAST, btnScenario3);
		getContentPane().add(lblCichlids_1);
		
		JLabel lblCichlidsAnd = new JLabel("3 Cichlids and the works.");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidsAnd, 0, SpringLayout.NORTH, btnScenario4);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidsAnd, 0, SpringLayout.WEST, lblCichlidsOn);
		getContentPane().add(lblCichlidsAnd);
		
		JLabel lblPicturesOfScenarios = new JLabel("Pictures of scenarios here");
		springLayout.putConstraint(SpringLayout.NORTH, lblPicturesOfScenarios, 0, SpringLayout.NORTH, btnCreateYourOwn);
		springLayout.putConstraint(SpringLayout.WEST, lblPicturesOfScenarios, 0, SpringLayout.WEST, lblScenario);
		getContentPane().add(lblPicturesOfScenarios);
		
		this.setVisible(true);	
	}
	public void shutDown()
	{
			super.dispose();
	}
	public void scenarioShutDown()
	{
		super.dispose();
		new MyGame().start();

	}
}
