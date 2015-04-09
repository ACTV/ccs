package actv.ccs.model.ui;

import java.awt.FlowLayout;

import javax.swing.*;

public class DataView extends JPanel implements IObserver { 
	private SimulationWorld realSW;
	private FlowLayout layout = new FlowLayout();
	private JLabel timesOutTop;
	private JLabel livesOutTop;
	private JLabel scoreOutTop;
	private JLabel soundOutTop;
	
	public DataView (SimulationWorld sw)
	{
		setLayout(layout);
		realSW = sw;
		// create JLabels of time, lives, score, and sound toggle 
		timesOutTop = new JLabel("Time: ");	// create top scoreboard
		add(timesOutTop);	
		setVisible(true);

	}
	 public void update(IObservable o, Object arg) { 
		// In the case of the ScoreView, what update() does is update the 
		// contents of the JLabels displaying the game state values in the JPanel (use JLabel 
		//  method setText(String) to update the label).
		// update 
		arg = (SimulationWorldProxy)o;
		timesOutTop.setText("Time: " + Double.toString(realSW.getTimer())); // update the text whenever a change occurs in GameWorld
		
		
	 }

}