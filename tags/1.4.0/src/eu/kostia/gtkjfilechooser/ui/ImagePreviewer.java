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

import static javax.swing.JFileChooser.SELECTED_FILE_CHANGED_PROPERTY;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel to provide {@link JFileChooser} a preview for images. Simply set it as
 * accessory component:
 * 
 * <pre>
 * JFileChooser chooser = new JFileChooser();
 * chooser.setAccessory(new ImagePreviewer(chooser));
 * </pre>
 * 
 * @author Costantino Cerbo
 * 
 */
public class ImagePreviewer extends JPanel implements PropertyChangeListener {
	private static final int SCALED_WIDTH = 180;
	private static final int OFFSET = 20;
	ImageIcon thumbnail = null;
	private JLabel filenameLabel;
	private JLabel previewLabel;

	public ImagePreviewer(JFileChooser fc) {
		filenameLabel = new JLabel("", JLabel.CENTER);
		previewLabel = new JLabel("", JLabel.CENTER);

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(SCALED_WIDTH + OFFSET, -1));
		fc.addPropertyChangeListener(this);
		setVisible(false);

		add(filenameLabel, BorderLayout.PAGE_START);
		add(previewLabel, BorderLayout.CENTER);
	}

	public void loadImage(File f) {
		if (f == null) {
			thumbnail = null;
		} else {
			ImageIcon tmpIcon = new ImageIcon(f.getPath());
			if (tmpIcon.getIconWidth() > SCALED_WIDTH) {
				Image scaled = tmpIcon.getImage().getScaledInstance(SCALED_WIDTH, -1,
						Image.SCALE_FAST);
				thumbnail = new ImageIcon(scaled);
			} else {
				thumbnail = tmpIcon;
			}

			setVisible(thumbnail.getIconWidth() != -1);
			filenameLabel.setText(f.getName());
			previewLabel.setIcon(thumbnail);
		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();
		if (SELECTED_FILE_CHANGED_PROPERTY.equals(property)) {
			File file = (File) e.getNewValue();
			loadImage(file);
			repaint();
		}
	}
}