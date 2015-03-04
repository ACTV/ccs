package actv.ccs.model.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoadWindow extends JFrame {
	
	private JTextField filename = new JTextField();
	private JTextField dir = new JTextField();
	private JButton load = new JButton("load");

	
	public LoadWindow()
	{
		this.setTitle("Load Window Test");
		JPanel p = new JPanel();
		load.addActionListener(new LoadAction());
		p.add(load);
		
		Container cp = getContentPane();
		cp.add(p, BorderLayout.SOUTH);
		dir.setEditable(false);
		filename.setEditable(false);
		p = new JPanel();
		p.setLayout(new GridLayout(2,1));
		p.add(filename);
		p.add(dir);
		cp.add(p, BorderLayout.NORTH);
		
		this.setSize(250, 110);
		this.setVisible(true);
	}
	
	public class LoadAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(LoadWindow.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(c.getSelectedFile().getName());
				dir.setText(c.getCurrentDirectory().toString());
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("Canceling");
				dir.setText("");
			}
		}
	}
}