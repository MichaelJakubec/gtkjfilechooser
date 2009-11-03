package eu.kostia.gtkjfilechooser.ui;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class GtkFileChooserSaveTestGui {
	public void showSaveDialog() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(null);
		if (JFileChooser.APPROVE_OPTION == option){
			System.out.println(">>>> Selected file: " + fileChooser.getSelectedFile());
		}
	}


	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())){
			UIManager.put("FileChooserUI", eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GtkFileChooserSaveTestGui test = new GtkFileChooserSaveTestGui();
				test.showSaveDialog();
			}
		});



	}
}
