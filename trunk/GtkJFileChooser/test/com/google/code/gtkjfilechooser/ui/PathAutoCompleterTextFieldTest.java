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
package com.google.code.gtkjfilechooser.ui;

import static com.google.code.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static com.google.code.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.UIManager;

import com.google.code.gtkjfilechooser.ui.PathAutoCompleter;
import com.google.code.gtkjfilechooser.ui.JPanelUtil.PanelElement;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;


public class PathAutoCompleterTextFieldTest {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		final JTextField textField = new JTextField(20);
		new PathAutoCompleter(textField);

		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ActionListener: " + textField.getText());				
			}
		});

		show(createPanel(new PanelElement(textField, BorderLayout.PAGE_START)));
	}
}
