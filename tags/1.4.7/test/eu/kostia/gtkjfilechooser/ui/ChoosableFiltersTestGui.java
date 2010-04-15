/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
package eu.kostia.gtkjfilechooser.ui;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class ChoosableFiltersTestGui {

	public void testChoosableFilters() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(createExtensionFilter("gif", "jpg"));
		fileChooser.addChoosableFileFilter(createExtensionFilter("txt"));

		for (FileFilter fileFilter : fileChooser.getChoosableFileFilters()) {
			System.out.println("  FileFilter: " + fileFilter.getDescription());
		}

		int option = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == option) {
			System.out.println("Selected file: " + fileChooser.getSelectedFile());
		}



	}

	private FileFilter createExtensionFilter(final String... extensions) {
		FileFilter imageFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName().toLowerCase();
				if (pathname.isDirectory()) {
					return true;
				}

				for (String extension : extensions) {
					if (name.endsWith("."+extension)) {
						return true;
					}
				}

				return false;
			}

			@Override
			public String getDescription() {
				return Arrays.toString(extensions) + " files";
			}

			@Override
			public String toString() {
				return getDescription();
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
