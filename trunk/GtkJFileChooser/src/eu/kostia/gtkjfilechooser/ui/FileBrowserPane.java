package eu.kostia.gtkjfilechooser.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import javax.swing.JFileChooser;

import eu.kostia.gtkjfilechooser.Log;

public class FileBrowserPane extends FilesListPane {

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

	public FileBrowserPane(File startDir) {
		this.currentDir = startDir;

		File[] files = currentDir.listFiles(acceptAll);
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

						getModel().clear();
						File[] files = selectedFile.listFiles();
						if (files != null) {
							for (File file : files) {
								getModel().addFile(file);
							}
						}
					}
				}

			}
		});
	}

	public File getCurrentDir() {
		return currentDir;
	}

}