package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import javax.swing.JTextField;

public class FileAutoCompleterGuiTest {

	public static void main(String[] args) {
		
		JTextField textField = new JTextField(40);
		new FileAutoCompleter(textField);
		
		show(createPanel(textField));		
	}
}
