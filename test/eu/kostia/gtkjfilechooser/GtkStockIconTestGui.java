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
