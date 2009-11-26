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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for the Java Native Interface (JNI) API.
 * 
 * @author Costantino Cerbo
 * 
 */
public class JNIUtil {

	/**
	 * <p>
	 * Usually the native libraries are passed to the VM with the property
	 * {@code java.library.path}. This is not pratical when we want to provide a
	 * single jar, and we cannot pass an URL to {@link System#load(String)}.
	 * Therefore we adopt the strategy to copy the native library contained in
	 * the jar (or simply classpath) in the system temporary folder and than we
	 * pass this filename to {@link System#load(String)}.
	 * </p>
	 * <p>
	 * As for the standard JDK method, this one should be invoked at
	 * classloading:
	 * 
	 * <pre>
	 * static {
	 * 	try {
	 * 		loadLibrary(&quot;my.package&quot;, &quot;HelloWorld&quot;);
	 * 	} catch (IOException e) {
	 * 		// handle this exception
	 * 	}
	 * }
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param pkg
	 *            The package where the shared library (for example .so or .ddl)
	 *            is.
	 * @param libname
	 *            The system indipendent name (for example HelloWorld for
	 *            libHelloWorld.so under linux or HelloWorld.dll under windows).
	 * @throws IOException
	 */
	static public void loadLibrary(String pkg, String libname) throws IOException {
		String syslibname = System.mapLibraryName(libname);

		String tmpdir = System.getProperty("java.io.tmpdir");
		File file = new File(tmpdir + File.separator + syslibname);
		if (!file.exists()) {
			String resourcename = "/" + pkg.replace('.', '/') + "/" + syslibname;
			writeToFile(GtkVersion.class.getResourceAsStream(resourcename), file);
		}

		System.load(file.getAbsolutePath());
	}

	static private void writeToFile(InputStream stream, File file) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			byte buffer[] = new byte[4096];
			int len = 0;
			while ((len = stream.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
		} finally {
			if (os != null) {
				os.close();
			}
			stream.close();
		}
	}
}
