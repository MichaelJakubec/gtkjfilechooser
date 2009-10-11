package eu.kostia.gtkjfilechooser.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class ChoosableFiltersTestGui {

	public void testChoosableFilters() {
		FileFilter imageFilter = createImageFilter();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(imageFilter);

		int option = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == option) {
			System.out.println("Selected file: " + fileChooser.getSelectedFile());
		}
	}

	private FileFilter createImageFilter() {
		FileFilter imageFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName().toLowerCase();
				return name.endsWith(".gif") || name.endsWith(".jpg");
			}

			@Override
			public String getDescription() {
				return "JPEG and GIF Image Files";
			}

		};
		return imageFilter;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())) {
			UIManager.put("FileChooserUI",
					eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
		}

		ChoosableFiltersTestGui test = new ChoosableFiltersTestGui();
		test.testChoosableFilters();

	}
}
