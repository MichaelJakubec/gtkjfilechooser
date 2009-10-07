package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import eu.kostia.gtkjfilechooser.UrlUtil;

/**
 * Manager for the recently used files.
 * 
 * @author c.cerbo
 * @see http://www.freedesktop.org/wiki/Specifications/desktop-bookmark-spec
 */
public class RecentlyUsedManager {

	static final private Logger LOG = Logger.getLogger(RecentlyUsedManager.class
			.getName());

	private static final String FILE_PROTOCOL = "file://";
	private File recentlyUsedfile;

	public RecentlyUsedManager() {
		this(new File(System.getProperty("user.home") + File.separator
				+ ".recently-used.xbel"));
	}

	RecentlyUsedManager(File recentlyUsed) {
		this.recentlyUsedfile = recentlyUsed;
	}

	/**
	 * Returns the desired number of bookmarks sorted by modified date.
	 * 
	 * @param n
	 *            The desired number of bookmarks.
	 * @return The desired number of recent file entries sorted by modified
	 *         date.
	 */
	public List<File> readRecentFiles(int n) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			RecentFilesHandler handler = new RecentFilesHandler();
			saxParser.parse(recentlyUsedfile, handler);

			List<File> allRecentFiles = handler.getAllRecentFiles();
			Collections.sort(allRecentFiles, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					long date1 = o1.lastModified();
					long date2 = o2.lastModified();
					return (date2 < date1 ? -1 : (date2 == date1 ? 0 : 1));
				}

			});

			if (n >= allRecentFiles.size()) {
				return allRecentFiles;
			}

			return allRecentFiles.subList(0, n);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Recent Files Parse Error", e);
			return new ArrayList<File>();
		}
	}

	private class RecentFilesHandler extends DefaultHandler {
		private final ISO8601DateFormat fmt;
		private final List<File> allRecentFiles;

		public RecentFilesHandler() {
			this.allRecentFiles = new ArrayList<File>();
			this.fmt = new ISO8601DateFormat();
		}

		public List<File> getAllRecentFiles() {
			return allRecentFiles;
		}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attr) {
			if (!"bookmark".equals(name)) {
				return;
			}

			int attrCount = attr.getLength();
			if (attrCount == 0) {
				// no attribute found
				return;
			}

			String href = null;
			Date modified = null;
			for (int i = 0; i < attrCount; i++) {

				// Attribute "href"
				if ("href".equals(attr.getQName(i))) {
					href = attr.getValue(i);

					if (!href.startsWith(FILE_PROTOCOL)) {
						// exclude entries that aren't files.
						return;
					}

					if (href.startsWith(FILE_PROTOCOL + System.getProperty("java.io.tmpdir"))) {
						// exclude temporary files.
						return;
					}

					// decode url
					href = UrlUtil.decode(href.substring(FILE_PROTOCOL.length()));
					if (!new File(href).exists()) {
						// exclude files that don't exist anymore
						return;
					}
				}

				// Attribute "modified"
				if ("modified".equals(attr.getQName(i))) {
					try {
						modified = fmt.parse(attr.getValue(i));
					} catch (ParseException e) {
						// TODO exception handling
						e.printStackTrace();
					}

					// stop the loop, we have that we need (href and modified)!
					break;
				}
			}

			final long modifiedTime = modified.getTime();
			File recentFile = new File(href) {
				private static final long serialVersionUID = 1L;

				@Override
				public long lastModified() {
					return modifiedTime;
				}
			};

			allRecentFiles.add(recentFile);

		}

		@Override
		public void warning(SAXParseException spe) {
			LOG.warning(spe.getMessage());
		}

		@Override
		public void fatalError(SAXParseException spe) throws SAXException {
			throw spe;
		}

	}
}
