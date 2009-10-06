package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class SearchPanelLayoutTestGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane = new FilesListPane();
	private SearchPanel  searchPanel;

	public SearchPanelLayoutTestGui() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.filesPane = new FilesListPane();
		this.searchPanel = new SearchPanel(filesPane);

		getContentPane().add(createPanel(new PanelElement(searchPanel, BorderLayout.CENTER)));
	}




	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				SearchPanelLayoutTestGui test = new SearchPanelLayoutTestGui();

				test.setSize(new Dimension(800, 400));
				test.setVisible(true);
			}

		});


	}
}
