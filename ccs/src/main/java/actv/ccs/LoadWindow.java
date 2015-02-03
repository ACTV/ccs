package actv.ccs;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
public class LoadWindow extends JFrame {
	
	public LoadWindow() {
		setTitle("Load Window");
		setSize(800, 800);
		System.out.println("LoadWindow is being called\n");
		JMenuBar b = createjMenu();
		this.setJMenuBar(b);
		
		// left side
		JPanel leftP = new JPanel();
		leftP.setBorder(new TitledBorder("boo"));
		leftP.setLayout(new GridLayout(10,1));
		this.add(leftP, BorderLayout.WEST);
		// adding labels
		JLabel cichlidPrompt = new JLabel ("How many Convict Cichlids: boooowwa");
		leftP.add(cichlidPrompt);
		// create jmenu thing
		JLabel fishFromDBPrompt = new JLabel ("Fish #  ");
		leftP.add(fishFromDBPrompt);
		// create jmenu for picking a fish from the database
		JLabel plantPrompt = new JLabel("How many plants: ");
		leftP.add(plantPrompt);
		// create dropdown
		JLabel plantDBPick = new JLabel("Plant Type: ");
		leftP.add(plantDBPick);
		// create dropdown
		JLabel tankSizePrompt = new JLabel("Tank Size: ");
		leftP.add(tankSizePrompt);
		// create empty box
		// width dimension
		// create empty box
		// height dimension
		JLabel waterTempP = new JLabel("Water Temperature: ");
		leftP.add(waterTempP);
		// drop down box
		
		JButton runSimulation = new JButton("Run Simulation");
		leftP.add(runSimulation);
		// lead to run simulation screen
		// aka test
		setVisible(true);
	}
	private JMenuBar createjMenu() {
		System.out.println("jmenu being called");
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