package eu.kostia.gtkjfilechooser.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.kostia.gtkjfilechooser.FileSearch;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.FileSearch.FileSearchHandler;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

/**
 * Panel to enter the term for a search.
 * @author c.cerbo
 *
 */
public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private FilesListPane filesPane;

	private FileSearch fileSearch;

	private JLabel searchLabel;

	private JTextField searchTextField;

	private JButton stopButton;

	public SearchPanel(FilesListPane pane) {		
		this.filesPane = pane;

		//		setLayout(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		/**
		 * Search label
		 */
		// TODO I18N
		searchLabel = new JLabel("Search:");
		add(searchLabel);
		add(Box.createRigidArea(new Dimension(10,0)));

		/**
		 * Search TextField
		 */
		searchTextField = new JTextField();
		int height = searchTextField.getPreferredSize().height;
		searchTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, height));
		searchTextField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				filesPane.clean();
				stopSearch();
				fileSearch = new FileSearch(System.getProperty("user.home"), searchTextField.getText(), new ThisFileSearchHandler());
				fileSearch.setSearchHidden(GtkFileChooserSettings.get().getShowHidden());
				fileSearch.start();					
			}
		});
		add(searchTextField);

		/**
		 * Stop Button
		 */
		stopButton = new JButton(GtkStockIcon.get("gtk-stop", Size.GTK_ICON_SIZE_MENU));
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				stopSearch();
			}			
		});
		add(stopButton);
	}

	@Override
	public boolean requestFocusInWindow(){
		return searchTextField.requestFocusInWindow();
	}

	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		filesPane.setCursor(cursor);
	}

	private void stopSearch() {
		if (fileSearch != null) {
			fileSearch.stop();
			fileSearch = null;
		}
	}

	private class ThisFileSearchHandler implements FileSearchHandler {

		@Override
		public void found(File file) {
			filesPane.addFile(file);			
		}

		@Override
		public void finished(Status status) {
			setCursor(Cursor.getDefaultCursor());				
		}	

	}
}
