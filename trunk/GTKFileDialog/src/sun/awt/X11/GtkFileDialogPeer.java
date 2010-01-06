package sun.awt.X11;

import java.awt.FileDialog;
import java.awt.peer.FileDialogPeer;
import java.io.FilenameFilter;

public class GtkFileDialogPeer implements FileDialogPeer {
	private FileDialog target;
		
	public GtkFileDialogPeer(FileDialog target) {
		super();
		this.target = target;
	}

	native public void init(String title);
	
	native public void setDirectory(String dir);

	native public void setFile(String file);
	
	native public void setMode(int mode);
	
	native protected String run();

	native void setFilenameFilterNative (FilenameFilter filter);

	@Override
	public void setFilenameFilter(FilenameFilter filter) {
		//TODO implement: see gtkfiledialogpeer.c in project classpath
		//http://csourcesearch.com/java/fid6412A0C71A758438F9E09C45D9AD414C004A43AC.aspx?s=GtkFileDialogPeer.java#L1
		//http://csourcesearch.com/c/fid91CC5D776375EFBA9028496BF036728F4A1A5A1A.aspx?s=accept#L74
	}
	
	public void setVisible(boolean vis) {
		String filename = run();
		target.setFile(filename);
	}
}
