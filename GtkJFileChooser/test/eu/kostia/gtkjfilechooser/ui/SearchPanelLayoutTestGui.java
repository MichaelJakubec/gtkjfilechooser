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

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.GtkFileView;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class SearchPanelLayoutTestGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane = new FilesListPane(new GtkFileView());
	private SearchPanel  searchPanel;

	public SearchPanelLayoutTestGui() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.filesPane = new FilesListPane(new GtkFileView());
		this.searchPanel = new SearchPanel(filesPane);

		getContentPane().add(createPanel(new PanelElement(searchPanel, BorderLayout.CENTER)));
	}




	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				SearchPanelLayoutTestGui test = new SearchPanelLayoutTestGui();

				test.setSize(new Dimension(800, 400));
				test.setVisible(true);
			}

		});


	}
}
