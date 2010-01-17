package sun.awt.X11;

import java.awt.FileDialog;

import javax.swing.JFrame;

public class FileDialogTest {
	public static void main(String[] args) {
		FileDialog fd = new FileDialog((JFrame)null);		
						
		fd.setVisible(true);
		
		System.out.println("Peer: " + fd.getPeer().getClass().getName());
		System.out.println("dir: " + fd.getDirectory());
		System.out.println("file: " + fd.getFile());
		
		fd.dispose();	
	}
}
