/*******************************************************************************
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
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
package eu.kostia.gtkjfilechooser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GettextResourceTest {
	GettextResource r0;
	GettextResource r1;
	public GettextResourceTest() throws IOException {
		r0 = new GettextResource(new File("misc/mo_file/gtk+.gtk-2-10.it.mo"));
		r1 = new GettextResource(new File("misc/mo_file/gtk+.gtk-2-10.it.mo"));
	}

	@BeforeClass
	static public void beforeClass() {
		Locale.setDefault(Locale.ITALIAN);
	}

	@AfterClass
	static public void afterClass() {
		String lang = System.getenv("LANG");
		if (lang == null) {
			return;
		}

		if (lang.lastIndexOf('.') > 0){
			lang = lang.substring(0, lang.lastIndexOf('.'));
		}		

		if (lang.lastIndexOf('_') > 0){
			String language = lang.substring(0, lang.lastIndexOf('_'));
			String country = lang.substring(lang.lastIndexOf('_') + 1);
			Locale.setDefault(new Locale(language, country));
		}
	}

	@Test
	public void testMoReaderGetKeys() throws Exception {
		GettextResource r = new GettextResource(new File("misc/mo_file/default.mo"));

		Enumeration<String> en = r.getKeys();
		while (en.hasMoreElements()) {
			String msgid = en.nextElement();
			String msgstr = r._(msgid);
			System.out.println("msgid \"" + msgid + "\"");
			System.out.println("msgstr \"" + msgstr + "\"");
			System.out.println();
		}
	}


	@Test
	public void testMoReaderDefault() throws Exception {
		GettextResource r = new GettextResource(new File("misc/mo_file/default.mo"));

		assertEquals("Yes, the look-up worked.  Congratulations!!", r._("hello"));
		assertEquals("This is just here to provide\nsome other strings so that the lookup\nis not totally trivial.", r._("misc_1"));
		assertEquals("Another misc.", r._("misc_2"));
		assertEquals("Bye!", r._("misc_3"));
	}

	@Test
	public void testMoReaderGtk20() throws Exception {
		GettextResource r = new GettextResource("gtk20");
		assertEquals("Impossibile recuperare informazioni sul file", r._("Could not retrieve information about the file"));
		assertEquals("Usati di recente", r._("Recently Used"));
	}


	@Test
	public void testMoReaderGtk0() throws Exception {		
		assertEquals("Nome del file non valido: %s", r1._("Invalid filename: %s"));
		assertEquals("Invio dati", r1._("print operation status|Sending data"));
	}

	/**
	 * The the performance gain with the cache. The excution of this test method should be shorter than {@link #testMoReaderGtk0()}
	 * @throws Exception
	 */
	@Test
	public void testMoReaderGtk1() throws Exception {
		assertEquals("Nome del file non valido: %s", r1._("Invalid filename: %s"));
		assertEquals("Invio dati", r1._("print operation status|Sending data"));
	}

	@Test
	public void testMoReaderLocale() throws Exception {
		GettextResource r = new GettextResource("nautilus");
		assertEquals("ieri", r._("yesterday"));
	}



	@Test
	public void testMoReaderGtk20Properties() throws Exception {
		GettextResource r = new GettextResource("gtk20-properties");
		assertEquals("La direzione verso cui punta la freccia", r._("The direction the arrow should point"));
	}

	@Test
	public void testSwap() throws Exception {
		int i = swap(881);
		System.out.println(i);
		assertEquals(1896022016, i);
	}

	@Test
	public void testSwapb() throws Exception {
		int i = swapb(0x00, 0x00, 0x00, 0x89);
		System.out.println("swapb: " + i);
	}

	/**
	 * Byte swap a single int value.
	 * 
	 * @param value
	 *            Value to byte swap.
	 * @return Byte swapped representation.
	 */
	int swap(int value) {
		int b1 = (value >> 0) & 0xff;
		int b2 = (value >> 8) & 0xff;
		int b3 = (value >> 16) & 0xff;
		int b4 = (value >> 24) & 0xff;

		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	}

	int swapb(int b1, int b2, int b3, int b4) {
		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	}

	@Test
	public void byteArrayToInt0() {
		// 71 03 00 00 --> 881
		// 05 00 00 00 --> 05
		int v = 136;

		byte[] b = toByteArray(v);
		for (byte c : b) {
			System.out.print(Integer.toHexString(c & 0xff) + " ");
		}
		System.out.println();
	}

	@Test
	public void byteArrayToInt1() {
		// 71 03 00 00 --> 881
		// 05 00 00 00 --> 05
		int v = 28;

		byte[] b = toByteArray(v);
		for (byte c : b) {
			System.out.print(Integer.toHexString(c & 0xff) + " ");
		}
		System.out.println();
	}

	private byte[] toByteArray(int v) {
		byte b[] = new byte[4];
		int i, shift;

		for(i = 0, shift = 24; i < 4; i++, shift -= 8)
			b[i] = (byte)(0xFF & (v >> shift));
		return b;
	}


}
