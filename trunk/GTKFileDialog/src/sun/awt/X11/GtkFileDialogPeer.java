package sun.awt.X11;

import java.awt.FileDialog;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;

public class GtkFileDialogPeer implements FileDialogPeer {
	private FileDialog fd;

	public GtkFileDialogPeer(FileDialog fd) {
		super();
		this.fd = fd;
	}

	static native void init();

	native String start(String title, int mode, String dir, String file, FilenameFilter filter);

	@Override
	public void setFile(String file) {
		// delegated to FileDialog#setFile(String)
	}

	@Override
	public void setDirectory(String dir) {
		// delegated to FileDialog#setDirectory(String)
	}

	@Override
	public void setFilenameFilter(FilenameFilter filter) {
		// delegated to FileDialog#setFilenameFilter(String)
		// TODO implement: see gtkfiledialogpeer.c in project classpath
		// http://csourcesearch.com/java/fid6412A0C71A758438F9E09C45D9AD414C004A43AC.aspx?s=GtkFileDialogPeer.java#L1
		// http://csourcesearch.com/c/fid91CC5D776375EFBA9028496BF036728F4A1A5A1A.aspx?s=accept#L74
	}

	public void setVisible(boolean vis) {
		String filename = start(fd.getTitle(), fd.getMode(), fd.getDirectory(),
				fd.getFile(), fd.getFilenameFilter());
		
		fd.setFile(filename);
	}

	/*
	 * This method interacts with the native callback function of the same name.
	 * The native function will extract the filename from the GtkFileFilterInfo
	 * object and send it to this method, which will in turn call the filter's
	 * accept() method and give back the return value.
	 */
	boolean filenameFilterCallback(String fullname) {		
		if (fd.getFilenameFilter() == null) {
			// no filter, accept all.
			return true;
		}
		
		String filename = fullname.substring(fullname.lastIndexOf(File.separator) + 1);
		String dirname = fullname.substring(0, fullname.lastIndexOf(File.separator));

		return fd.getFilenameFilter().accept(new File(dirname), filename);
	}	
}
