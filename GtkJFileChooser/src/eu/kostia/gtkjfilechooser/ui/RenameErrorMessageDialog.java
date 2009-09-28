package eu.kostia.gtkjfilechooser.ui;

import java.text.MessageFormat;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class RenameErrorMessageDialog {

	private final String renameErrorTitleText;
	private final String renameErrorText;
	private final String renameErrorFileExistsText;

	private JFileChooser chooser;

	public RenameErrorMessageDialog(JFileChooser chooser) {
		this.chooser = chooser;

		Locale locale = chooser.getLocale();
		renameErrorTitleText = UIManager.getString("FileChooser.renameErrorTitleText",
				locale);
		renameErrorText = UIManager.getString("FileChooser.renameErrorText", locale);
		renameErrorFileExistsText = UIManager.getString(
				"FileChooser.renameErrorFileExistsText", locale);
	}

	/**
	 * Show error file already exists.
	 * @param oldFileName
	 */
	public void showRenameErrorFileExists(String oldFileName) {
		JOptionPane.showMessageDialog(chooser, MessageFormat.format(
				renameErrorFileExistsText, oldFileName), renameErrorTitleText,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show generic rename error.
	 * @param oldFileName
	 */
	public void showRenameError(String oldFileName) {
		JOptionPane.showMessageDialog(chooser, MessageFormat.format(renameErrorText,
				oldFileName), renameErrorTitleText, JOptionPane.ERROR_MESSAGE);
	}

}
