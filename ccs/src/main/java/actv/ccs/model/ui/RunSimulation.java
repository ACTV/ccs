package actv.ccs.model.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;







import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import actv.ccs.CCSKnowledgeBaseBuilder;
import actv.ccs.fact.Auditor;
import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.IDrawable;
import actv.ccs.model.IMovable;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;

import java.awt.FlowLayout;

import javax.swing.border.LineBorder;
import javax.swing.JLayeredPane;


public class RunSimulation extends JFrame implements ActionListener {
	private String mainFilePath = "";
	private ConvictCichlid cichlid;
	private TankObject tank;
	private SimulationWorld world;
	private TankView tV;
	private DataView dV;
	private ResultSet rs;
	private Timer timer;
	private int timerT;
	
	public RunSimulation () throws IOException
	{
		setTitle("Convict Cichlid Fish Simulator Test 1");
		setSize(1000,600);
		
		/*
		 * so the thing i want to do is merge all of the sage code to the current iterations of ... everything.
		 * so this is going to suck. i need to learn how to merge all of these functions.
		 */
		
		
		
	//	mH = new MainHub(mainFilePath);
		world = new SimulationWorld();
		//glc = new GLCanvas();
		
	//	mF = new MainFunction(mg);
		// here is where you get the time from the tank. 
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM [TankData] WHERE ID='1' ");
			while (rs.next())
			{
			String id = rs.getString("Time"); // added new string for ID
			timerT = Integer.parseInt(id);
			System.out.println("rS: " + timerT);
		}
        	conn.close();
        	
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// create menu bar
		JMenuBar b = createJMenu();
		this.setJMenuBar(b);
        tV = new TankView(world);
        dV = new DataView(world);
	//	tV = new TankView(world,mH.getGLC());
		world.addObserver(tV);
		world.addObserver(dV);
		
		tV.setBorder(new EtchedBorder());
		tV.setBackground(Color.white);
		
		// Bottom Panel for Data Output
	//	JPanel dataPanel = new JPanel();
	//	dataPanel.setBorder(new EtchedBorder());
		
		// output fish data to one side ... this is good for now.
		JLabel printData = new JLabel("Data Output");
		
		JButton btnPauseButton = new JButton("Pause Button");
		
		JButton endSimulationButton = new JButton("End Simulation");
		endSimulationButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Connection conn;
				try {
					conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
					Statement s = conn.createStatement();
		        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 1");
		        	System.out.println("a is " + a);
		        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 2");
		        	System.out.println("b is " + b);
		        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");
		        	System.out.println("c is " + b);

		        	conn.close();
		        	
		        	// End the Rules Knowledge Session
		        	world.stopRunner();
		        	
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
				// close the game
				System.exit(0);
			}
		});
		
		JLayeredPane layeredPane = new JLayeredPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(tV, GroupLayout.PREFERRED_SIZE, 982, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(printData)
							.addGap(811)
							.addComponent(btnPauseButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)
							.addGap(650)
							.addComponent(endSimulationButton)))
					.addGap(84))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(tV, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPauseButton)
						.addComponent(printData, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(endSimulationButton)
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
					.addGap(12))
		);
		
		DataView dataView = new DataView((SimulationWorld) null);
		dataView.setBorder(new LineBorder(new Color(0, 0, 0)));
		dataView.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(dataView, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addGap(5)
					.addComponent(dataView, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(70, Short.MAX_VALUE))
		);
		layeredPane.setLayout(gl_layeredPane);
		getContentPane().setLayout(groupLayout);
		
		world.startRunner();

		System.out.println("timer: " + timerT);

		timer = new Timer(timerT, this);
		timer.start();
		this.setVisible(true);
		world.notifyObservers();
		
	}
	public void actionPerformed(ActionEvent e) {
		double time = 0; // need to fix this later
		time++;
		
		if (time == timerT)
		{
			System.out.println("CLOSE THIS SIMULATION THE TIMER HAS GONE CRAZY");
		}
		 Iterator iteraz = world.getIterator(); // iterate to remove flagged objects from game
		 while (iteraz.hasNext())
		 {
			 IMovable obj = (IMovable) iteraz.getNext();
				  obj.move(time);
		 }
			 	world.notifyObservers();
			 	repaint();
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
