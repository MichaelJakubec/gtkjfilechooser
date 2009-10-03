package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RecentlyUsedManagerTest {

	@Test
	public void testReadBookmarks() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator
				+ ".recently-used.xbel");
		if (!recentlyUsed.exists()) {
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		}
		RecentlyUsedManager m = new RecentlyUsedManager(recentlyUsed);


		int count = 1;
		List<Bookmark> bookmarks = m.readBookmarks(30);
		Assert.assertEquals(30, bookmarks.size());
		for (Bookmark bookmark : bookmarks) {			
			String href = bookmark.getHref();
			if (!href.startsWith("file://")) {
				continue;
			}

			File file = new File(href.substring("file://".length()));
			System.out.println(count+") "+file.getName() + "\t" + bookmark.getModified());
			count++;
		}
	}

}
