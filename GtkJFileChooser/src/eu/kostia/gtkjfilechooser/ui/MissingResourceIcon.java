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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;

public class MissingResourceIcon implements Icon {

	/**
	 * Width
	 */
	private int w;

	/**
	 * Height
	 */
	private int h;

	/**
	 * Margin
	 */
	private int mx = 2;
	private int my = 2;

	public MissingResourceIcon(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public MissingResourceIcon(int x) {
		this(x, x);
	}

	public MissingResourceIcon() {
		this(16, 16);
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		// Use antialiasing.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, w, h);

		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(mx, my, w - 2 * mx, h - 2 * my);

		int stroke = 2;
		int k = 2;
		g2d.setStroke(new BasicStroke(stroke));
		g2d.setColor(Color.RED);
		GeneralPath cross = new GeneralPath();
		cross.moveTo(k * my + stroke, k * mx + stroke);
		cross.lineTo(w - (k * mx + stroke), h - (k * my + stroke));
		cross.moveTo(w - (k * mx + stroke), k * mx + stroke);
		cross.lineTo(k * my + stroke, h - (k * my + stroke));
		g2d.draw(cross);

		g2d.dispose();
	}

	public int getIconWidth() {
		return w;
	}

	public int getIconHeight() {
		return h;
	}

}
