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

import static eu.kostia.gtkjfilechooser.NavigationKeyBinding.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NavigationKeyBindingTestGui extends JFrame implements ActionListener {

	public NavigationKeyBindingTestGui() {
		setTitle("NavigationKeyBindingTestGui");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();

		for (int i = 0; i < 10; i++) {
			panel.add(new JButton("Button " + i));
		}
		panel.setPreferredSize(new Dimension(500, 500));
		panel.setBackground(Color.RED);
		getContentPane().add(panel);
		pack();

		NavigationKeyBinding keyBinding = new NavigationKeyBinding(panel);
		keyBinding.addActionListener(this);
	}

	public static void main(String[] args) {
		new NavigationKeyBindingTestGui().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		System.out.println("Signal: " + cmd);

		if (LOCATION_POPUP.equals(cmd)) {
			System.out.println(cmd);
		} else if (UP_FOLDER.equals(cmd)) {
			System.out.println(cmd);
		} else if (DOWN_FOLDER.equals(cmd)) {
			System.out.println(cmd);
		} else if (HOME_FOLDER.equals(cmd)) {
			System.out.println(cmd);
		} else if (DESKTOP_FOLDER.equals(cmd)) {
			System.out.println(cmd);
		} else if (QUICK_BOOKMARK.equals(cmd)) {
			System.out.println(cmd + ": " + e.getID());
		}
	}

	static public Object invokeMethod(Object obj, String cmd, int id) {
		try {
			String methodName = toMethodName(cmd);
			Method method = obj.getClass().getMethod(methodName, Integer.class);
			return method.invoke(obj, id);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	static private String toMethodName(String name) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (Character.isLetter(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

}
