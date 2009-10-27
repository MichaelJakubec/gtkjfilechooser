package eu.kostia.gtkjfilechooser;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

public class GtkFileChooserResourceTest {
	GettextResource r = new GettextResource("gtk20");

	@BeforeClass
	public static void setup() {
		Locale.setDefault(Locale.ITALIAN);
	}

	@Test
	public void testMoReaderGtk20() throws Exception {
		Locale.setDefault(Locale.ITALIAN);


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

	public static void main(String[] args) {
		GettextResource.main("-i", "/usr/share/locale/it/LC_MESSAGES/gtk20.mo");
		//GettextResource.main("-k", "Yesterday at %H:%M", "/usr/share/locale/ps/LC_MESSAGES/gtk20.mo");
	}
}
