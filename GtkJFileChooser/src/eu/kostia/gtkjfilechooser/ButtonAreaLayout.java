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
package eu.kostia.gtkjfilechooser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * <code>ButtonAreaLayout</code> behaves in a similar manner to
 * <code>FlowLayout</code>. It lays out all components from left to right,
 * flushed right. The widths of all components will be set to the largest
 * preferred size width.
 */
public class ButtonAreaLayout implements LayoutManager {
	private int hGap = 5;
	private int topMargin = 17;

	public void addLayoutComponent(String string, Component comp) {
	}

	public void layoutContainer(Container container) {
		Component[] children = container.getComponents();

		if (children != null && children.length > 0) {
			int numChildren = children.length;
			Dimension[] sizes = new Dimension[numChildren];
			Insets insets = container.getInsets();
			int yLocation = insets.top + topMargin;
			int maxWidth = 0;

			for (int counter = 0; counter < numChildren; counter++) {
				sizes[counter] = children[counter].getPreferredSize();
				maxWidth = Math.max(maxWidth, sizes[counter].width);
			}
			int xLocation, xOffset;
			if (container.getComponentOrientation().isLeftToRight()) {
				xLocation = container.getSize().width - insets.left - maxWidth;
				xOffset = hGap + maxWidth;
			} else {
				xLocation = insets.left;
				xOffset = -(hGap + maxWidth);
			}
			for (int counter = numChildren - 1; counter >= 0; counter--) {
				children[counter].setBounds(xLocation, yLocation, maxWidth,
						sizes[counter].height);
				xLocation -= xOffset;
			}
		}
	}

	public Dimension minimumLayoutSize(Container c) {
		if (c != null) {
			Component[] children = c.getComponents();

			if (children != null && children.length > 0) {
				int numChildren = children.length;
				int height = 0;
				Insets cInsets = c.getInsets();
				int extraHeight = topMargin + cInsets.top + cInsets.bottom;
				int extraWidth = cInsets.left + cInsets.right;
				int maxWidth = 0;

				for (int counter = 0; counter < numChildren; counter++) {
					Dimension aSize = children[counter].getPreferredSize();
					height = Math.max(height, aSize.height);
					maxWidth = Math.max(maxWidth, aSize.width);
				}
				return new Dimension(extraWidth + numChildren * maxWidth
						+ (numChildren - 1) * hGap, extraHeight + height);
			}
		}
		return new Dimension(0, 0);
	}

	public Dimension preferredLayoutSize(Container c) {
		return minimumLayoutSize(c);
	}

	public void removeLayoutComponent(Component c) {
	}
}
