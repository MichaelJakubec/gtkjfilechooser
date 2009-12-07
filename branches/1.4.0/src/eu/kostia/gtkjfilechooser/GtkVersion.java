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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * <p>
 * Version Information for GTK+
 * </p>
 * <p>
 * We get the GNOME version with the ubiquitous command
 * <tt>gnome-panel --version</tt> and then we retrieve the corresponding GTK+
 * version from an hard-coded map. Though not the most elegant solution, it has
 * the advantage to avoid cross-compiling issue like when you use JNI or
 * external dependencies like JNA (Java Native Access).
 * </p>
 * <p>
 * The shared object containing the version information is
 * <tt>/usr/lib/libgtk-x11-2.0.so</tt>.
 * </p>
 * <p>
 * Another solution were the interrogate the file
 * <tt>/usr/lib/pkgconfig/gtk+-2.0.pc</tt> but it isn't always present in a
 * linux distro.
 * </p>
 * 
 * 
 * 
 * @see {@link http://library.gnome.org/devel/gtk/unstable/gtk-Feature-Test-Macros.html}
 * @see {@link http://git.gnome.org/cgit/gtk+/tree/gtk/gtkversion.h.in}
 * 
 * @author Costantino Cerbo
 * 
 */
public class GtkVersion {
	static private final Map<String, GtkVersion> GNOME_GTK_MAP = new HashMap<String, GtkVersion>();
	static {
		GNOME_GTK_MAP.put("2.28", new GtkVersion(2, 18)); // Sep 2009
		GNOME_GTK_MAP.put("2.26", new GtkVersion(2, 16)); // Mar 2009
		GNOME_GTK_MAP.put("2.24", new GtkVersion(2, 14)); // Sep 2008
		GNOME_GTK_MAP.put("2.22", new GtkVersion(2, 12)); // Mar 2008
		GNOME_GTK_MAP.put("2.20", new GtkVersion(2, 12)); // Sep 2007
		GNOME_GTK_MAP.put("2.18", new GtkVersion(2, 12)); // Mar 2007
		GNOME_GTK_MAP.put("2.16", new GtkVersion(2, 10)); // Sep 2006
		GNOME_GTK_MAP.put("2.14", new GtkVersion(2, 10)); // Mar 2006
		GNOME_GTK_MAP.put("2.12", new GtkVersion(2, 6)); // Sep 2005
		GNOME_GTK_MAP.put("2.10", new GtkVersion(2, 6)); // Mar 2005
		GNOME_GTK_MAP.put("2.8", new GtkVersion(2, 4)); // Sep 2004
		GNOME_GTK_MAP.put("2.6", new GtkVersion(2, 4)); // Mar 2004
	}

	static private final GtkVersion UNKNOWN = new GtkVersion(-1, -1);

	static public GtkVersion current;

	private int major, minor;

	private GtkVersion(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	static public GtkVersion getCurrent() {
		try {
			return getCurrent0();
		} catch (Throwable th) {
			Log.log(Level.WARNING, th, "Cannot detect GTK+ version");
			return UNKNOWN;
		}
	}

	static private GtkVersion getCurrent0() throws IOException {
		if (current == null) {
			BufferedReader br = null;
			try {
				// gnome-panel is installed on all UNIX-like
				// operating systems with GNOME
				String[] cmd = { "gnome-panel", "--version" };
				Process process = new ProcessBuilder(cmd).start();
				br = new BufferedReader(new InputStreamReader(process
						.getInputStream()));
				String line = br.readLine();

				// GNOME major.minor version number (without micro)
				int start = line.lastIndexOf(' ') + 1;
				int end = line.lastIndexOf('.');
				String version = line.substring(start, end);

				GtkVersion result = GNOME_GTK_MAP.get(version);
				current = result != null ? result : UNKNOWN;
			} finally {
				if (br != null) {
					br.close();
				}
			}
		}

		return current;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
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
		if (getCurrent().getMajor() == -1) {
			return null;
		}

		return (getCurrent().getMajor() > (major)
				|| (getCurrent().getMajor() == (major) && getCurrent()
						.getMinor() > (minor)) || (getCurrent().getMajor() == (major) && getCurrent()
				.getMinor() == (minor)));
	}

	@Override
	public String toString() {
		return major + "." + minor;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(GtkVersion.getCurrent());
	}
}
