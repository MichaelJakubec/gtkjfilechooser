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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;

import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.Path;
import eu.kostia.gtkjfilechooser.BookmarkManager.GtkBookmark;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class FileComboBoxRenderer extends JLabel implements ListCellRenderer, UIResource {

	/**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the
	 * <code>getListCellRendererComponent</code> method and set the border of
	 * the returned component directly.
	 */
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(LowerBorder.INSETS);
	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(LowerBorder.INSETS);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	private JComboBox comboBox;

	public FileComboBoxRenderer(JComboBox comboBox) {
		super();
		this.comboBox = comboBox;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		setName("ComboBox.listRenderer");
		
		// Set the height
		Dimension size = getPreferredSize();
		size.height = 29;
		setPreferredSize(size);
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());

		} else {
			setBackground(list.getSelectionForeground());
			setForeground(list.getForeground());
		}

		setFont(list.getFont());

		if (value instanceof Path) {
			Path path = (Path) value;

			setText(path.getName());
			setIcon(GtkStockIcon.get(path.getIconName(), Size.GTK_ICON_SIZE_MENU));
		}

		// Manage the internal disabling of the items
		if (comboBox != null) {
			setEnabled(comboBox.isEnabled());
			setComponentOrientation(comboBox.getComponentOrientation());
		}

		// Manage the border in the items
		Border border = null;
		if (cellHasFocus) {
			if (isSelected) {
				
				border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = UIManager.getBorder("List.focusCellHighlightBorder");
			}
		} else {
			border = getNoFocusBorder();
		}
		setBorder(border);

		if ((index + 1) < list.getModel().getSize()) { // if has next row
			Object nextValue = list.getModel().getElementAt(index + 1);
			if (!(value instanceof GtkBookmark) && nextValue instanceof GtkBookmark) {
				setBorder(new LowerBorder(Color.GRAY, 1));
			}
		}

		return this;
	}

	private Border getNoFocusBorder() {
		Border border = UIManager.getBorder("List.cellNoFocusBorder");
		if (System.getSecurityManager() != null) {
			if (border != null) return border;
			return SAFE_NO_FOCUS_BORDER;
		} else {
			if (border != null
					&& (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
				return border;
			}
			return noFocusBorder;
		}
	}

}
