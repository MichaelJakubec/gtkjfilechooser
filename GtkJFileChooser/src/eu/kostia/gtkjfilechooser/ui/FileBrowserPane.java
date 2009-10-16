package eu.kostia.gtkjfilechooser.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eu.kostia.gtkjfilechooser.Log;
/**
 * File browser
 * 
 * @author Costantino Cerbo
 *
 */


public class FileBrowserPane extends FilesListPane {
	//TODO fire all the following property changes:

	//	/** Identifies user's directory change. */
	//	public static final String DIRECTORY_CHANGED_PROPERTY = "directoryChanged";
	//
	//	/** Identifies change in user's single-file selection. */
	//	public static final String SELECTED_FILE_CHANGED_PROPERTY = "SelectedFileChangedProperty";
	//
	//	/** Identifies change in user's multiple-file selection. */
	//	public static final String SELECTED_FILES_CHANGED_PROPERTY = "SelectedFilesChangedProperty";
	//
	//	/** Enables multiple-file selections. */
	//	public static final String MULTI_SELECTION_ENABLED_CHANGED_PROPERTY = "MultiSelectionEnabledChangedProperty";
	//
	//	/**
	//	 * Says that a different object is being used to find available drives
	//	 * on the system. 
	//	 */
	//	public static final String FILE_SYSTEM_VIEW_CHANGED_PROPERTY = "FileSystemViewChanged";
	//
	//	/**
	//	 * Says that a different object is being used to retrieve file
	//	 * information. 
	//	 */
	//	public static final String FILE_VIEW_CHANGED_PROPERTY = "fileViewChanged";
	//
	//	/** Identifies a change in the display-hidden-files property. */
	//	public static final String FILE_HIDING_CHANGED_PROPERTY = "FileHidingChanged";
	//
	//	/** User changed the kind of files to display. */
	//	public static final String FILE_FILTER_CHANGED_PROPERTY = "fileFilterChanged";
	//
	//	/**
	//	 * Identifies a change in the kind of selection (single,
	//	 * multiple, etc.). 
	//	 */
	//	public static final String FILE_SELECTION_MODE_CHANGED_PROPERTY = "fileSelectionChanged";
	//
	//	/**
	//	 * Says that a different accessory component is in use
	//	 * (for example, to preview files). 
	//	 */
	//	public static final String ACCESSORY_CHANGED_PROPERTY = "AccessoryChangedProperty";
	//
	//	/**
	//	 * Identifies whether a the AcceptAllFileFilter is used or not. 
	//	 */
	//	public static final String ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY = "acceptAllFileFilterUsedChanged";
	//
	//	/** Identifies a change in the dialog title. */
	//	public static final String DIALOG_TITLE_CHANGED_PROPERTY = "DialogTitleChangedProperty";
	//
	//	/**
	//	 * Identifies a change in the type of files displayed (files only,
	//	 * directories only, or both files and directories). 
	//	 */
	//	public static final String DIALOG_TYPE_CHANGED_PROPERTY = "DialogTypeChangedProperty";
	//
	//	/** 
	//	 * Identifies a change in the list of predefined file filters
	//	 * the user can choose from.
	//	 */
	//	public static final String CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY = "ChoosableFileFilterChangedProperty";

	private File currentDir;

	protected boolean showHidden = false;

	protected boolean isMultiSelectionEnabled = false;

	protected int fileSelectionMode = JFileChooser.FILES_ONLY;

	private FileFilter acceptAll = new FileFilter() {

		@Override
		public boolean accept(File file) {
			return !showHidden ? !file.isHidden() : true;
		}
	};

	private FileFilter currentFilter = acceptAll;

	public FileBrowserPane(File startDir) {
		this.currentDir = startDir;

		File[] files = currentDir.listFiles(currentFilter);
		if (files != null) {
			setModel(Arrays.asList(files));
		}

		addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane) e.getSource();
				String cmd = e.getActionCommand();
				File selectedFile = p.getSelectedFile();
				Log.debug(cmd, ": ", selectedFile);

				if (FilesListPane.DOUBLE_CLICK.equals(cmd)) {
					if (fileSelectionMode == JFileChooser.FILES_ONLY
							&& selectedFile.isDirectory()) {
						setCurrentDir(selectedFile);
					}
				}
			}
		});

		// Move to FilesListPane
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				firePropertyChange(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, null, getSelectedFile());
			}
		});
	}

	private void listDirectory(File dir){
		if (!dir.exists()) {
			throw new IllegalArgumentException(dir + " doesn't exist.");
		}

		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(dir + " isn't a directory.");
		}
		getModel().clear();
		File[] files = dir.listFiles(currentFilter);
		if (files != null) {
			for (File file : files) {
				getModel().addFile(file);
			}
		}
	}

	public File getCurrentDir() {
		return currentDir;
	}

	/**
	 * Set the current dir and update the view.
	 * @param currentDir
	 */
	public void setCurrentDir(File currentDir) {
		Object oldValue = this.currentDir;
		Object newValue = currentDir;

		this.currentDir = currentDir;
		listDirectory(currentDir);

		firePropertyChange(JFileChooser.DIRECTORY_CHANGED_PROPERTY, oldValue, newValue);
	}

}