package eu.kostia.gtkjfilechooser.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;

public class ImagePreviewerTestGui {
	public void showOpenDialog() throws Exception {
		File imgDir = FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.PICTURES);

		JFileChooser chooser = new JFileChooser(imgDir);		
		chooser.setAccessory(new ImagePreviewer(chooser));

		int option = chooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == option){
			System.out.println("Selected file: " + chooser.getSelectedFile());
		}
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())) {
			UIManager.put("FileChooserUI",
					eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}



		ImagePreviewerTestGui test = new ImagePreviewerTestGui();
		test.showOpenDialog();
	}
}
