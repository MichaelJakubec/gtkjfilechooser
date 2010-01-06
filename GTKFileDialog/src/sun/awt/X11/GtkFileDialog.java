package sun.awt.X11;

import java.awt.Dialog;
import java.awt.FileDialog;


public class GtkFileDialog extends FileDialog {

	public GtkFileDialog(Dialog parent, String title) {
		super(parent, title);
	}

	@Override
	public void addNotify() {		
		super.addNotify();
		
		//TODO
		//peer = new GtkFileDialogPeer(this);
	}
}
