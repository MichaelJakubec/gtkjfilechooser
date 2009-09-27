package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.io.IOException;

public class GtkFileChooserSettings {

	static private final String SETTINGS_GROUP = "Filechooser Settings";
	static private final String LOCATION_MODE_KEY = "LocationMode";
	static private final String SHOW_HIDDEN_KEY = "ShowHidden";
	static private final String EXPAND_FOLDERS_KEY = "ExpandFolders";
	static private final String SHOW_SIZE_COLUMN_KEY = "ShowSizeColumn";
	static private final String GEOMETRY_X_KEY = "GeometryX";
	static private final String GEOMETRY_Y_KEY = "GeometryY";
	static private final String GEOMETRY_WIDTH_KEY = "GeometryWidth";
	static private final String GEOMETRY_HEIGHT_KEY = "GeometryHeight";
	static private final String SORT_COLUMN_KEY = "SortColumn";
	static private final String SORT_ORDER_KEY = "SortOrder";

	static private final String COLUMN_NAME_STRING = "name";
	static private final String COLUMN_MTIME_STRING = "modified";
	static private final String COLUMN_SIZE_STRING = "size";
	static private final String SORT_ASCENDING_STRING = "ascending";
	static private final String SORT_DESCENDING_STRING = "descending";

	static private final String MODE_PATH_BAR = "path-bar";
	static private final String MODE_FILENAME_ENTRY = "filename-entry";

	private GKeyFile gKeyFile;

	public GtkFileChooserSettings() throws IOException {
		gKeyFile = new GKeyFile(new File(System.getProperty("user.home") + File.separator + ".config/gtk-2.0/gtkfilechooser.ini"));
	}



}
