package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RecentlyUsedManagerTest {

	@Test
	public void testReadRecentFiles() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator
				+ ".recently-used.xbel");
		if (!recentlyUsed.exists()) {
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		}
		RecentlyUsedManager m = new RecentlyUsedManager(recentlyUsed);


		int count = 1;
		List<File> fileEntries = m.readRecentFiles(30);
		Assert.assertEquals(30, fileEntries.size());
		System.out.println("\nJAXB");
		for (File file : fileEntries) {			
			System.out.println(count+") "+file.getName() + "\t" + new Date(file.lastModified()));
			count++;
		}
	}
	
	@Test
	public void testReadRecentFilesSax() throws Exception {
		File recentlyUsed = new File(System.getProperty("user.home") + File.separator
				+ ".recently-used.xbel");
		if (!recentlyUsed.exists()) {
			recentlyUsed = new File("misc/xbel/recently-used-example.xbel");
		}
		RecentlyUsedManager m = new RecentlyUsedManager(recentlyUsed);


		int count = 1;
		List<File> fileEntries = m.readRecentFilesSax(30);
		Assert.assertEquals(30, fileEntries.size());
		System.out.println("\nSAX");
		for (File file : fileEntries) {			
			System.out.println(count+") "+file.getName() + "\t" + new Date(file.lastModified()));
			count++;
		}
	}

}
