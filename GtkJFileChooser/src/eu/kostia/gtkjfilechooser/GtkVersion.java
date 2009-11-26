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


/**
 * Version Information — Variables and functions to check the GTK+ version
 * 
 * @see {@link http://library.gnome.org/devel/gtk/unstable/gtk-Feature-Test-Macros.html}
 * @see {@link http://git.gnome.org/cgit/gtk+/tree/gtk/gtkversion.h.in}
 * @author Costantino Cerbo
 *
 */
public class GtkVersion {
	
	static {
		System.out.println(System.getProperty("java.library.path"));
	    System.loadLibrary("GtkVersion");
	   }

	
	/**
	 * Checks that the GTK+ library in use is compatible with the given version.
	 * 
	 * @param required_major
	 * @param required_minor
	 * @param required_micro
	 * @return {@code null} if the GTK+ library is compatible with the given
	 *         version, or a string describing the version mismatch. The
	 *         returned string is owned by GTK+ and should not be modified or
	 *         freed.
	 */
	static native public String check(int required_major, int required_minor, int required_micro);

	public static void main(String[] args) {		
		System.out.println("Version: " + check(2, 18, 0));
	}


}
