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

import java.io.File;

import javax.swing.JTable;
import javax.swing.text.Position;

public class FileFindAction extends TableFindAction {
	@Override
	public int getNextMatch(JTable table, String prefix, int startIndex,
			Position.Bias bias) {
		
		int max = table.getRowCount();
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		if (startIndex < 0 || startIndex >= max) {
			throw new IllegalArgumentException();
		}

		prefix = prefix.toUpperCase();

		// start search from the next element after the selected element
		int increment = (bias == null || bias == Position.Bias.Forward) ? 1 : -1;
		int index = startIndex;
		do {
			int column = 0;
			File item = (File) table.getValueAt(index, column);

			if (item != null) {
				String text = item.getName();

				text = text.toUpperCase();

				if (text != null && text.startsWith(prefix)) {
					return index;
				}
			}
			index = (index + increment + max) % max;
		} while (index != startIndex);
		return -1;
	}
}
