/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
/*
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
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 */
package eu.kostia.gtkjfilechooser;

import static eu.kostia.gtkjfilechooser.GtkStockIcon.md5;
import static eu.kostia.gtkjfilechooser.GtkStockIcon.toFileuri;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

/**
 * @author Costantino Cerbo
 *
 */
public class GtkStockIconTest {

	@Test
	public void testMd5sum0() throws Exception {
		File file = new File("/home/c.cerbo/Documenti/Ciao Belli.pdf");

		String fileuri = toFileuri(file);
		assertEquals("file:///home/c.cerbo/Documenti/Ciao%20Belli.pdf", fileuri);

		String md5 = md5(fileuri);
		assertEquals("745f2ee69382d3cb445425f1183bfa39", md5);
	}

	@Test
	public void testMd5sum1() {
		File file = new File("/home/c.cerbo/Documenti/Effective_Java.pdf");

		String fileuri = toFileuri(file);
		System.out.println(fileuri);

		String md5 = md5(fileuri);
		assertEquals("6e1c7944eac7bc932b2b56c6ac94960f", md5);
	}

}
