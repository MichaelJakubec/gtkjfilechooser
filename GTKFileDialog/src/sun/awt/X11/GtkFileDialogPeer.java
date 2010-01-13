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

	native void start(String title, int mode, String dir, String file, FilenameFilter filter);

	@Override
	public void setFile(String filename) {
		File file = new File(filename);
		fd.setFile(file.getName());
		fd.setDirectory(file.getParent() + File.separator);
	}

	@Override
	public void setDirectory(String dir) {
		// delegated to FileDialog#setDirectory(String)
	}

	@Override
	public void setFilenameFilter(FilenameFilter filter) {
		// delegated to FileDialog#setFilenameFilter(String)		
	}

	public void setVisible(boolean vis) {
		if (vis) {
			start(fd.getTitle(), fd.getMode(), fd.getDirectory(),
					fd.getFile(), fd.getFilenameFilter());
		}
	}

	boolean filenameFilterCallback(String fullname) {		
		if (fd.getFilenameFilter() == null) {
			// no filter, accept all.
			return true;
		}
		
		File file = new File(fullname);
		return fd.getFilenameFilter().accept(new File(file.getParent()), file.getName());
	}

	public void dispose() {
		//TODO dispose
		System.out.println("dispose GtkFileDialogPeer (TODO)");
		//UnsafeXDisposerRecord#dispose
		//ref. http://bugs.sun.com/view_bug.do?bug_id=6853592
		try {
			Class<?> cls = Class.forName("sun.awt.X11.XlibWrapper");
//			long displayString = XlibWrapper.XDisplayString(XToolkit.getDisplay());
//
//	        if (displayString == 0) {
//	            throw new XException("XDisplayString returns NULL");
//	        }
//
//	        long newDisplay = XlibWrapper.XOpenDisplay(displayString);
//	        XlibWrapper.XCloseDisplay(newDisplay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
