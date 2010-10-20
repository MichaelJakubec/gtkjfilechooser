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
package com.google.code.gtkjfilechooser;

import static com.google.code.gtkjfilechooser.NavigationKeyBinding.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.code.gtkjfilechooser.NavigationKeyBinding;

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
