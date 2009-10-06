package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class SearchPanelTestGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane = new FilesListPane();
	private SearchPanel  searchPanel;

	public SearchPanelTestGui() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.filesPane = new FilesListPane();
		this.searchPanel = new SearchPanel(filesPane);

		getContentPane().add(createPanel(
				0,10, // horizontal and vertical gap
				new PanelElement(searchPanel, BorderLayout.PAGE_START),
				new PanelElement(filesPane, BorderLayout.CENTER)
		));
	}




	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				SearchPanelTestGui test = new SearchPanelTestGui();

				test.pack();
				test.setVisible(true);
			}

		});


	}
}
