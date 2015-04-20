package actv.ccs.model.ui;

import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import actv.ccs.sageTest.MyGame;

public class SimulationPrompter extends JFrame {
	
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
		
		JButton btnChooseFromA = new JButton("Choose from a Preexisting Scenario");
		springLayout.putConstraint(SpringLayout.NORTH, btnChooseFromA, 26, SpringLayout.SOUTH, btnCreateYourOwn);
		springLayout.putConstraint(SpringLayout.WEST, btnChooseFromA, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnChooseFromA);
		
		JLabel lblIfNewSimulation = new JLabel("If new simulation ... then autoclose this and go to new simulation");
		springLayout.putConstraint(SpringLayout.NORTH, lblIfNewSimulation, 21, SpringLayout.SOUTH, btnChooseFromA);
		springLayout.putConstraint(SpringLayout.WEST, lblIfNewSimulation, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblIfNewSimulation);
		
		JLabel lblElseIf = new JLabel("Else ... if Preexisting scenario then jcombobox and etc. and pics showing the other tanks");
		springLayout.putConstraint(SpringLayout.NORTH, lblElseIf, 22, SpringLayout.SOUTH, lblIfNewSimulation);
		springLayout.putConstraint(SpringLayout.WEST, lblElseIf, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblElseIf);
		
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
		
		JComboBox scenarioChooser = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, scenarioChooser, -4, SpringLayout.NORTH, lblScenario_3);
		springLayout.putConstraint(SpringLayout.WEST, scenarioChooser, 0, SpringLayout.WEST, lblHelloWelcomeTo);
		springLayout.putConstraint(SpringLayout.EAST, scenarioChooser, 244, SpringLayout.WEST, getContentPane());
		getContentPane().add(scenarioChooser);
		scenarioChooser.addItem("Scenario 1");
		scenarioChooser.addItem("Scenario 2");
		scenarioChooser.addItem("Scenario 3");
		scenarioChooser.addItem("Scenario 4");
		
		
		
		JButton btnRunSimulation = new JButton("Run Simulation");
		springLayout.putConstraint(SpringLayout.NORTH, btnRunSimulation, 17, SpringLayout.SOUTH, scenarioChooser);
		springLayout.putConstraint(SpringLayout.WEST, btnRunSimulation, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnRunSimulation);
		
		this.setVisible(true);	
	}
	public void shutDown()
	{
			super.dispose();
	}
}
