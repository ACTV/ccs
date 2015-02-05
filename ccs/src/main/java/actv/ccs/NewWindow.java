package actv.ccs;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
public class NewWindow extends JFrame {
		
		// initialize variables
		
		public NewWindow() {
		setTitle("New Window");
		setSize(1200, 800);
		System.out.println("NewWindow is being called\n");
		JMenuBar b = createjMenu();
		this.setJMenuBar(b);
		
		// left side
		JPanel leftP = new JPanel();
		leftP.setBorder(new TitledBorder("boo"));
		leftP.setLayout(new GridLayout(10,1));
		this.add(leftP, BorderLayout.WEST);
		// adding labels
		// cichlid prompt
		JLabel cichlidPrompt = new JLabel ("How many Convict Cichlids: "); // text box - set a range now
		leftP.add(cichlidPrompt);
		JTextField cichlidNumberP = new JTextField();
		leftP.add(cichlidNumberP); // add a number check including range etc.
		// then when the user has a accceptable range
		/*
		 * when user has good range for cichlidNumberP
		 * then create n number of JPopupMenus
		 */
		// create jmenu thing
		/*
		 * Then the user picks out the fish from the database etc.
		 */
		JLabel fishFromDBPrompt = new JLabel ("Fish #1 ");
		leftP.add(fishFromDBPrompt);
		/*
		 * dropdown menu from database, so need to find a awy to access database, mysql?
		 */
		// create jmenu for picking a fish from the database
		JLabel plantPrompt = new JLabel("\nHow many plants: ");
		leftP.add(plantPrompt);
		JTextField plantPN = new JTextField();
		leftP.add(plantPN);
		/*
		 * range of 2-3 plants for now
		 * when user has a good number of plants prompts
		 * then create n dropdownmenus for plant types
		 */
		// acceptable ranges
		JLabel plantDBPick = new JLabel("Plant Type: ");
		leftP.add(plantDBPick);
		// create dropdownmenu for hydrogenas?
		JLabel tankSizePrompt = new JLabel("\nTank Size (Length x Width x Height) (cm)s: ");
		leftP.add(tankSizePrompt);
		// length dimension
		JTextField lengthP = new JTextField();
		leftP.add(lengthP);
		// create empty box
		System.out.println("AHAHAH");
		// width dimension
		JTextField widthP = new JTextField();
		leftP.add(widthP);
		// create empty box
		System.out.println(" x ");
		// height dimension
		JTextField heightP = new JTextField();
		leftP.add(heightP);
		// create empty box
		System.out.println("\n");
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