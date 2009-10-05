package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.FileEntry;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class FilesListPaneAddTestGui {

	private FilesListPane testAddFiles() throws Exception {
		FilesListPane pane = new FilesListPane();
		pane.addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		return pane;
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		FilesListPaneAddTestGui test = new FilesListPaneAddTestGui();

		FilesListPane pane = test.testAddFiles();
		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));

		pane.addFileEntry(new FileEntry(new File("Hello"), new Date(1254779700475L)));
		Thread.sleep(1000);
		pane.addFileEntry(new FileEntry(new File("to"), new Date(254779700475L)));
		Thread.sleep(1000);
		pane.addFileEntry(new FileEntry(new File("everyone"), new Date(25779700475L)));
	}
}
