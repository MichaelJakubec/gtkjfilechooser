package sun.awt.X11;

import java.awt.FileDialog;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;

public class GtkFileDialogPeer implements FileDialogPeer {
	private FileDialog target;

	public GtkFileDialogPeer(FileDialog target) {
		super();
		this.target = target;
	}

	static native void init();

	native void start(String title, int mode, String dir, String file, FilenameFilter filter);

	@Override
	public void setFile(String filename) {
		File filen = new File(filename);
		target.setFile(filen.getName());
		target.setDirectory(filen.getParent() + File.separator);
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
			start(target.getTitle(), target.getMode(), target.getDirectory(),
					target.getFile(), target.getFilenameFilter());
		}
	}

	boolean filenameFilterCallback(String fullname) {		
		if (target.getFilenameFilter() == null) {
			// no filter, accept all.
			return true;
		}
		
		File filen = new File(fullname);
		return target.getFilenameFilter().accept(new File(filen.getParent()), filen.getName());
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
