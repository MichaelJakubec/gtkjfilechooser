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

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.gtkjfilechooser.BookmarkManager;
import com.google.code.gtkjfilechooser.BookmarkManager.GtkBookmark;


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
