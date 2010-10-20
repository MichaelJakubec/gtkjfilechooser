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
package com.google.code.gtkjfilechooser.ui;

import static junit.framework.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class MD5Test {

	@Test
	public void testMd5() throws Exception {
		assertEquals("3e25960a79dbc69b674cd4ec67a72c62", md5("Hello world"));

		String uri = "file:///home/c.cerbo/temp/SchermataApriFile.png";
		System.out.println(md5(uri));
	}

	private String md5(String uri) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(uri.getBytes());
			return toHex(result);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private String toHex(byte[] b) {
		StringBuilder sb = new StringBuilder(64);
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}
