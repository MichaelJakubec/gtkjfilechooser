package eu.kostia.gtkjfilechooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class NavigationKeyBindingTestGui extends JFrame implements ActionListener {
	
	public NavigationKeyBindingTestGui() {
		setTitle("NavigationKeyBindingTestGui");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(500, 500));
		panel.setBackground(Color.RED);
		getContentPane().add(panel);
		pack();
		
		NavigationKeyBinding keyBinding = new NavigationKeyBinding(panel);
		keyBinding.addActionListener(this);
	}
	
	public static void main(String[] args) {
		new NavigationKeyBindingTestGui().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println("Signal: " + cmd);		
	}

}
