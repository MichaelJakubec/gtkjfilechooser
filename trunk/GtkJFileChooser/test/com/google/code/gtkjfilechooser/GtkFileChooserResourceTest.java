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
package com.google.code.gtkjfilechooser;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.security.AccessController;
import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.gtkjfilechooser.GettextResource;

import sun.security.action.GetPropertyAction;

public class GtkFileChooserResourceTest {
	

	public void resetLocale() {
		String language = AccessController.doPrivileged(new GetPropertyAction("user.language"));
		String country = AccessController.doPrivileged(new GetPropertyAction("user.country"));

		Locale.setDefault(new Locale(language, country));
	}

	@Test
	public void testMoReaderGtk20() throws Exception {		
		GettextResource r = new GettextResource(Locale.ITALIAN, "gtk20");
		
		assertEquals("_Salva", r._("Stock label|_Save"));		
		assertEquals("_Apri", r._("Stock label|_Open"));	
		
		assertEquals("Usati di recente", r._("Recently Used"));
		assertEquals("Ricerca", r._("Search"));
		assertEquals("Il file esiste già in «%s». Scegliendo di sostituirlo il suo contenuto verrà sovrascritto.", r._("The file already exists in \"%s\".  Replacing it will overwrite its contents."));
		assertEquals("Ieri alle %k.%M", r._("Yesterday at %H:%M"));
		//assertEquals("Mostra _file nascosti", r._("Show _Hidden Files"));
		assertEquals("A_ggiungi", r._("_Add"));
		assertEquals("_Rimuovi", r._("_Remove"));
		assertEquals("Ris_orse", r._("_Places"));
		assertEquals("Salva nella _cartella:", r._("Save in _folder:"));
		assertEquals("_Esplora altre cartelle", r._("_Browse for other folders"));
		assertEquals("Scrivania", r._("Desktop"));
		assertEquals("File system", r._("File System"));
		assertEquals("_OK", r._("Stock label|_OK"));
		assertEquals("A_nnulla", r._("Stock label|_Cancel"));
		

		//Missing entries
		r.markMissingTranslation(true);
		assertEquals("**Missing**", r._("Stock label|Missing"));	
		assertEquals("*Missing again*", r._("Missing again"));
		r.markMissingTranslation(false);
		assertEquals("Missing", r._("Stock label|Missing"));	
		assertEquals("Missing again", r._("Missing again"));
		
		resetLocale();
	}
	
	@Test
	@Ignore
	public void testMoReaderGtk20StockLabel0() throws Exception {
		Locale.setDefault(Locale.ITALIAN);
				
		GettextResource r = new GettextResource(Locale.ITALIAN, "gtk20");
		r.markMissingTranslation(true);
		
		assertEquals("_Salva", r._("Stock label|_Save"));
		assertEquals("_Apri", r._("Stock label|_Open"));	
		
		resetLocale();
	}
	
	@Test
	@Ignore
	public void testMoReaderGtk20StockLabel1() throws Exception {
		Locale.setDefault(Locale.ITALIAN);
		
		GettextResource r = new GettextResource(Locale.ITALIAN, "gtk20");
		
		assertEquals("_Salva", r._("_Save"));
		assertEquals("_Apri", r._("_Open"));	
		
		resetLocale();
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
