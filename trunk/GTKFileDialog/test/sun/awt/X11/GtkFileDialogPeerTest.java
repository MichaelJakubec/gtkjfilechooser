package sun.awt.X11;

import java.io.File;
import java.io.FilenameFilter;

public class GtkFileDialogPeerTest {
	static {
		System.load(new File("native/bin/libGtkFileDialogPeer.so").getAbsolutePath());
	}
	
	public static void main(String[] args) throws Exception {		
		GtkFileDialog fd = new GtkFileDialog(null, "My File Dialog");
		
		GtkFileDialogPeer fdp = new GtkFileDialogPeer(fd);
		fdp.init("My File Dialog");
		fdp.setMode(0);		
		
		//fdp.setFilenameFilter(createTextFileFilter());
		fdp.setVisible(true);
		
		String filename = fd.getFile();
		if (filename != null) {
			System.out.println("Filename: " + filename);	
		}
		
		fdp.destroy();
	}

	private static FilenameFilter createTextFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {				
				return name.endsWith(".txt");
			}			
		};
	}
}
