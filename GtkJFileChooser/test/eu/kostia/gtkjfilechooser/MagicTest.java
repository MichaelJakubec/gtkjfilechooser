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

import static org.junit.Assert.*;

import java.io.File;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

import eu.kostia.gtkjfilechooser.Magic.Result;

/**
 * @author Costantino Cerbo
 * 
 */
public class MagicTest {

	@Test
	public void testMagicJavaClass() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/Wildcard.class"));
		System.out.println(result);

		assertNotNull(result);
		assertEquals("compiled Java class data, version 50.0 (Java 1.6)", result.getDescription());
		assertEquals("application/x-java-applet", result.getMime());
	}

	@Test
	public void testMagicMp3() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/Jaws.mp3"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("MPEG ADTS, layer III, v1, 128 kbps, 44.1 kHz, Stereo", result.getDescription());
		assertEquals("audio/mpeg", result.getMime());
	}

	@Test
	public void testMagicOdt() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/softwarelist.odt"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("OpenDocument Text", result.getDescription());
		assertEquals("application/vnd.oasis.opendocument.text", result.getMime());
	}

	/**
	 * See rule on row 3454 in /usr/share/magic
	 */
	@Test
	public void testMagicGZip() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/sca.gz"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("gzip compressed data, was \"sca.pdf\", from Unix, last modified: Sat Nov 07 00:02:45 CET 2009", result.getDescription());
		assertEquals("application/x-gzip", result.getMime());
	}

	@Test
	public void testMagicWord() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/Senior.doc"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("CDF V2 Document, Little Endian, Os: Windows, Version 6.0, Code page: 1252, Title: Senior Finance Manager - GE Oil & Gas, Author: Euan Slater, Template: Normal.dotm, Last Saved By: Shola, Revision Number: 2, Name of Creating Application: Microsoft Office Word, Total Editing Time: 01:00, Create Time/Date: Mon Mar 23 15:49:00 2009, Last Saved Time/Date: Mon Mar 23 15:49:00 2009, Number of Pages: 3, Number of Words: 793, Number of Characters: 4525, Security: 0", result.getDescription());
		assertEquals("application/x-java-applet", result.getMime());
	}

	@Test
	public void testMagicExcel() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/testEXCEL.xls"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("CDF V2 Document, Little Endian, Os: Windows, Version 5.1, Code page: 1252, Title: Simple Excel document, Author: Keith Bennett, Last Saved By: RIBEN9, Name of Creating Application: Microsoft Excel, Create Time/Date: Sun Sep 30 17:13:56 2007, Last Saved Time/Date: Sun Sep 30 17:31:43 2007, Security: 0", result.getDescription());
		assertEquals("application/x-java-applet", result.getMime());
	}

	/**
	 * See rule on row 10212 in /usr/share/magic
	 */
	@Test
	public void testWinExe() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/control.exe"));
		System.out.println(result);

		assertNotNull("Result is null", result);
		assertEquals("PE32 executable for MS Windows (GUI) Intel 80386 32-bit", result.getDescription());
		assertEquals("application/octet-stream", result.getMime());
	}

	// Numeric values may be preceded by a character indicating the opera-
	// tion to be performed. It may be =, to specify that the value from
	// the file must equal the specified value, <, to specify that the
	// value from the file must be less than the specified value, >, to
	// specify that the value from the file must be greater than the spec-
	// ified value, &, to specify that the value from the file must have
	// set all of the bits that are set in the specified value, ^, to
	// specify that the value from the file must have clear any of the
	// bits that are set in the specified value, or ~, the value specified
	// after is negated before tested. x, to specify that any value will
	// match. If the character is omitted, it is assumed to be =. Opera-
	// tors &, ^, and ~ don’t work with floats and doubles. The operator
	// ! specifies that the line matches if the test does not succeed.
	@Test
	public void operators() throws Exception {
		char[] expecteds = new char[] { '!', '&', '<', '=', '>', '^', '~' };
		char[] operators = new char[] { '!', '&', '<', '=', '>', '^', '~' };
		Arrays.sort(operators);
		System.out.println(Arrays.toString(operators));
		assertArrayEquals(expecteds, operators);
	}

	@Test
	public void testCafeBabe() throws Exception {
		byte[] java = new byte[] { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe };
		long l = ByteUtil.toInt(ByteOrder.BIG_ENDIAN, java);
		System.out.println(ByteUtil.toHexString(java) + ": " + l);

		int signed32bit0 = 0xcafebabe;
		byte[] b0 = ByteUtil.toBytes(signed32bit0, ByteOrder.BIG_ENDIAN);
		System.out.println("int: " +signed32bit0 + " ; hex: " + ByteUtil.toHexString(b0));
		assertEquals("CA FE BA BE", ByteUtil.toHexString(b0));


		int signed32bit1 = -889275714;
		long unsigned = signed32bit1 & 0xffffffffL; 
		int ununsigned = (int) unsigned; 
		System.out.println("signed32bit :" + signed32bit1 +" ; unsigned32bit: " + unsigned);
		System.out.println("ununsigned: " + ununsigned);
		//3405691582

		long l0 = Long.parseLong("cafebabe", 16);
		System.out.println("Long.parseLong(\"cafebabe\", 16): " + l0);

		short signed16bit0 = (short) 0xcafe;
		byte[] i0 = ByteUtil.toBytes(signed16bit0, ByteOrder.BIG_ENDIAN);
		System.out.println("int: " +signed16bit0 + " ; hex: " + ByteUtil.toHexString(i0));
		System.out.println("Integer.parseInt(\"cafe\", 16): " + Integer.parseInt("cafe", 16));
		assertEquals("CA FE", ByteUtil.toHexString(i0));

	}

	@Test
	public void testZipString() throws Exception {		
		String s = "PK\003\004";
		System.out.println("Zip String 1: " + s);

		byte[] b = new byte[] {0x50, 0x4b, 0x03, 0x04};
		System.out.println("Zip String 2: " + new String(b, Charset.forName("UTF-8")));

		String s0 = "PK\\003\\004";		
		//		System.out.println("s0 = " + convertStringProps(s0));

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		String x = (String) engine.eval("'"+s0+"'");
		System.out.println("x = " + x);

	}


	@Test
	public void testToInt0() throws Exception {		
		Magic magic = new Magic(new File("misc/magic/magic")) {
			@Override
			byte[] readByte(int offset, int len) throws java.io.IOException {
				byte[] bb = new byte[len];
				for (int i = 0; i < bb.length; i++) {
					bb[i] = 10;					
				}

				return bb;
			};
		};
		magic.toInt("(0x3c.l)");
	}

	@Test
	public void testToInt1() throws Exception {		
		Magic magic = new Magic(new File("misc/magic/magic")) {
			@Override
			byte[] readByte(int offset, int len) throws java.io.IOException {
				byte[] bb = new byte[len];
				for (int i = 0; i < bb.length; i++) {
					bb[i] = 10;					
				}

				return bb;
			};
		};
		magic.toInt("(4.s*512)");

		String s = "0111";ByteUtil.toBinaryString(7);
		char[] c = new char[s.length()];
		for (int i = 0; i < c.length; i++) {
			c[i] = s.charAt(i) == '0' ? '1' : '0';

		}
		System.out.println(Long.parseLong(String.valueOf(c), 2));
	}


}
