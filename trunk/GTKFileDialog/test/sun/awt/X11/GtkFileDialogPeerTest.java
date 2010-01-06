package sun.awt.X11;

import java.io.File;

import javax.swing.JFrame;

public class GtkFileDialogPeerTest {
	static {
		System.load(new File("native/bin/libGtkFileDialogPeer.so").getAbsolutePath());
	}
	
	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame();
		
		GtkFileDialog fd = new GtkFileDialog(null, "MY File Dialog");
		
		GtkFileDialogPeer fdp = new GtkFileDialogPeer(fd);
		fdp.init("My File Dialog");
		fdp.setMode(0);		
		
		fdp.setVisible(true);
		
		String filename = fd.getFile();
		if (filename != null) {
			System.out.println("Filename: " + filename);	
		}
		
		//Thread.sleep(1000);
		
		//fdp.setVisible(true);
	}
}
