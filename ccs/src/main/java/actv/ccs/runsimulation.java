package actv.ccs;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
public class runsimulation extends JFrame{
	
	public runsimulation()
	{
	
		setTitle("Convict Cichlid Simulator");
		setSize(500, 500);
		System.out.println("runSimulation is being called\n");
		JMenuBar b = createjMenu();
		this.setJMenuBar(b);
		// create center border = stuff
		JPanel displayPanel = new JPanel();
		// use something along the lines of gameobserver ala 133
		displayPanel.setBorder(new EtchedBorder());
		displayPanel.setBackground(Color.GRAY);
		this.add(displayPanel, BorderLayout.CENTER);
		// create bottom border = stuff
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(new EtchedBorder());
		dataPanel.setBackground(Color.YELLOW);
		this.add(dataPanel, BorderLayout.SOUTH);
		// create data
		JLabel fishEx = new JLabel("Fish #1 \n");
		dataPanel.add(fishEx);
		// create small table for Fish Data 
		// create small table for data on simulation, time etc.
		JButton StopSim = new JButton("Stop Simulation");
		JButton ContSim = new JButton("Continue Simulation");
		JButton outPutData = new JButton("Output Data");
		dataPanel.add(StopSim);
		//newline
		dataPanel.add(outPutData);
		this.setVisible(true);	
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