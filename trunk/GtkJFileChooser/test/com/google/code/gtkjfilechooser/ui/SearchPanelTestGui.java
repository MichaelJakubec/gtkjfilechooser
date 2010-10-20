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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.google.code.gtkjfilechooser.GtkFileView;
import com.google.code.gtkjfilechooser.ui.FilesListPane;
import com.google.code.gtkjfilechooser.ui.SearchPanel;
import com.google.code.gtkjfilechooser.ui.JPanelUtil.PanelElement;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;


public class SearchPanelTestGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane = new FilesListPane(new GtkFileView());
	private SearchPanel  searchPanel;

	public SearchPanelTestGui() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.filesPane = new FilesListPane(new GtkFileView());
		this.searchPanel = new SearchPanel(filesPane);

		getContentPane().add(createPanel(
				0,10, // horizontal and vertical gap
				new PanelElement(searchPanel, BorderLayout.PAGE_START),
				new PanelElement(filesPane, BorderLayout.CENTER)
		));
	}




	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				SearchPanelTestGui test = new SearchPanelTestGui();

				test.pack();
				test.setVisible(true);
			}

		});


	}
}
