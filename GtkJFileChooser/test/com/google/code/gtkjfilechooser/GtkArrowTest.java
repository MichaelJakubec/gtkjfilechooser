/*
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
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 */
package com.google.code.gtkjfilechooser;

import static com.google.code.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static com.google.code.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.google.code.gtkjfilechooser.GtkArrow;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKConstants.ArrowType;


/**
 * @author Costantino Cerbo
 *
 */
public class GtkArrowTest {
	public void testArrows() throws Exception {
		JPanel arrows = new JPanel(new BorderLayout());
		
		arrows.add(button(new GtkArrow(ArrowType.UP)), BorderLayout.NORTH);
		arrows.add(button(new GtkArrow(ArrowType.DOWN)), BorderLayout.SOUTH);
		arrows.add(new JLabel(" "), BorderLayout.CENTER);
		arrows.add(button(new GtkArrow(ArrowType.LEFT)), BorderLayout.WEST);
		arrows.add(button(new GtkArrow(ArrowType.RIGHT)), BorderLayout.EAST);
		show(createPanel(arrows));
	}
	
	private JButton button(JLabel label) {
		JButton btn = new JButton();
		btn.add(label);
		return btn;
	}
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		
		GtkArrowTest test = new GtkArrowTest();
		test.testArrows();
	}
}
