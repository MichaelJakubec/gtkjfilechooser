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

import static eu.kostia.gtkjfilechooser.I18N._;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;

/**
 * Basic location in a file system: home dir, desktop and root.
 * 
 * @author c.cerbo
 *
 */
public class BasicPath implements Path {

	static public final BasicPath HOME = new BasicPath(System.getProperty("user.name"), System.getProperty("user.home"), "places/user-home");
	static public final BasicPath DESKTOP = new BasicPath(FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP).getName(), FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP).getAbsolutePath(), "places/user-desktop");

	static public final BasicPath ROOT = new BasicPath(_("File System"), "/", "gtk-harddisk"); 

	private static final long serialVersionUID = 1L;

	protected String location;
	protected String name;
	protected String iconName;

	public BasicPath() {
		super();
	}

	public BasicPath(String name, String location, String iconName) {
		this();
		this.name = name;
		this.location = location;
		this.iconName = iconName;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIconName() {
		return iconName;
	}

}
