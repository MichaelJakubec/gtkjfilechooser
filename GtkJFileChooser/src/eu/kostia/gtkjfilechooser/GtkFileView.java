package eu.kostia.gtkjfilechooser;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileView;

import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class GtkFileView extends FileView {

	@Override
	public String getDescription(File f) {
		return null;
	}

	@Override
	public Icon getIcon(File f) {
		return GtkStockIcon.get(f, Size.GTK_ICON_SIZE_MENU);
	}

	@Override
	public String getName(File f) {
		return f.getName();
	}

	@Override
	public String getTypeDescription(File f) {
		return null;
	}

	@Override
	public Boolean isTraversable(File f) {
		return f.isDirectory();
	}

}
