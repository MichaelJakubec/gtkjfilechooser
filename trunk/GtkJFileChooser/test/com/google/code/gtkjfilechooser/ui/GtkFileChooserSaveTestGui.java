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
package com.google.code.gtkjfilechooser.ui;

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
			UIManager.put("FileChooserUI", com.google.code.gtkjfilechooser.ui.GtkFileChooserUI.class.getName());
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

