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

import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * Autocompleter decorator for path locations
 * 
 * @author c.cerbo
 * 
 */
public class PathAutoCompleter extends Autocompleter {

	private String currentPath;
	private boolean showHidden = false;
	private int fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;
	private javax.swing.filechooser.FileFilter currentFilter;

	public PathAutoCompleter(JTextField comp) {
		super(comp);
		setCurrentPath(System.getProperty("user.dir"));
	}

	public String getCurrentPath() {
		return currentPath;
	}

	/**
	 * Sets the auto completion to allow the user to justselect files, just
	 * select directories, or select both files and directories. The default is
	 * <code>JFileChooser.FILES_ONLY</code>.
	 * 
	 * @param mode
	 *            the type of files to be displayed:
	 *            <ul>
	 *            <li>JFileChooser.FILES_ONLY</li>
	 *            <li>JFileChooser.DIRECTORIES_ONLY</li>
	 *            <li>JFileChooser.FILES_AND_DIRECTORIES</li>
	 *            </ul>
	 * 
	 * @exception IllegalArgumentException
	 *                if <code>mode</code> is an illegal file selection mode
	 */
	public void setFileSelectionMode(int fileSelectionMode) {
		if (fileSelectionMode < 0  || fileSelectionMode > 2) {
			throw new IllegalArgumentException("Value " + fileSelectionMode + " is invalid.");
		}
		this.fileSelectionMode = fileSelectionMode;
	}

	public void setCurrentFilter(javax.swing.filechooser.FileFilter currentFilter) {
		this.currentFilter = currentFilter;
	}

	/**
	 * Show hidden files?
	 * 
	 * @param showHidden
	 */
	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	@Override
	protected List<String> updateSuggestions(String value) {
		File file = isAbsolute(value) ? new File(value) : new File(getCurrentPath()
				+ File.separator + value);

		final String prefix = file.getName();
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!showHidden && pathname.isHidden()) {
					return false;
				}

				if (fileSelectionMode == DIRECTORIES_ONLY && !pathname.isDirectory()) {
					return false;
				}

				// Take care of the choosableFilter set in the combo box
				if (currentFilter != null) {
					if(!currentFilter.accept(pathname)){
						return false;
					}
				}

				String name = pathname.getName();
				return name != null ? name.startsWith(prefix) : false;
			}
		};

		File[] list = null;
		if (file.isDirectory()) {
			list = file.listFiles(filter);
		} else if (file.getParentFile() != null) {
			list = file.getParentFile().listFiles(filter);
		}

		if (list == null || list.length == 0) {
			return null;
		}

		List<String> results = new ArrayList<String>();

		int index = value.lastIndexOf(File.separatorChar);
		String before = (index != -1) ? value.substring(0, index + 1) : "";
		for (File f : list) {
			String name = f.getName();
			if (f.isDirectory()) {
				// add slash if directory
				name = name + File.separator;
			}
			results.add(before + name);
		}

		Collections.sort(results);
		return results;
	}

	private boolean isAbsolute(String value) {
		return new File(value).isAbsolute();
	}

	@Override
	protected ListCellRenderer getCellRenderer() {
		return new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);

				String suggestion = (String) value;
				int lastIndexOf = suggestion.lastIndexOf(File.separatorChar);
				if (lastIndexOf < suggestion.length() - 1) {
					setText(suggestion.substring(lastIndexOf + 1));
				}
				return this;
			}
		};
	}

}
