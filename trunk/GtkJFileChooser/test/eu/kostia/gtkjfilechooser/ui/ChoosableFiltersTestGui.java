/*******************************************************************************
 * Copyright 2010 Costantino Cerbo.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
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
