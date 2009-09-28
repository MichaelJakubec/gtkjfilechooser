package eu.kostia.gtkjfilechooser;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GtkFileChooserSettings {
	static private final Logger LOG = Logger.getLogger(GtkFileChooserSettings.class
			.getName());

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

	public enum Column {
		NAME, MODIFIED, SIZE
	};

	public enum Order {
		ASCENDING, DESCENDING
	};

	public enum Mode {
		PATH_BAR, FILENAME_ENTRY
	};

	// static private final String COLUMN_NAME_STRING = "name";
	// static private final String COLUMN_MTIME_STRING = "modified";
	// static private final String COLUMN_SIZE_STRING = "size";
	//	
	// static private final String SORT_ASCENDING_STRING = "ascending";
	// static private final String SORT_DESCENDING_STRING = "descending";

	// static private final String MODE_PATH_BAR = "path-bar";
	// static private final String MODE_FILENAME_ENTRY = "filename-entry";

	private GKeyFile settings;

	private static GtkFileChooserSettings instance;
	
	static public GtkFileChooserSettings get(){
		if (instance == null){
			instance = new GtkFileChooserSettings();
		}
		
		return instance;
	}
	
	private GtkFileChooserSettings() {
		try {
			settings = new GKeyFile(new File(System.getProperty("user.home")
					+ File.separator + ".config/gtk-2.0/gtkfilechooser.ini"));
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Could not find settings.", e);
		}
	}

	public Rectangle getBound() {
		Integer x = settings.getGroup(SETTINGS_GROUP).getInteger(GEOMETRY_X_KEY);
		Integer y = settings.getGroup(SETTINGS_GROUP).getInteger(GEOMETRY_Y_KEY);
		Integer width = settings.getGroup(SETTINGS_GROUP).getInteger(GEOMETRY_WIDTH_KEY);
		Integer height = settings.getGroup(SETTINGS_GROUP)
				.getInteger(GEOMETRY_HEIGHT_KEY);

		if (x == null || y == null || width == null || height == null) {
			return null;
		}

		return new Rectangle(x, y, width, height);
	}

	public void setBound(Rectangle bound) {
		settings.getGroup(SETTINGS_GROUP).setInteger(GEOMETRY_X_KEY, bound.x);
		settings.getGroup(SETTINGS_GROUP).setInteger(GEOMETRY_Y_KEY, bound.y);
		settings.getGroup(SETTINGS_GROUP).setInteger(GEOMETRY_WIDTH_KEY, bound.width);
		settings.getGroup(SETTINGS_GROUP).setInteger(GEOMETRY_HEIGHT_KEY, bound.height);

		save("boundaries");
	}

	private void save(final String propertyname) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					settings.save();
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Could not persist '" + propertyname + "' in "
							+ settings.getGkeyfile() + ".", e);
				}
			}

		}).start();
	}

	public Mode getLocationMode() {
		return Mode.valueOf(settings.getGroup(SETTINGS_GROUP)
				.getString(LOCATION_MODE_KEY).toUpperCase());
	}

	public void setLocationMode(Mode mode) {
		settings.getGroup(SETTINGS_GROUP).setString(LOCATION_MODE_KEY,
				mode.toString().toLowerCase().replace('_', '-'));
		save(LOCATION_MODE_KEY);
	}

	public Boolean getShowHidden() {
		return settings.getGroup(SETTINGS_GROUP).getBoolean(SHOW_HIDDEN_KEY);
	}

	public void setShowHidden(boolean showHidden) {
		settings.getGroup(SETTINGS_GROUP).setBoolean(SHOW_HIDDEN_KEY, showHidden);
		save(SHOW_HIDDEN_KEY);
	}

	public Boolean getExpandFolders() {
		return settings.getGroup(SETTINGS_GROUP).getBoolean(EXPAND_FOLDERS_KEY);
	}

	public void setExpandFolders(boolean expandFolders) {
		settings.getGroup(SETTINGS_GROUP).setBoolean(EXPAND_FOLDERS_KEY, expandFolders);
		save(EXPAND_FOLDERS_KEY);
	}

	public Boolean getShowSizeColumn() {
		return settings.getGroup(SETTINGS_GROUP).getBoolean(SHOW_SIZE_COLUMN_KEY);
	}

	public void setShowSizeColumn(boolean expandFolders) {
		settings.getGroup(SETTINGS_GROUP).setBoolean(SHOW_SIZE_COLUMN_KEY, expandFolders);
		save(SHOW_SIZE_COLUMN_KEY);
	}

	public String getSortColumn() {
		return settings.getGroup(SETTINGS_GROUP).getString(SORT_COLUMN_KEY);
	}

	public String getSortSortOrder() {
		return settings.getGroup(SETTINGS_GROUP).getString(SORT_ORDER_KEY);
	}

	public void setSortBy(Column column, Order order) {
		settings.getGroup(SETTINGS_GROUP).setString(SORT_COLUMN_KEY,
				column.toString().toLowerCase());
		settings.getGroup(SETTINGS_GROUP).setString(SORT_ORDER_KEY,
				order.toString().toLowerCase());
		save("sorting");
	}

}
