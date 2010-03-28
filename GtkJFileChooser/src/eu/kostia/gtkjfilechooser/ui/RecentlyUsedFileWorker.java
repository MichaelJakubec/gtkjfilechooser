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

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;

import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedFileWorker extends SwingWorker<Void, Void> implements PropertyChangeListener {

	private static final int NUMBER_OF_RECENT_FILES = 30;

	/**
	 * Manager for the recent used files.
	 */
	private RecentlyUsedManager recentManager;

	private final GtkFileChooserUI fileChooserUI;

	public RecentlyUsedFileWorker(GtkFileChooserUI fileChooserUI) {
		this.fileChooserUI = fileChooserUI;
		addPropertyChangeListener(this);
	}

	@Override
	protected Void doInBackground() throws Exception {
		fileChooserUI.getFileChooser().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (recentManager == null) {
			// RecentlyUsedManager objects are expensive: create them
			// only when needed.
			recentManager = new RecentlyUsedManager(NUMBER_OF_RECENT_FILES);
		}
		List<File> fileEntries = recentManager.getRecentFiles();
		// add files in a loop instead of using
		// recentlyUsedPane#setModel:
		// the user see the progress and hasn't the impression that the
		// GUI is frozen.
		fileChooserUI.getRecentlyUsedPane().getModel().clear();
		for (File file : fileEntries) {
			if(fileChooserUI.getFileChooser().getFileFilter().accept(file)){
				fileChooserUI.getRecentlyUsedPane().addFile(file);
			}					
		}

		return null;
	}


	@Override
	protected void done() {
		fileChooserUI.getFileChooser().setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Log.debug(evt);

	}

}


