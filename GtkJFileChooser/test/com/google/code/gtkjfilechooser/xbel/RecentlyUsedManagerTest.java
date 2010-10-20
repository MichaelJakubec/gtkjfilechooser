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
package com.google.code.gtkjfilechooser.xbel;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.code.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedManagerTest {

	@Test
	public void testReadRecentFiles() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator + ".recently-used.xbel");

		int n = 30;
		RecentlyUsedManager m = null;
		if (!recentlyUsed.exists()) {
			m = new RecentlyUsedManager(n) {
				@Override
				protected File getRecentlyUsedFile() {
					return new File("misc/xbel/recently-used-example.xbel");
				}
			};
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		} else {
			m = new RecentlyUsedManager(n);
		}

		int count = 1;
		List<File> fileEntries = m.getRecentFiles();
		Assert.assertEquals(30, fileEntries.size());
		System.out.println("\nSAX");
		for (File file : fileEntries) {			
			System.out.printf("%2d %s \t %tD\n", count, file.getName() , new Date(file.lastModified()));
			count++;
		}
	}

}
