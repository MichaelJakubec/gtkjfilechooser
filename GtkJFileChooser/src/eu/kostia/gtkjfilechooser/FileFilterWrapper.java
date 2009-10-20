package eu.kostia.gtkjfilechooser;

import java.io.File;
/**
 * Wrap a {@link javax.swing.filechooser.FileFilter} into a {@link java.io.FileFilter}.
 * 
 * @author Costantino Cerbo
 *
 */
public class FileFilterWrapper implements java.io.FileFilter {

	private javax.swing.filechooser.FileFilter filter;

	public FileFilterWrapper(javax.swing.filechooser.FileFilter filter) {
		this.filter = (filter != null) ? filter : new AcceptAllFileFilter();		
	}

	@Override
	public boolean accept(File pathname) {
		return filter.accept(pathname);
	}

	@Override
	public String toString() {
		return filter.getDescription();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

}
