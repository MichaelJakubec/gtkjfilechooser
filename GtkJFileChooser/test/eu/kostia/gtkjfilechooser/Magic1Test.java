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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

import eu.kostia.gtkjfilechooser.Magic.Result;

/**
 * @author Costantino Cerbo
 * 
 */
public class Magic1Test {
	public static void main(String[] args) throws IOException {
		File testFilesFolder = new File("misc/magic/testfiles/openbds");
		File[] testFiles = testFilesFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".in");
			}
		});

		Magic magic = new Magic(new File("misc/magic/magic"));
		for (File fileIn : testFiles) {
			Result result = magic.detect(fileIn);

			File fileOut = new File(fileIn.getAbsolutePath().replace(".in", ".out"));
			String expected = new Scanner(fileOut).useDelimiter("\\Z").next();

			System.out.println("Actual:   " + result);			
			System.out.println("Expected: " + expected);
			System.out.println();
		}
	}

}
