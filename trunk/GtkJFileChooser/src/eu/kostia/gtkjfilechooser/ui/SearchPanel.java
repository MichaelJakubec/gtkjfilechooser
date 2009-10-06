package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.kostia.gtkjfilechooser.FileSearch;
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

		setLayout(new BorderLayout());
		//setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		// TODO I18N
		searchLabel = new JLabel("Search:");
		add(searchLabel, BorderLayout.LINE_START);


		searchTextField = new JTextField();		
		searchTextField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				debug("Search started");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				filesPane.clean();
				fileSearch = new FileSearch(System.getProperty("user.home"), searchTextField.getText(), new ThisFileSearchHandler());
				fileSearch.start();					
			}
		});
		searchLabel.setLabelFor(searchTextField);
		add(searchTextField, BorderLayout.CENTER);

		stopButton = new JButton(GtkStockIcon.get("gtk-stop", Size.GTK_ICON_SIZE_BUTTON));
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileSearch != null) {
					fileSearch.stop();
					fileSearch = null;
				}
			}			
		});
		add(stopButton, BorderLayout.LINE_END);
	}

	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		filesPane.setCursor(cursor);
	}

	private void debug(String msg){
		Logger.getLogger(SearchPanel.class.getName()).finest(msg);
	}


	private class ThisFileSearchHandler implements FileSearchHandler {

		@Override
		public void found(File file) {
			filesPane.addFile(file);			
		}

		@Override
		public void finished(Status status) {
			setCursor(Cursor.getDefaultCursor());
			debug("Search " + (status == Status.COMPLETED ? "completed!" : "interruped!"));						
		}	

	}
}
