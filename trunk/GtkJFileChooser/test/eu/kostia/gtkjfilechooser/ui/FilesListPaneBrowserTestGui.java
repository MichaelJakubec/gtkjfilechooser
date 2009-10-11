package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;


public class FilesListPaneBrowserTestGui {
	private FilesListPane createFileBrowser(File currentDir) {
		final FilesListPane pane = new FilesListPane();

		pane.addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				String cmd = e.getActionCommand();
				File selectedFile = p.getSelectedFile();
				Log.debug(cmd, ": ", selectedFile);

				if (FilesListPane.DOUBLE_CLICK.equals(cmd)){
					pane.clean();
					File[] files = selectedFile.listFiles();
					if (files != null){
						pane.setModel(Arrays.asList(files));					
					}
				}

			}
		});

		File[] files = currentDir.listFiles();
		if (files != null) {
			pane.setModel(Arrays.asList(files));
		}

		return pane;
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		File currentDir = new File(System.getProperty("user.home"));
		FilesListPaneBrowserTestGui test = new FilesListPaneBrowserTestGui();

		FilesListPane pane = test.createFileBrowser(currentDir);


		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));
	}
}
