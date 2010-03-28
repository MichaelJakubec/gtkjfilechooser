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
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanelBoxLayout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class JFileChooserLayoutTestGui extends JPanel {
	JPanel cardPanel;
	JComboBox filterComboBox;

	public JFileChooserLayoutTestGui() {
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Top panel", JLabel.CENTER));
		topPanel.setBackground(Color.RED);
		add(topPanel, BorderLayout.PAGE_START);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.YELLOW);

		JPanel leftPanel = new JPanel(new BorderLayout());
		JPanel leftButtonPanel = new JPanel(new GridLayout(3, 1));
		addButton(leftButtonPanel, 1);
		addButton(leftButtonPanel, 2);
		addButton(leftButtonPanel, 3);

		leftPanel.add(leftButtonPanel, BorderLayout.CENTER);
		leftPanel.add(createPanel(new GridLayout(1, 2), new JButton("Add"), new JButton("Remove")), BorderLayout.PAGE_END);

		JPanel rightPanel = new JPanel(new BorderLayout());
		cardPanel = new JPanel(new CardLayout());
		cardPanel.add(new JLabel("Card 1", JLabel.CENTER), "Card 1");
		cardPanel.add(new JLabel("Card 2", JLabel.CENTER), "Card 2");
		cardPanel.add(new JLabel("Card 3", JLabel.CENTER), "Card 3");

		rightPanel.add(cardPanel, BorderLayout.CENTER);
		//		rightPanel.add(createPanel(new GridLayout(1, 3), new JLabel(), new JLabel(), new JComboBox(new String[] {"Item one", "Item two"})), BorderLayout.PAGE_END);
		JComboBox filterComboBox = createComboBox();

		rightPanel.add(createPanelBoxLayout(Box.createHorizontalGlue(), filterComboBox), BorderLayout.PAGE_END);
		rightPanel.setBackground(Color.PINK);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(true);

		//		centerPanel.add(splitPane);
		add(splitPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(new JLabel("Bottom panel", JLabel.CENTER));
		bottomPanel.setBackground(Color.GREEN);
		add(bottomPanel, BorderLayout.PAGE_END);

		setPreferredSize(new Dimension(700, 500));
	}

	private void addButton(JPanel leftButtonPanel, int i) {
		JButton button = new JButton("Card " + i);
		button.setBackground(Color.CYAN);
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				String cardName = button.getText();
				CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
				cardLayout.show(cardPanel, cardName);
				filterComboBox.setSelectedItem(cardName);
			}
		});	
		leftButtonPanel.add(button);
	}

	private JComboBox createComboBox() {
		filterComboBox = new JComboBox(new String[] {"Card 1", "Card 2" ,"Card 3"});

		Dimension size = filterComboBox.getPreferredSize();
		size.width = 150;
		filterComboBox.setPreferredSize(size);
		filterComboBox.setMaximumSize(size);
		filterComboBox.setMinimumSize(size);

		filterComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String cardName = (String) filterComboBox.getSelectedItem();
				CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
				cardLayout.show(cardPanel, cardName);
			}
		});
		return filterComboBox;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		JPanelUtil.show(new JFileChooserLayoutTestGui());
	}
}
