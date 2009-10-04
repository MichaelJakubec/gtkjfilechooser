package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	private static final String FILE_PROTOCOL = "file://";
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
		Collections.sort(allBookmarks, new Comparator<Bookmark>() {
			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				return o2.getModified().compareTo(o1.getModified());
			}
		});

		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		for (Bookmark bookmark : allBookmarks) {
			String href = bookmark.getHref();
			if (!href.startsWith(FILE_PROTOCOL)) {
				continue;
			}

			if (new File(href.substring(FILE_PROTOCOL.length())).exists()){
				bookmarks.add(bookmark);
			}

			if (bookmarks.size() == n) {
				break;
			}
		}

		return bookmarks;
	}

}
