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

import java.io.File;
import java.util.Map.Entry;

/**
 * @author Costantino Cerbo
 * 
 */
public class Platform {

	static public boolean isRedhat() {
		if (new File("/etc/fedora-release").exists()) {
			return true;
		}
		
		if (new File("/etc/redhat-release").exists()) {
			return true;
		}
		
		if (System.getProperty("os.version").indexOf(".fc") != -1) {
			return true;
		}
		
		
		if (System.getProperty("os.version").indexOf(".el") != -1) {
			return true;
		}
		
		return false;
	}

	static public boolean isSuSE() {
		//TODO implement
		return false;
	}

	static public boolean isUbuntu() {
		//TODO implement try with "cat /etc/issue" or "cat /etc/lsb-release"
		return false;
	}

	static public boolean isSolaris() {
		if (System.getProperty("os.name").indexOf("SunOS") != -1) {
			return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		for (Entry<Object, Object> prop : System.getProperties().entrySet()) {
			System.out.println(prop);
		}
	}
}
