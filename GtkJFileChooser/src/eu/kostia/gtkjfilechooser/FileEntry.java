package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.util.Date;

/**
 * A wrapper for a {@link File}. The modification date is settable as required by
 * the recently-used files (see ~/.recently-used.xbel)
 * 
 * @author c.cerbo
 * 
 */
public class FileEntry {

	private File file;

	private Date modified;

	public FileEntry() {
		super();
	}

	public FileEntry(File file, Date modified) {
		super();
		this.file = file;
		this.modified = modified;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the time that the file denoted by this abstract pathname was last
	 * modified. Usually the same as {@link File#lastModified()}, except for the
	 * recently-used files.
	 * 
	 * @return The date of the last modification.
	 */
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}
