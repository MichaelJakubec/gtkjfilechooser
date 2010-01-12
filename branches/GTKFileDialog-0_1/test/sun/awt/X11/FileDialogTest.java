package sun.awt.X11;

import java.awt.FileDialog;

import javax.swing.JFrame;

public class FileDialogTest {
	public static void main(String[] args) {
		FileDialog fd = new FileDialog((JFrame)null);
		
		fd.setVisible(true);
		System.out.println("File: " + fd.getFile());
	}
}
