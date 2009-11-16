/*******************************************************************************
 * Copyright 2009 Costantino Cerbo.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
package eu.kostia.gtkjfilechooser;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SortOrder;

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
		NAME, SIZE, MODIFIED
	};

	public enum Mode {
		PATH_BAR, FILENAME_ENTRY
	};

	private GKeyFile settings;

	private static GtkFileChooserSettings instance;

	static public GtkFileChooserSettings get() {
		if (instance == null) {
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
				.getString(LOCATION_MODE_KEY).toUpperCase().replace('-', '_'));
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

	/**
	 * Returns if we should shown the Location and File panels in the save mode
	 * ("Browse for other folders").
	 * 
	 * @return if expanding the "Browser for other folders" section (only save mode).
	 */
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

	public void setShowSizeColumn(boolean showSize) {
		settings.getGroup(SETTINGS_GROUP).setBoolean(SHOW_SIZE_COLUMN_KEY, showSize);
		save(SHOW_SIZE_COLUMN_KEY);
	}

	public Column getSortColumn() {
		String value = settings.getGroup(SETTINGS_GROUP).getString(SORT_COLUMN_KEY);
		if (value == null) {
			return null;
		}

		return Column.valueOf(value.toUpperCase());
	}

	public SortOrder getSortOrder() {
		String value = settings.getGroup(SETTINGS_GROUP).getString(SORT_ORDER_KEY);
		if (value == null) {
			return SortOrder.UNSORTED;
		}

		return SortOrder.valueOf(value.toUpperCase());
	}

	public void setSortBy(Column column, SortOrder order) {
		settings.getGroup(SETTINGS_GROUP).setString(SORT_COLUMN_KEY,
				column.toString().toLowerCase());
		settings.getGroup(SETTINGS_GROUP).setString(SORT_ORDER_KEY,
				order.toString().toLowerCase());
		save("sorting");
	}

}