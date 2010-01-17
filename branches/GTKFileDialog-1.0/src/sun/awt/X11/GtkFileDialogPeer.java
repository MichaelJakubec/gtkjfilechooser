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

	native void start(String title, int mode, String dir, String file, FilenameFilter filter);

	void setFileInternal(String filename) {
		if (filename == null || filename.trim().isEmpty()) {
			target.setFile(null);
			target.setDirectory(null);
		} else {
			File filen = new File(filename);
			target.setFile(filen.getName());
			target.setDirectory(filen.getParent() + File.separator);
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

	@Override
	public void setVisible(boolean b) {
		if (b) {
			start(target.getTitle(), target.getMode(), target.getDirectory(),
					target.getFile(), target.getFilenameFilter());
		}
	}
	
	@Override
	public void setFile(String file) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDirectory(String dir) {
		// delegated to FileDialog#setDirectory(String)
	}

	@Override
	public void setFilenameFilter(FilenameFilter filter) {
		// delegated to FileDialog#setFilenameFilter(String)		
	}

}
