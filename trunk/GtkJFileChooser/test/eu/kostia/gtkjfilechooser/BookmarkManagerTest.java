package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import eu.kostia.gtkjfilechooser.BookmarkManager;
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
