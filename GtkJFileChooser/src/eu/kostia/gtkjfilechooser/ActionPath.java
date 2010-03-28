/*******************************************************************************
 * Copyright 2010 Costantino Cerbo.  All Rights Reserved.
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

/**
 * To an {@link ActionPath} corresponds no location, but is like an button to
 * execute an action.
 * 
 * @author c.cerbo
 * 
 */
public class ActionPath implements Path {

	private static final long serialVersionUID = 1L;

	static final public int RECENTLY_USED_PANEL_ID = 1001;
	static final public int SEARCH_PANEL_ID = 1002;

	static public final ActionPath SEARCH = new ActionPath(_("Search"), SEARCH_PANEL_ID, "search", "actions/stock_search");
	static public final ActionPath RECENTLY_USED = new ActionPath(_("Recently Used"), RECENTLY_USED_PANEL_ID, "recently_used", "actions/document-open-recent");

	private String name;
	private int id;
	private String action;
	private String iconName;

	public ActionPath(String name, int id, String action, String iconName) {
		super();
		this.name = name;
		this.id = id;
		this.action = action;
		this.iconName = iconName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

}
