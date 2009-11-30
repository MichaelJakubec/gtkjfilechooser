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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Costantino Cerbo
 * 
 */
public class GtkVersion0 {
	private int major, minor, micro;

	private GtkVersion0(int major, int minor, int micro) {
		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	static public GtkVersion0 getCurrent() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("gnome-panel", "--version");
		InputStream is = pb.start().getInputStream();
		
		String output = toString(is);
		
		return new GtkVersion0(0,0,0);
	}

	private static String toString(InputStream is) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = is.read(b)) != -1;) {
			out.append(new String(b, 0, n, Charset.defaultCharset()));
		}
		return out.toString();
	}

	public static void main(String[] args) throws IOException {
		GtkVersion0.getCurrent();
	}
}
