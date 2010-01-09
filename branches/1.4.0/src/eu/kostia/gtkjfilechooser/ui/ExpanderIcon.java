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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;

public class ExpanderIcon implements Icon {

	/**
	 * Width
	 */
	private int w = 18;

	/**
	 * Height
	 */
	private int h = 18;

	/**
	 * Margin
	 */
	private int mx = 5;
	private int my = 2;

	public enum Orientation {
		RIGHT, DOWN
	};

	private boolean down = false;
	public boolean filled = false;

	public ExpanderIcon(boolean down, boolean filled) {
		this.down = down;
		this.filled = filled;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		// Use antialiasing.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		GeneralPath triangle = new GeneralPath();
		if (down) {
			triangle.moveTo(my, mx);
			triangle.lineTo(w - my, mx);
			triangle.lineTo(w/2, h-mx);
			triangle.closePath();
		} else {
			triangle.moveTo(mx, my);
			triangle.lineTo(w - mx, h / 2);
			triangle.lineTo(mx, h - my);
			triangle.closePath();	
		}

		if (filled) {
			g2d.fill(triangle);
		} else {
			g2d.draw(triangle);
		}

		g2d.dispose();
	}

	public int getIconWidth() {
		return down ? h : w;
	}

	public int getIconHeight() {
		return down ? w : h;
	}

}