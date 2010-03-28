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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Expander extends JComponent implements PropertyChangeListener {
	static final public String EXPANDED_STATUS_CHANGED = "expanded_status_changed";
	private JLabel label;
	private JComponent component;
	private boolean expanded = false;

	public Expander(String text, JComponent aComponent) {
		this.component = aComponent;
		addPropertyChangeListener(this);

		setLayout(new BorderLayout());

		label = new JLabel(text);
		label.setIcon(new ExpanderIcon(false, false));

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				expanded = !expanded;
				firePropertyChange(EXPANDED_STATUS_CHANGED, !expanded, expanded);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setOpaque(true);
				label.setBackground(new Color(241, 238, 233));
				label.setIcon(expanded ? new ExpanderIcon(true, true) : new ExpanderIcon(
						false, true));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setOpaque(false);
				label.setBackground(UIManager.getColor("Label.background"));
				label.setIcon(expanded ? new ExpanderIcon(true, false)
				: new ExpanderIcon(false, false));
			}
		});

		add(label, BorderLayout.PAGE_START);

		component.setVisible(false);
		add(component, BorderLayout.CENTER);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();

		if (EXPANDED_STATUS_CHANGED.equals(property)) {
			doStatusChanged();
		}
	}

	public void setExpanded(boolean expanded) {
		boolean oldValue = this.expanded;
		boolean newValue = expanded;
		this.expanded = expanded;

		firePropertyChange(EXPANDED_STATUS_CHANGED, oldValue, newValue);
	}

	public boolean isExpanded() {
		return expanded;
	}

	private void doStatusChanged() {
		component.setVisible(expanded);
		label.setIcon(expanded ? new ExpanderIcon(true, true) : new ExpanderIcon(false,
				true));
	}
}
