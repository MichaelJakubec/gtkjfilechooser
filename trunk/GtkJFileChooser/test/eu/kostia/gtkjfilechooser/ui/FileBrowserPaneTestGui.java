package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;


public class FileBrowserPaneTestGui {
	FileBrowserPane fileBrowser;
	JTextField locationField;

	public FileBrowserPaneTestGui() {
		File startDir = new File(System.getProperty("user.home"));
		fileBrowser = new FileBrowserPane(startDir);
		locationField = new JTextField(20);
		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String location = locationField.getText();
				fileBrowser.setCurrentDir(new File(location));
			}
		});

		fileBrowser.addPropertyChangeListener(propertyChange());

		show(createPanel(
				new PanelElement(fileBrowser, BorderLayout.CENTER),
				new PanelElement(createPanel(locationField, goButton), BorderLayout.PAGE_END)
		));
	}

	private PropertyChangeListener propertyChange() {
		return new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String property = evt.getPropertyName();
				Object value = evt.getNewValue();

				if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(property)) {
					locationField.setText(((File)value).getAbsolutePath());
				}

			}
		};
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		new FileBrowserPaneTestGui();
	}
}
