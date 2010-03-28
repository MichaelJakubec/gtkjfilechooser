/*******************************************************************************
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
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
package eu.kostia.gtkjfilechooser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class UrlUtil {

	static public String decode(String str) {
		try {
			return URLDecoder.decode(str, Charset.defaultCharset().toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	static public String encode(String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int dec = ch;

			// ASCII Control characters
			if (dec >= 0 && dec <= 31) {
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Non-ASCII characters
			if (dec >= 127) {
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Reserved and unsafe characters
			switch (dec) {
			case '$':
			case '&':
			case '+':
			case ',':
			case ':':
			case ';':
			case '=':
			case '?':
			case '@':
			case ' ':
			case 34: // Quotation marks
			case '<':
			case '>':
			case '#':
			case '%':
			case '{':
			case '}':
			case '|':
			case 92: // Backslash
			case '^':
			case '~':
			case '[':
			case ']':
			case '`':
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Append the char as it is
			sb.append(ch);
		}

		return sb.toString();
	}
}
