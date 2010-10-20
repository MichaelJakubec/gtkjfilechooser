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

import static com.google.code.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static com.google.code.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.UIManager;

import com.google.code.gtkjfilechooser.GtkFileView;
import com.google.code.gtkjfilechooser.ui.FilesListPane;
import com.google.code.gtkjfilechooser.ui.JPanelUtil.PanelElement;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;


public class FilesListPaneAddTestGui {

	private FilesListPane testAddFiles() throws Exception {
		FilesListPane pane = new FilesListPane(new GtkFileView());
		pane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		return pane;
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		FilesListPaneAddTestGui test = new FilesListPaneAddTestGui();

		FilesListPane pane = test.testAddFiles();
		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));

		pane.addFile(new File("Hello"){		
			private static final long serialVersionUID = 1L;

			@Override
			public long lastModified() {
				return 1254779700475L;
			}
		});
		Thread.sleep(1000);
		pane.addFile(new File("to"){		
			private static final long serialVersionUID = 1L;

			@Override
			public long lastModified() {
				return 254779700475L;
			}
		});
		Thread.sleep(1000);
		pane.addFile(new File("Everyone"){		
			private static final long serialVersionUID = 1L;

			@Override
			public long lastModified() {
				return 1254839609487L;
			}
		});
	}
}
