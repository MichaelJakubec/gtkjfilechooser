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
package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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
