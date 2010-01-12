package sun.awt.X11;

import java.io.File;
import java.io.FilenameFilter;

public class GtkFileDialogPeerTest {
	static {
		System.load(new File("native/bin/libGtkFileDialogPeer.so").getAbsolutePath());
		GtkFileDialogPeer.init();
	}

	public static void main(String[] args) throws Exception {
		GtkFileDialog fd = new GtkFileDialog(null, "My File Dialog");
		fd.setMode(GtkFileDialog.SELECT_FOLDER);
		fd.setFilenameFilter(createTextFileFilter());
		fd.setDirectory(System.getProperty("user.home"));

		GtkFileDialogPeer fdp = new GtkFileDialogPeer(fd);

		fdp.setVisible(true);

		String filename = fd.getFile();
		if (filename != null) {
			System.out.println("Filename: " + filename);
		}
	}

	private static FilenameFilter createTextFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// return name.endsWith(".log");
				return name.endsWith(".txt");
			}
		};
	}
}
