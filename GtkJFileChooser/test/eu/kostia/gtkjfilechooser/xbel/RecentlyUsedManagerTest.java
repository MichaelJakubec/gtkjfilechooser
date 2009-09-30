package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class RecentlyUsedManagerTest {

	@Test
	public void testGetXbel() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator	+ ".recently-used.xbel");
		if (!recentlyUsed.exists()) {
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		}
		RecentlyUsedManager m = new RecentlyUsedManager(recentlyUsed);

		Xbel xbel = m.getXbel();

		List<Bookmark> recentlyUsedEntry = new ArrayList<Bookmark>();
		for (Object entry : xbel.getBookmarkOrFolderOrAliasOrSeparator()) {
			if (entry instanceof Bookmark) {
				Bookmark bookmark = (Bookmark) entry;
				recentlyUsedEntry.add(bookmark);
			}
		}

		Collections.sort(recentlyUsedEntry, new Comparator<Bookmark>() {

			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				// TODO sort by modified date
				return -1;
			}
		});
	}

}
