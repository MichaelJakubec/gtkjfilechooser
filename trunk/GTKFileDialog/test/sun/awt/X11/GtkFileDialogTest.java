package sun.awt.X11;

import sun.awt.X11.GtkFileDialog;

public class GtkFileDialogTest {
	public static void main(String[] args) {
		GtkFileDialog fd = new GtkFileDialog(null, "My File Dialog");
		fd.setVisible(true);
		System.out.println("File: " + fd.getFile());
	}
}
