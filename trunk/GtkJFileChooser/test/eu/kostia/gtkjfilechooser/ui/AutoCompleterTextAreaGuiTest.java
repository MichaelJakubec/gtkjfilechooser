package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;

import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class AutoCompleterTextAreaGuiTest {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		JTextArea textArea = new JTextArea(20,20);
		new TestAutoCompleterTextArea(textArea);

		show(createPanel(new PanelElement(textArea, BorderLayout.PAGE_START)));
	}
}
