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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * <p>
 * Detect the file type basing on the magic pattern database.
 * </p>
 * The format of the magic patter file is described in {@link http://man.he.net/?topic=magic&section=all}. <br />
 * In the most linux system the magic file is in <tt>/usr/share/file/magic</tt>.
 * 
 * @author Costantino Cerbo
 * 
 */
//TODO Still implementing...
public class Magic {
	private File magicfile;

	public Magic(File magicfile) {
		this.magicfile = magicfile;
	}

	public Result detect(File file) throws IOException {
		return detect(new FileInputStream(file));
	}

	public Result detect(URL url) throws IOException {
		return detect(url.openConnection().getInputStream());
	}

	public Result detect(InputStream stream) throws IOException {
		Scanner sc0 = null;
		try {
			sc0 = new Scanner(magicfile);
			while (sc0.hasNextLine()) {
				String line = sc0.nextLine();
				if (line.startsWith("#") || line.trim().isEmpty()) {
					continue;
				}

				if (line.startsWith("!:")) {
					// other options (mime, strength, etc...)
				} else if (line.startsWith(">")) {
					// subsequent-level magic pattern
					int level = level(line);
					// println(level+"\t"+line);
				} else {
					// top-level magic pattern
					Scanner sc1 = new Scanner(line);

					try {
						String offset = sc1.next();
						String type = sc1.next();
						String test = sc1.next();

						// get the remaining part of the line
						sc1.useDelimiter("\\Z");
						String message = sc1.hasNext() ? sc1.next().trim() : null;

						printf("offset: '%s', type: '%s', test: '%s', message: '%s'\n",
								offset, type, test, message);
					} catch (Exception e) {
						throw new IllegalStateException(
								"Line that caused the exception:\n" + line, e);
					}

				}

			}

			return null;
		} finally {
			if (stream != null) {
				stream.close();
			}

			if (sc0 != null) {
				sc0.close();
			}
		}
	}

	private void println(String str) {
		System.out.println(str);
	}

	private void printf(String format, Object... args) {
		System.out.printf(format, args);
	}

	private int level(String line) {
		int level = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '>') {
				level++;
			} else {
				break;
			}
		}
		return level;
	}

	/**
	 * Inner bean containing the result of the detection: mime type and full
	 * description.
	 * 
	 */
	public class Result {
		private String mime;
		private String description;

		public String getMime() {
			return mime;
		}

		public String getDescription() {
			return description;
		}
	}
}
