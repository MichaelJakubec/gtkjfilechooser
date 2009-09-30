package eu.kostia.gtkjfilechooser.xbel;

import java.io.File;

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

	private Xbel xbel;

	public RecentlyUsedManager() {
		this(new File(System.getProperty("user.home") + File.separator	+ ".recently-used.xbel"));
	}

	RecentlyUsedManager(File recentlyUsed) {
		try {
			JAXBContext context = JAXBContext.newInstance(Xbel.class);

			Unmarshaller unm = context.createUnmarshaller();

			xbel = (Xbel) unm.unmarshal(recentlyUsed);
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}
	}

	public Xbel getXbel() {
		return xbel;
	}

}
