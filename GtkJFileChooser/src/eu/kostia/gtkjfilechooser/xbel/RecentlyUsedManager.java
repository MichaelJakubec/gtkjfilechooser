package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Manager for the recently used files.
 * 
 * @author c.cerbo
 * @see http://www.freedesktop.org/wiki/Specifications/desktop-bookmark-spec
 */
public class RecentlyUsedManager {

	private File recentlyUsedfile;

	public RecentlyUsedManager() {
		this(new File(System.getProperty("user.home") + File.separator	+ ".recently-used.xbel"));
	}

	RecentlyUsedManager(File recentlyUsed) {
		this.recentlyUsedfile = recentlyUsed;
	}

	private Xbel readXbel() {
		try {
			JAXBContext context = JAXBContext.newInstance(Xbel.class);

			Unmarshaller unm = context.createUnmarshaller();

			return (Xbel) unm.unmarshal(recentlyUsedfile);
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Returns the desired number of bookmarks sorted by modified date.
	 * 
	 * @param n The desired number of bookmarks.
	 * @return The desired number of bookmarks sorted by modified date.
	 */
	public List<Bookmark> readBookmarks(int n) {
		List<Bookmark> allBookmarks = readXbel().getBookmarks();
		
		int lastIndex = allBookmarks.size() - 1;
		int limit = n <= lastIndex ? lastIndex - n + 1 : 0;
		
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		for (int i = lastIndex; i >= limit; i--) {
			if (!allBookmarks.get(i).getHref().startsWith("file://")) {
				i++;
				continue;
			}
			
			bookmarks.add(allBookmarks.get(i));
		}
		
		return bookmarks;
	}

}
