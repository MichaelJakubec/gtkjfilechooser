/*******************************************************************************
 * Copyright 2009 Costantino Cerbo.  All Rights Reserved.
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
