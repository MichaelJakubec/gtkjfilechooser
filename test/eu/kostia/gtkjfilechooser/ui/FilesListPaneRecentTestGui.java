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

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.GtkFileView;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class FilesListPaneRecentTestGui {

	private FilesListPane testShowRecentUsedFiles() {
		FilesListPane pane = new FilesListPane(new GtkFileView());
		pane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		List<File> fileEntries = new RecentlyUsedManager(30).getRecentFiles();
		pane.setModel(fileEntries, false);
		return pane;
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		FilesListPaneRecentTestGui test = new FilesListPaneRecentTestGui();

		FilesListPane pane = test.testShowRecentUsedFiles();
		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));
	}
}
