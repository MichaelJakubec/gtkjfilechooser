package eu.kostia.gtkjfilechooser.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.GtkLocationsPane;



public class GtkBoomarkPaneTestGui {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		GtkLocationsPane pane = new GtkLocationsPane();

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().add(pane);
		f.pack();
		f.setVisible(true);
	}

}
