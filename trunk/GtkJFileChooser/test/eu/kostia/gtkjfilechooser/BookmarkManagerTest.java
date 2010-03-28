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
package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import eu.kostia.gtkjfilechooser.BookmarkManager.GtkBookmark;

public class BookmarkManagerTest {

	@Test
	public void testGetAll() {
		BookmarkManager m = new BookmarkManager();
		List<GtkBookmark> bookmarks = m.getAll();
		for (GtkBookmark bookmark : bookmarks) {
			System.out.println(bookmark);
		}
	}

	@Test
	@Ignore("don't run because of filesystem dependency")
	public void testAdd() {
		BookmarkManager m = new BookmarkManager();
		File dir = new File("/home/c.cerbo/temp/ciao bella");
		m.add(dir, null);
	}


	@Test
	@Ignore("don't run because of filesystem dependency")
	public void testDelete() {
		BookmarkManager m = new BookmarkManager();
		m.delete("shared");
	}

	@Test
	@Ignore("don't run because of filesystem dependency")
	public void testRename() {
		BookmarkManager m = new BookmarkManager();
		m.rename("shared", "my shared");
	}
}
