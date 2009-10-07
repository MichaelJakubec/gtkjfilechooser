package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class FilesListPaneRecentTestGui {

	private FilesListPane testShowRecentUsedFiles() {
		FilesListPane pane = new FilesListPane();
		pane.addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		List<File> fileEntries = new RecentlyUsedManager(30).getRecentFiles();
		pane.updateModel(fileEntries);
		return pane;
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		FilesListPaneRecentTestGui test = new FilesListPaneRecentTestGui();

		FilesListPane pane = test.testShowRecentUsedFiles();
		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));
	}
}
