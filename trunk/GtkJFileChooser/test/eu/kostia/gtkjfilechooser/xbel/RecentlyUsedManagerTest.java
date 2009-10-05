package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.kostia.gtkjfilechooser.FileEntry;

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
		List<FileEntry> fileEntries = m.readRecentFiles(30);
		Assert.assertEquals(30, fileEntries.size());
		for (FileEntry fileEntry : fileEntries) {			
			System.out.println(count+") "+fileEntry.getFile().getName() + "\t" + fileEntry.getModified());
			count++;
		}
	}

}
