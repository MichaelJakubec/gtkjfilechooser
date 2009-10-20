package eu.kostia.gtkjfilechooser.ui;

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;

import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedFileWorker extends SwingWorker<Void, Void> implements PropertyChangeListener {

	private static final int NUMBER_OF_RECENT_FILES = 30;

	/**
	 * Manager for the recent used files.
	 */
	private RecentlyUsedManager recentManager;

	private final GtkFileChooserUI fileChooserUI;

	public RecentlyUsedFileWorker(GtkFileChooserUI fileChooserUI) {
		this.fileChooserUI = fileChooserUI;
		addPropertyChangeListener(this);
	}

	@Override
	protected Void doInBackground() throws Exception {
		fileChooserUI.getFileChooser().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (recentManager == null) {
			// RecentlyUsedManager objects are expensive: create them
			// only when needed.
			recentManager = new RecentlyUsedManager(NUMBER_OF_RECENT_FILES);
		}
		List<File> fileEntries = recentManager.getRecentFiles();
		// add files in a loop instead of using
		// recentlyUsedPane#setModel:
		// the user see the progress and hasn't the impression that the
		// GUI is frozen.
		fileChooserUI.getRecentlyUsedPane().getModel().clear();
		for (File file : fileEntries) {
			if(fileChooserUI.getFileChooser().getFileFilter().accept(file)){
				fileChooserUI.getRecentlyUsedPane().addFile(file);
			}					
		}

		return null;
	}


	@Override
	protected void done() {
		fileChooserUI.getFileChooser().setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Log.debug(evt);

	}

}


