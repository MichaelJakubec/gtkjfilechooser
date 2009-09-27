package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class GtkFileChooserUITestGui {

	public void testGTKFileChooserUI() throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())){
			UIManager.put("FileChooserUI", eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}		

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
	}

	public void testLayout() throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		// FIXME remove
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel rightPanel = new JPanel();

		// --

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.RED);

		mainPanel.add(rightPanel, BorderLayout.LINE_START);
		mainPanel.add(leftPanel, BorderLayout.CENTER);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().add(mainPanel);
		f.pack();
		f.setVisible(true);

	}

	public static void main(String[] args) throws Exception {
		new GtkFileChooserUITestGui().testGTKFileChooserUI();
		//new GtkFileChooserUITestGui().testLayout();

	}
}

