/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
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
