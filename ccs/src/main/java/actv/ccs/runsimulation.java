package actv.ccs;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class runsimulation extends JFrame implements ActionListener {
		// initialize variables - bless you 133!
	
		private MapView mv;
		private GameWorld gw;
		private Timer timer;
	
	public runsimulation()
	{
	
		gw = new GameWorld();
		mv = new MapView(gw);
		gw.addObserver(mv);
		
		
		setTitle("Convict Cichlid Simulator");
		setSize(1000, 600);
		System.out.println("runSimulation is being called\n");
		JMenuBar b = createjMenu();
		this.setJMenuBar(b);
		
		// create center border = stuff
		mv.setBorder( new EtchedBorder());
		mv.setBackground(Color.GRAY);
		this.add(mv, BorderLayout.CENTER);
		
		// create bottom border = stuff
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(new EtchedBorder());
		dataPanel.setBackground(Color.YELLOW);
		this.add(dataPanel, BorderLayout.SOUTH);
		// create data
		JLabel fishEx = new JLabel("Fish #1 \n");
		dataPanel.add(fishEx);
		// create small table for Fish Data
		// fish info from database is being displayed here through viewer
		// create small table for data on simulation, time etc.
		JButton StopSim = new JButton("Stop Simulation");
		JButton ContSim = new JButton("Continue Simulation");
		JButton outPutData = new JButton("Output Data"); // testing here
		
		dataPanel.add(StopSim);
		//newline
		dataPanel.add(outPutData);
		
		
		// 
		gw.spawn();
		
		
		//
		timer = new Timer(500, this);
		timer.start();
		this.setVisible(true);	
		gw.notifyObservers(); // notify observers of change
	}

 	public void actionPerformed(ActionEvent e) {
 	 	 double timeElapsed = gw.getTime();
 	 	 gw.setTime(timeElapsed);
 	 	 // each timer tick should pass an elapsed time value to the move method of each movable object
 		 Iterator i = gw.getIterator();
 		 while (i.hasNext())
 		 {
 			 GameObject obj = (GameObject) i.getNext(); // for ever object 
 			   if (obj instanceof IMovable)
 			   {
 				((IMovable) obj).move( timeElapsed ); // move items if paused
 			   }
 		 }
 		 gw.setTime(gw.getTime()+1);
 		 gw.notifyObservers();
 		 repaint();
 	}
	private JMenuBar createjMenu() { 
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