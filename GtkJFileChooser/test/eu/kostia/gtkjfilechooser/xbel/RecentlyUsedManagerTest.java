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
