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

import static eu.kostia.gtkjfilechooser.I18N._;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.kostia.gtkjfilechooser.FileSearch;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.FileSearch.FileSearchHandler;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

/**
 * Panel to enter the term for a search.
 * @author c.cerbo
 *
 */
public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane;

	private FileSearch fileSearch;

	private JLabel searchLabel;

	private JTextField searchTextField;

	private JButton stopButton;

	private FileFilter fileFilter;

	public SearchPanel(FilesListPane pane) {		
		this.filesPane = pane;

		//		setLayout(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		/**
		 * Search label
		 */
		searchLabel = new JLabel(_("_Search:"));
		add(searchLabel);
		add(Box.createRigidArea(new Dimension(10,0)));

		/**
		 * Search TextField
		 */
		searchTextField = new JTextField();
		int height = searchTextField.getPreferredSize().height;
		searchTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, height));
		searchTextField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				filesPane.getModel().clear();
				stopSearch();
				fileSearch = new FileSearch(System.getProperty("user.home"), searchTextField.getText(), new ThisFileSearchHandler());
				fileSearch.setSearchHidden(GtkFileChooserSettings.get().getShowHidden());
				fileSearch.setFileFilter(fileFilter);
				fileSearch.start();					
			}
		});
		add(searchTextField);

		/**
		 * Stop Button
		 */
		stopButton = new JButton(GtkStockIcon.get("gtk-stop", Size.GTK_ICON_SIZE_MENU));
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				stopSearch();
			}			
		});
		add(stopButton);
	}


	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	@Override
	public boolean requestFocusInWindow(){
		return searchTextField.requestFocusInWindow();
	}

	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		filesPane.setCursor(cursor);
	}

	public void stopSearch() {
		if (fileSearch != null) {
			fileSearch.stop();
			fileSearch = null;
		}
	}

	private class ThisFileSearchHandler implements FileSearchHandler {

		@Override
		public void found(File file) {
			filesPane.addFile(file);			
		}

		@Override
		public void finished(Status status) {
			setCursor(Cursor.getDefaultCursor());				
		}	

	}
}