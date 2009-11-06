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

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static java.awt.BorderLayout.LINE_END;
import static java.awt.BorderLayout.LINE_START;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class GtkPathBar extends JPanel {

	private static final String DIRECTORY_INDEX = "directory index";

	private static final int BUTTON_HEIGHT = 34;

	private static final long serialVersionUID = 1L;

	private JToggleButton selectedButton;

	/**
	 * Group of buttons for the directories
	 */
	private ButtonGroup dirButtonsgroup;

	/**
	 * Number of visible buttons
	 */
	static private int VISIBLE_BUTTONS = 6;

	private int currentStartIndex = 0;

	private String[] directories;

	private String currentDirectory;

	private JButton backButton;
	private JPanel buttonsPanel;
	private JButton forwardButton;

	private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	public GtkPathBar(File location) {
		setDirectories(location);

		setLayout(new BorderLayout());

		backButton = createBackButton();
		add(createPanel(new FlowLayout(FlowLayout.LEFT), backButton), LINE_START);

		createButtonsPanel();
		add(createPanel(new FlowLayout(FlowLayout.LEFT), buttonsPanel),	BorderLayout.CENTER);

		forwardButton = createForwardButton();
		add(createPanel(new FlowLayout(FlowLayout.RIGHT), forwardButton), LINE_END);

		int last = directories.length - 1;
		currentStartIndex = last - VISIBLE_BUTTONS + 1;
		showButtons(currentStartIndex, last);
		selectButton(directories.length - 1);
	}

	public void setCurrentDirectory(File location) {
		if (location.equals(getCurrentDirectory())){
			return;
		}

		setDirectories(location);
		createButtonsPanel();
		add(createPanel(new FlowLayout(FlowLayout.RIGHT), forwardButton), LINE_END);

		int last = directories.length - 1;
		currentStartIndex = last - VISIBLE_BUTTONS + 1;
		showButtons(currentStartIndex, last);
		selectButton(directories.length - 1);
		buttonsPanel.validate();
	}

	public File getCurrentDirectory() {
		return new File(currentDirectory);
	}

	public void addActionListener(ActionListener l) {
		actionListeners.add(l);
	}

	public void removeActionListener(ActionListener l) {
		actionListeners.remove(l);
	}

	public List<ActionListener> getActionListeners() {
		return actionListeners;
	}

	private JButton createBackButton() {
		JButton backButton = new JButton("<");
		setStandardHeight(backButton);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentStartIndex--;
				if (currentStartIndex < 0) {
					currentStartIndex = 0;
				}
				showButtons(currentStartIndex, currentStartIndex + VISIBLE_BUTTONS - 1);
			}
		});
		return backButton;
	}

	private void setStandardHeight(JButton backButton) {
		Dimension size = backButton.getPreferredSize();
		size.height = BUTTON_HEIGHT;
		backButton.setPreferredSize(size);
	}

	private void createButtonsPanel() {
		dirButtonsgroup = new ButtonGroup();
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));	
		}

		buttonsPanel.removeAll();

		for (int i = 0; i < directories.length; i++) {
			String dir = directories[i];
			JToggleButton dirButton = new JToggleButton(){				
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					// Must be small enough to not affect total width.
					d.height = BUTTON_HEIGHT;
					return d;
				}
			};

			dirButton.putClientProperty(DIRECTORY_INDEX, i);

			if (dir.equals(File.separator)){
				// Set root icon (the root button has no text)
				dirButton.setIcon(GtkStockIcon.get("gtk-harddisk", Size.GTK_ICON_SIZE_BUTTON));
			} else {
				dirButton.setText(dir);
			}

			File tmp = getDirectory(i);
			if (tmp.equals(new File(System.getProperty("user.home")))){
				// user home icon		
				dirButton.setIcon(GtkStockIcon.get("places/user-home", Size.GTK_ICON_SIZE_BUTTON));
			}

			if(FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP).equals(tmp)) {
				// desktop dir icon
				dirButton.setIcon(GtkStockIcon.get("places/user-desktop", Size.GTK_ICON_SIZE_MENU));
			} 

			dirButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton oldSelectedButton = selectedButton;
					if (oldSelectedButton != null) {
						Font plain = oldSelectedButton.getFont().deriveFont(Font.PLAIN);
						oldSelectedButton.setFont(plain);
						oldSelectedButton.setForeground(Color.BLACK);
					}

					selectedButton = (JToggleButton) e.getSource();
					Font bold = selectedButton.getFont().deriveFont(Font.BOLD);
					selectedButton.setFont(bold);


					// Update current dir
					updateCurrentDir();

					// Foward action to the main action listeners
					for (ActionListener listener : actionListeners) {
						ActionEvent myEvt = new ActionEvent(GtkPathBar.this, 1, "directory-selected");
						listener.actionPerformed(myEvt);
					}
				}
			});

			dirButtonsgroup.add(dirButton);
			buttonsPanel.add(dirButton);
		}		
	}

	private void updateCurrentDir() {
		int dirIndex = (Integer) selectedButton.getClientProperty(DIRECTORY_INDEX);

		if (dirIndex == 0){
			//If zero, it's the root dir (it's the only button with no text but just an icon)
			currentDirectory = File.separator;
			return;			
		}

		StringBuilder sb = new StringBuilder(File.separator);
		for (int i = 1; i <= dirIndex; i++) {
			sb.append(directories[i]);
			sb.append(File.separator);
		}
		currentDirectory = sb.toString();
	}

	private File getDirectory(int n){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= n; i++) {
			sb.append(directories[i]).append(File.separator);
		}
		return new File(sb.toString());
	}

	private JButton createForwardButton() {
		JButton forwardButton = new JButton(">");
		setStandardHeight(forwardButton);
		forwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentStartIndex++;
				if ((currentStartIndex + VISIBLE_BUTTONS - 1) > (directories.length - 1)) {
					currentStartIndex--;
				}

				showButtons(currentStartIndex, currentStartIndex + VISIBLE_BUTTONS - 1);
			}
		});
		return forwardButton;
	}

	private void showButtons(int startIndex, int endIndex) {
		Enumeration<AbstractButton> buttons = dirButtonsgroup.getElements();
		int count = 0;
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			button.setVisible(count >= startIndex && count <= endIndex);
			count++;
		}
	}

	private void selectButton(int index) {
		Enumeration<AbstractButton> buttons = dirButtonsgroup.getElements();
		int count = 0;
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			button.setSelected(count == index);
			if (count == index) {
				button.doClick();
			}

			count++;
		}
	}

	public void upFolder() {
		Enumeration<AbstractButton> buttons = dirButtonsgroup.getElements();
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()){
				if (buttons.hasMoreElements()) {
					AbstractButton nextButton = buttons.nextElement();
					nextButton.doClick();
				}
			}
		}
	}

	public void downFolder() {
		Enumeration<AbstractButton> buttons = dirButtonsgroup.getElements();
		AbstractButton previousButton = null;
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()){
				if (previousButton != null) {
					previousButton.doClick();
				}
			}

			previousButton = button;
		}
	}

	private void setDirectories(File location) throws IOError {
		if (location == null){
			throw new IllegalArgumentException("The location cannot be null!");
		}

		File parentDir = location.isDirectory() ? location : location.getParentFile();

		String parentDirPath = null;
		try {
			parentDirPath = parentDir.getCanonicalPath();
		} catch (IOException e) {
			throw new IOError(e);
		}
		currentDirectory = parentDirPath;

		if (parentDirPath.startsWith(File.separator)) {
			parentDirPath = "x" + parentDirPath;
		}
		String[] dirs = parentDirPath.split(Pattern.quote(File.separator));

		//Add Root dir
		dirs[0] = File.separator;

		directories = dirs;
	}
}
