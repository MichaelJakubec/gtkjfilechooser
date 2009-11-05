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

import java.nio.charset.Charset;
import java.security.AccessController;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.security.action.GetPropertyAction;

public class GtkFileChooserResourceTest {
	GettextResource r = new GettextResource("gtk20");

	@Before
	public void beforeClass() {
		Locale.setDefault(Locale.ITALIAN);
	}

	@After
	public void resetLocale() {
		String language = AccessController.doPrivileged(new GetPropertyAction("user.language"));
		String country = AccessController.doPrivileged(new GetPropertyAction("user.country"));

		Locale.setDefault(new Locale(language, country));
	}

	@Test
	public void testMoReaderGtk20() throws Exception {
		assertEquals("Usati di recente", r._("Recently Used"));
		assertEquals("Ricerca", r._("Search"));
		assertEquals("Il file esiste già in «%s». Scegliendo di sostituirlo il suo contenuto verrà sovrascritto.", r._("The file already exists in \"%s\".  Replacing it will overwrite its contents."));
		assertEquals("Ieri alle %k.%M", r._("Yesterday at %H:%M"));
		assertEquals("Mostra _file nascosti", r._("Show _Hidden Files"));
		assertEquals("A_ggiungi", r._("_Add"));
		assertEquals("_Rimuovi", r._("_Remove"));
		assertEquals("Ris_orse", r._("_Places"));
		assertEquals("Salva nella _cartella:", r._("Save in _folder:"));
		assertEquals("_Esplora altre cartelle", r._("_Browse for other folders"));
		assertEquals("Scrivania", r._("Desktop"));
		assertEquals("File system", r._("File System"));
		assertEquals("_OK", r._("Stock label|_OK"));
		assertEquals("A_nnulla", r._("Stock label|_Cancel"));
		assertEquals("_Salva", r._("Stock label|_Save"));		
		assertEquals("_Apri", r._("Stock label|_Open"));	

		//Missing entries
		r.markMissingTranslation(true);
		assertEquals("*Missing*", r._("Stock label|Missing"));	
		assertEquals("*Missing again*", r._("Missing again"));
		r.markMissingTranslation(false);
		assertEquals("Missing", r._("Stock label|Missing"));	
		assertEquals("Missing again", r._("Missing again"));
	}

	@Test
	public void testCharset() throws Exception {
		Charset isoLatin = Charset.forName("ISO-8859-1");
		Charset utf8 = Charset.forName("UTF-8");

		byte a = "|".getBytes("ISO-8859-1")[0];
		byte b = "|".getBytes("UTF-8")[0];
		System.out.println(Integer.toHexString(a));
		System.out.println(Integer.toHexString(b));

		System.out.println(new String(new byte[]{0x04}, isoLatin));
		System.out.println(new String(new byte[]{0x7c}, isoLatin));
		System.out.println(new String(new byte[]{0x04}, utf8));
		System.out.println(new String(new byte[]{0x7c}, utf8));
	}

	public static void main(String[] args) {
		GettextResource.main("-i", "/usr/share/locale/it/LC_MESSAGES/gtk20.mo");
		//GettextResource.main("-k", "Yesterday at %H:%M", "/usr/share/locale/ps/LC_MESSAGES/gtk20.mo");
	}
}
