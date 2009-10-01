package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;

import org.junit.Test;

public class RecentlyUsedManagerTest {

	@Test
	public void testGetXbel() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator
				+ ".recently-used.xbel");
		if (!recentlyUsed.exists()) {
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		}
		RecentlyUsedManager m = new RecentlyUsedManager(recentlyUsed);

		Xbel xbel = m.getXbel();
		for (Bookmark bookmark : xbel.getBookmarks()) {			
			String href = bookmark.getHref();
			if (!href.startsWith("file://")) {
				continue;
			}

			File file = new File(href.substring("file://".length()));
			System.out.println(file.getName() + "\t" + bookmark.getModified());
		}
	}

}
