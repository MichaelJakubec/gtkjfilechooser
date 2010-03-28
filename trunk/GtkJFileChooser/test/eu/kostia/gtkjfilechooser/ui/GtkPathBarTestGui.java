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

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;



public class GtkPathBarTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		//		File path = new File(".");
		File path = new File("/home/c.cerbo/src/chromium/src");
		final GtkPathBar pane = new GtkPathBar(path);
		pane.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				GtkPathBar src = (GtkPathBar) e.getSource();
				System.out.println(src.getCurrentDirectory());				
			}			
		});


		JButton downButton = new JButton("<<");
		downButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.downFolder();
			}
		});

		JButton upButton = new JButton(">>");
		upButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.upFolder();
			}
		});

		JButton changeDirButton = new JButton("Change Dir");
		changeDirButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//pane.setCurrentDirectory(new File(SystemProperties.JAVA_IO_TMPDIR));
				pane.setCurrentDirectory(new File("/home/c.cerbo/workspaces/dm/dm430/head/Projektplanung/Dokumente/Spezifikationen"));
			}
		});

		show(createPanel(new GridLayout(2,1) , pane, createPanel(downButton, upButton, changeDirButton)));
	}
}
