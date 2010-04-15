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

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.JLabel;

import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class GtkStockIconTestGui {
	public void testFileIcon(String filename) throws Exception {
		File file = new File(filename);
		Icon icon = GtkStockIcon.get(file, Size.GTK_ICON_SIZE_INVALID);
		if (icon == null) {
			System.err.println("No icon for " + filename);
			return;
		}

		show(createPanel(new JLabel(icon)));

	}

	public void testMagic() throws Exception {
		String file = "/home/c.cerbo/temp/Schermata.png";

		byte[] bytes = new byte[3];
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			is.read(bytes);
		} finally {
			if (is != null) {
				is.close();
			}
		}

		boolean isScript = (bytes[0] == '#' && bytes[1] == '!' && bytes[2] == '/');

		System.out.println("Script? " + isScript);

		System.out.println(new String(bytes));
	}



	public static void main(String[] args) throws Exception {
		GtkStockIconTestGui test = new GtkStockIconTestGui();
		//		test.testFileIcon("/home/c.cerbo/temp/install-lobo-0.98.4.jar");
		//		test.testFileIcon("/home/c.cerbo/Immagini/autoritratti/r0011005.jpg");
		test.testMagic();
	}
}
