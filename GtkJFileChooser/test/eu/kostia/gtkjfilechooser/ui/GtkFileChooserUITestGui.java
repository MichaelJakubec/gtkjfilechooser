package eu.kostia.gtkjfilechooser.ui;

import java.io.File;

import javax.swing.JFileChooser;
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

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())){
			UIManager.put("FileChooserUI", eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}

		GtkFileChooserUITestGui test = new GtkFileChooserUITestGui();
		test.showOpenDialog();
	}
}

