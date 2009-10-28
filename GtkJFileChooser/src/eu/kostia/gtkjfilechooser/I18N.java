package eu.kostia.gtkjfilechooser;

/**
 * Wrapper around {@link GettextResource}. You should use this class as entry
 * point for the i18n instead of {@link GettextResource}.
 * 
 * @author Costantino Cerbo
 * 
 */
public class I18N {
	static private GettextResource RESOURCE = GettextResource.hasTranslation("gtk20") ? new GettextResource("gtk20") : null;

	/**
	 * Returns the translated string without mnemonics.
	 * 
	 * @param msgid
	 * @return the translated string without mnemonics.
	 */
	static final public String _(String msgid) {
		// Return a string without mnemonic
		return getString(msgid).replace("_", "");
	}

	/**
	 * @see String#format(String, Object...)
	 */
	static final public String _(String msgid, Object args) {
		return String.format(_(msgid), args);
	}

	/**
	 * Returns the mnemonic associate with this message, or 0 if none.
	 * 
	 * @param msgid
	 * @return the mnemonic associate with this message, or 0 if none.
	 */
	static final public int getMnemonic(String msgid) {
		String msg = getString(msgid);
		int indexOf = msg.indexOf('_');
		if (indexOf >= 0) {
			return msg.charAt(indexOf + 1);
		}
		return 0;
	}

	/**
	 * When RESOURCE is null or msgstr is empty, return the msgid (without classifier) otherwise the found msgstr.
	 */
	private static String getString(String msgid) {
		if (RESOURCE != null) {
			return RESOURCE.getString(msgid);
		}

		int indexOf = msgid.indexOf('|');
		if (indexOf < 0) {
			return msgid;
		}

		return msgid.substring(indexOf + 1);
	}
}
