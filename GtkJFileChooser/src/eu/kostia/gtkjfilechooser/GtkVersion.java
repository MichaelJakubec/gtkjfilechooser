/*
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
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 */
package eu.kostia.gtkjfilechooser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Version Information — Variables and functions to check the GTK+ version
 * 
 * @see {@link http
 *      ://library.gnome.org/devel/gtk/unstable/gtk-Feature-Test-Macros.html}
 * @see {@link http://git.gnome.org/cgit/gtk+/tree/gtk/gtkversion.h.in}
 * 
 * @author Costantino Cerbo
 * 
 */
public class GtkVersion {
	static public int GTK_MAJOR_VERSION;
	static public int GTK_MINOR_VERSION;
	static public int GTK_MICRO_VERSION;
	static {
		try {
			BufferedReader br = null;
			try {
				// pkg-config works on all UNIX-like operating systems
				String[] cmd = { "pkg-config", "--modversion", "gtk+-2.0" };
				Process process = new ProcessBuilder(cmd).start();
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String[] arg = br.readLine().split(Pattern.quote("."));

				GTK_MAJOR_VERSION = Integer.parseInt(arg[0]);
				GTK_MINOR_VERSION = Integer.parseInt(arg[1]);
				GTK_MICRO_VERSION = Integer.parseInt(arg[2]);
			} finally {
				if (br != null) {
					br.close();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			GTK_MAJOR_VERSION = -1;
			GTK_MINOR_VERSION = -1;
			GTK_MICRO_VERSION = -1;
		}
	}

	/**
	 * Checks that the GTK+ library in use is compatible (the same or newer)
	 * with the given version.
	 * 
	 * @param required_major
	 * @param required_minor
	 * @param required_micro
	 * @return {@code null} if the GTK+ library is compatible with the given
	 *         version, or a string describing the version mismatch. The
	 *         returned string is owned by GTK+ and should not be modified or
	 *         freed.
	 */
	static public Boolean check(int major, int minor, int micro) {
		if (GTK_MAJOR_VERSION == -1) {
			return null;
		}

		return (GTK_MAJOR_VERSION > (major)
				|| (GTK_MAJOR_VERSION == (major) && GTK_MINOR_VERSION > (minor)) || (GTK_MAJOR_VERSION == (major)
				&& GTK_MINOR_VERSION == (minor) && GTK_MICRO_VERSION >= (micro)));
	}
}
