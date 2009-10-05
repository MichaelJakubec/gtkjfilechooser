package eu.kostia.gtkjfilechooser;

import java.io.File;

/**
 * Find files and directories than contain the searched term (case insensitive).
 * 
 * @author c.cerbo
 * 
 */
public class FileSearch {
	private String targetdir;
	private String searchterm;
	private FileSearchHandler handler;

	private boolean stop = false;

	public FileSearch(String targetdir, String searchterm, FileSearchHandler handler) {
		this.targetdir = targetdir;
		this.searchterm = searchterm.toLowerCase();
		this.handler = handler;
	}

	/**
	 * Stop the current search.
	 */
	public void stop() {
		this.stop = true;
	}

	/**
	 * Start the search in background. The in-progress results are returned to the {@link FileSearchHandler}.
	 */
	public void start() {
		Thread scanFilesThread = new Thread(new Runnable() {
			@Override
			public void run() {
				scanFiles(new File(targetdir));
			}
		});

		scanFilesThread.start();
	}

	private void scanFiles(File file) {
		if (stop) {
			return;
		}

		if (file.getName().toLowerCase().contains(searchterm)) {
			handler.found(file);
		}

		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					scanFiles(children[i]);
				}
			}
		}
	}

	/**
	 * Inner class FileSearch
	 * 
	 */
	public interface FileSearchHandler {

		/**
		 * Method invoked when a file is found.
		 * 
		 * @param file
		 */
		public void found(File file);
	}
}
