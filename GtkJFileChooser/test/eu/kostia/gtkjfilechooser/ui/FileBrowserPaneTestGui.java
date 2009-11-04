package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.GtkFileView;
import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;


public class FileBrowserPaneTestGui implements PropertyChangeListener {
	FileBrowserPane fileBrowser;
	JTextField locationField;
	JComboBox fileSelectionModeComboBox;

	public FileBrowserPaneTestGui() {
		File startDir = new File(System.getProperty("user.home"));
		fileBrowser = new FileBrowserPane(startDir, new GtkFileView());
		locationField = new JTextField(20);

		JButton goButton = new JButton();

		AbstractAction changeDirAction = new AbstractAction("Change Dir") {

			@Override
			public void actionPerformed(ActionEvent e) {
				String location = locationField.getText();
				fileBrowser.setCurrentDir(new File(location));
			}
		};
		locationField.registerKeyboardAction(changeDirAction, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
		goButton.setAction(changeDirAction);

		JButton showSelectionsButton = new JButton("Show Selections");
		showSelectionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File[] files = fileBrowser.getSelectedFiles();
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
				}
			}
		});

		JButton createFolderButton = new JButton("Create folder");
		createFolderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileBrowser.createFolder();
			}
		});

		JCheckBox multiSelection = new JCheckBox("Multi");
		multiSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				fileBrowser.setIsMultiSelectionEnabled(selected);
			}
		});

		String[] options = {"Files only", "Directories only", "Files and directories"};
		fileSelectionModeComboBox = new JComboBox(options);
		fileSelectionModeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileBrowser.setFileSelectionMode(fileSelectionModeComboBox.getSelectedIndex());
			}
		});
		fileSelectionModeComboBox.setSelectedIndex(JFileChooser.FILES_ONLY);


		fileBrowser.addPropertyChangeListener(this);

		fileBrowser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ActionCommand: " + e.getActionCommand());
			}
		});


		show(createPanel(
				new PanelElement(fileBrowser, BorderLayout.CENTER),
				new PanelElement(createPanelBoxLayout(locationField, goButton), BorderLayout.PAGE_START),
				new PanelElement(createPanel(showSelectionsButton, createFolderButton, multiSelection, fileSelectionModeComboBox), BorderLayout.PAGE_END)
		));
	}



	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		new FileBrowserPaneTestGui();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		Object value = evt.getNewValue();

		Log.debug(property, " = ", value);

		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(property)) {
			locationField.setText(((File)value).getAbsolutePath());
		}		
	}
}
