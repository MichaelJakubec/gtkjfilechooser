package eu.kostia.gtkjfilechooser;

/**
 * Test for the .po file format
 * 
 * 
 * @author Costantino Cerbo
 * 
 */
public class POTest {

	//TODO test also xgettext and mergetxt
	public POTest() {
		System.out.println(_("Hallo, how are you?"));
		System.out.println(_("Fine, thanks!"));
	}

	static public String _(String msgid) {
		return msgid;
	}

	public static void main(String[] args) {
		new POTest();
	}
}
