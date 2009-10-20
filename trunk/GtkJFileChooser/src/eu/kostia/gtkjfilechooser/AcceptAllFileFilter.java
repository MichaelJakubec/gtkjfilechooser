package eu.kostia.gtkjfilechooser;

import java.io.File;

import javax.swing.UIManager;

public class AcceptAllFileFilter extends javax.swing.filechooser.FileFilter{

	@Override
	public boolean accept(File file) {
		return true;
	}

	@Override
	public String getDescription() {
		return UIManager.getString("FileChooser.acceptAllFileFilterText");
	}

	@Override
	public String toString() {
		return getDescription();
	}

}
