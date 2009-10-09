package eu.kostia.gtkjfilechooser.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * Autocompleter decorator for path locations
 * 
 * @author c.cerbo
 * 
 */
public class PathAutoCompleter extends Autocompleter {

	private String currentPath;
	private boolean showHidden = false;

	public PathAutoCompleter(JTextComponent comp) {
		super(comp);
		setCurrentPath(System.getProperty("user.dir"));
	}

	public String getCurrentPath() {
		return currentPath;
	}
	
	/**
	 * Show hidden files?
	 * @param showHidden
	 */
	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	@Override
	protected List<String> updateSuggestions(String value) {
		File file = isAbsolute(value) ? new File(value) : new File(getCurrentPath()
				+ File.separator + value);

		final String prefix = file.getName();
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!showHidden && pathname.isHidden()) {
					return false;
				}
				
				String name = pathname.getName();				
				return name != null ? name.startsWith(prefix) : false;
			}
		};

		File[] list = null;
		if (file.isDirectory()) {
			list = file.listFiles(filter);
		} else if (file.getParentFile() != null) {
			list = file.getParentFile().listFiles(filter);
		}

		if (list == null || list.length == 0) {
			return null;
		}

		List<String> results = new ArrayList<String>();

		int index = value.lastIndexOf(File.separatorChar);
		String before = (index != -1) ? value.substring(0, index + 1) : "";
		for (File f : list) {
			String name = f.getName();
			if (f.isDirectory()) {
				// add slash if directory
				name = name + File.separator;
			}
			results.add(before + name);
		}

		return results;
	}

	private boolean isAbsolute(String value) {
		return new File(value).isAbsolute();
	}
}
