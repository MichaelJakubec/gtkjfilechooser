package sun.awt.X11;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class GtkFileDialog extends FileDialog {
	static {
		System.load(new File("native/bin/libGtkFileDialogPeer.so").getAbsolutePath());
		System.load(new File("native/bin/libGtk2Interface.so").getAbsolutePath());
	}

	private static final long serialVersionUID = 1L;

	static public int SELECT_FOLDER = 2;
	static public int CREATE_FOLDER = 3;

	private int mode;
	private GtkFileDialogPeer peer;

	public GtkFileDialog(Frame parent, String title) {
		super(parent, title);
		this.peer = new GtkFileDialogPeer(this);
	}

	@Override
	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public int getMode() {
		return mode;
	}

	@Override
	public void addNotify() {
		// do nothing
	}

	@Override
	public void setVisible(boolean b) {
		this.peer.setVisible(b);
	}
	
}
