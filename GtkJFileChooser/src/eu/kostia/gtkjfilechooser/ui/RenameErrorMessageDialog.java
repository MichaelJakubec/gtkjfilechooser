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

import java.text.MessageFormat;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class RenameErrorMessageDialog {

	private final String renameErrorTitleText;
	private final String renameErrorText;
	private final String renameErrorFileExistsText;

	private JFileChooser chooser;

	public RenameErrorMessageDialog(JFileChooser chooser) {
		this.chooser = chooser;

		Locale locale = chooser.getLocale();
		renameErrorTitleText = UIManager.getString("FileChooser.renameErrorTitleText",
				locale);
		renameErrorText = UIManager.getString("FileChooser.renameErrorText", locale);
		renameErrorFileExistsText = UIManager.getString(
				"FileChooser.renameErrorFileExistsText", locale);
	}

	/**
	 * Show error file already exists.
	 * @param oldFileName
	 */
	public void showRenameErrorFileExists(String oldFileName) {
		JOptionPane.showMessageDialog(chooser, MessageFormat.format(
				renameErrorFileExistsText, oldFileName), renameErrorTitleText,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show generic rename error.
	 * @param oldFileName
	 */
	public void showRenameError(String oldFileName) {
		JOptionPane.showMessageDialog(chooser, MessageFormat.format(renameErrorText,
				oldFileName), renameErrorTitleText, JOptionPane.ERROR_MESSAGE);
	}

}
