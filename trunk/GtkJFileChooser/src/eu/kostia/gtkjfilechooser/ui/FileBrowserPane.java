package eu.kostia.gtkjfilechooser.ui;

import static javax.swing.JFileChooser.*;
import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;
/**
 * File browser
 * 
 * @author Costantino Cerbo
 *
 */

//TODO add popup here?
public class FileBrowserPane extends FilesListPane implements PropertyChangeListener{

	private File currentDir;

	private boolean showHidden = false;

	private boolean isMultiSelectionEnabled = false;

	private int fileSelectionMode = FILES_ONLY;

	private FileFilter acceptAll = new FileFilter() {

		@Override
		public boolean accept(File file) {
			return !showHidden ? !file.isHidden() : true;
		}
	};

	private FileFilter currentFilter = acceptAll;

	public FileBrowserPane(File startDir) {
		addPropertyChangeListener(this);
		this.currentDir = startDir;

		File[] files = currentDir.listFiles(currentFilter);
		if (files != null) {
			setModel(Arrays.asList(files));
		}

		addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();

				if (FilesListPane.DOUBLE_CLICK.equals(cmd)) {
					maybeApproveSelection();
				}
			}
		});

		// Move to FilesListPane
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (table.getSelectedRowCount() > 1) {
					firePropertyChange(SELECTED_FILES_CHANGED_PROPERTY, null, getSelectedFiles());
				} else {
					firePropertyChange(SELECTED_FILE_CHANGED_PROPERTY, null, getSelectedFile());	
				}				
			}
		});

		bindKeyAction();

		doMultiSelectionEnabledChanged(isMultiSelectionEnabled);
	}

	/**
	 * The following action are binded to the following keys
	 * <ul>
	 * <li><i>up-folder:</i> Alt+Up[b] ; Backspace</li>
	 * <li><i>down-folder:</i> Alt+Down</li>
	 * <li><i>home-folder:</i>  Alt+Home</li>
	 * <li><i>location-popup:</i> Control+L (empty path); / (path of "/")[a]; ~ (path of "~")</li>
	 * <li><i>desktop-folder:</i> Alt+D</li>
	 * <li><i>quick-bookmark:</i> Alt+1 through Alt+0</li>	 * 
	 * </ul>
	 */
	private void bindKeyAction() {
		// On enter pressed, approve the selection or go into the selected dir.
		bind(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new AbstractAction("maybeApproveSelection") {
			@Override
			public void actionPerformed(ActionEvent e) {
				maybeApproveSelection();
			}
		});

		// Home-folder: Alt+Home
		KeyStroke altHome = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.ALT_DOWN_MASK);
		bind(altHome, new AbstractAction("Go to Home folder") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDirectoryChanged(new File(System.getProperty("user.home")));
			}
		});

		// Desktop-folder: Alt+D
		//TODO disable incremental search
		KeyStroke altD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK);
		bind(altD, new AbstractAction("Go to Desktop") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDirectoryChanged(FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP));
			}
		});
	}

	private void bind(KeyStroke key, Action action) {
		String name = (String) action.getValue(Action.NAME);
		if (name == null) {
			throw new IllegalArgumentException("The action must have a name.");
		}

		table.getInputMap().put(key, name);
		table.getActionMap().put(name, action);
	}

	private void listDirectory(File dir, FileFilter filter){
		if (!dir.exists()) {
			throw new IllegalArgumentException(dir + " doesn't exist.");
		}

		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(dir + " isn't a directory.");
		}
		getModel().clear();
		File[] files = dir.listFiles(filter);
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

		firePropertyChange(DIRECTORY_CHANGED_PROPERTY, oldValue, newValue);
	}

	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
		firePropertyChange(FILE_HIDING_CHANGED_PROPERTY, !showHidden, showHidden);
	}

	public void setIsMultiSelectionEnabled(boolean enabled){
		this.isMultiSelectionEnabled = enabled;
		firePropertyChange(MULTI_SELECTION_ENABLED_CHANGED_PROPERTY, !enabled, enabled);
	}

	public void setCurrentFilter(FileFilter currentFilter) {
		Object oldValue = this.currentFilter;
		Object newValue = currentFilter;

		this.currentFilter = currentFilter;

		firePropertyChange(FILE_FILTER_CHANGED_PROPERTY, oldValue, newValue);
	}

	//TODO move to FilesListpane?
	public void setFileSelectionMode(int fileSelectionMode) {
		int oldValue = this.fileSelectionMode;
		int newValue = fileSelectionMode;

		this.fileSelectionMode = fileSelectionMode;
		firePropertyChange(FILE_SELECTION_MODE_CHANGED_PROPERTY, oldValue, newValue);
	}

	/**
	 * Property listeners
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		Object value = evt.getNewValue();

		if (MULTI_SELECTION_ENABLED_CHANGED_PROPERTY.equals(property)) {
			doMultiSelectionEnabledChanged((Boolean)value);
		} else if (FILE_FILTER_CHANGED_PROPERTY.equals(property)){
			doFileFilerChanged((FileFilter)value);
		} else if (DIRECTORY_CHANGED_PROPERTY.equals(property)){
			doDirectoryChanged((File)value);// 
		} else if (FILE_SELECTION_MODE_CHANGED_PROPERTY.equals(property)){
			doFileSelectionModeChanged((Integer)value);
		}

	}

	private void doFileSelectionModeChanged(Integer value) {
		setFilesSelectable(DIRECTORIES_ONLY != value);

		// Repaint the table to immediately enable/disable the rows
		table.repaint();
	}

	private void doDirectoryChanged(File dir) {
		listDirectory(dir, currentFilter);
	}

	private void doFileFilerChanged(FileFilter filter) {
		listDirectory(getCurrentDir(), filter);
	}

	private void doMultiSelectionEnabledChanged(Boolean multi) {
		table.setSelectionMode(multi ? MULTIPLE_INTERVAL_SELECTION : SINGLE_SELECTION);
	}

	/**
	 * Approve a selection (with double click or enter) or navigate 
	 * into another directory (when the File Selection Mode isn't DIRECTORIES_ONLY).
	 */
	private void maybeApproveSelection() {
		File selectedFile = getSelectedFile();
		if (selectedFile == null) {
			return;
		}

		if (fileSelectionMode != DIRECTORIES_ONLY && selectedFile.isDirectory()) {
			setCurrentDir(selectedFile);
		} else {
			fireActionEvent(new ActionEvent(FileBrowserPane.this, APPROVE_SELECTION.hashCode(), APPROVE_SELECTION));
		}
	}



}