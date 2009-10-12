package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class GtkFileChooserUITestGui {

	public void showOpenDialog() throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		int option = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == option){			
			for (File selected : fileChooser.getSelectedFiles()) {
				System.out.println("Selected file: " + selected);	
			}

		}
	}

	public void showSaveDialog() throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(null);
		if (JFileChooser.APPROVE_OPTION == option){
			System.out.println("Selected file: " + fileChooser.getSelectedFile());
		}
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
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())){
			UIManager.put("FileChooserUI", eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}

		GtkFileChooserUITestGui test = new GtkFileChooserUITestGui();
		test.showOpenDialog();
		//test.showSaveDialog();
		//test.testLayout();

	}
}

