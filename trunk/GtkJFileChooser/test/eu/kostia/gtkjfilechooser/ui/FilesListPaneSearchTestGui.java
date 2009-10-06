package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.FileSearch;
import eu.kostia.gtkjfilechooser.FileSearch.FileSearchHandler;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class FilesListPaneSearchTestGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private FilesListPane pane = new FilesListPane();
	private FileSearch fileSearch;
	
	public FilesListPaneSearchTestGui() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void testSearch() {				
		pane.addActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesListPane p = (FilesListPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		
		
		final JTextField searchTextField = new JTextField(40);
		searchTextField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Search started");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				pane.clean();
				fileSearch = new FileSearch(System.getProperty("user.home"), searchTextField.getText(), new ThisFileSearchHandler());
				fileSearch.start();					
			}
			
		});
	
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileSearch != null) {
					fileSearch.stop();
					fileSearch = null;
				}
			}			
		});
		
		JPanel topPanel = createPanel(new PanelElement(searchTextField, BorderLayout.CENTER), new PanelElement(stopButton, BorderLayout.LINE_END));
		getContentPane().add(createPanel(
				new PanelElement(topPanel, BorderLayout.PAGE_START),
				new PanelElement(pane, BorderLayout.CENTER)));
	}
	

	private class ThisFileSearchHandler implements FileSearchHandler {

		@Override
		public void found(File file) {
			pane.addFile(file);			
		}

		@Override
		public void finished(Status status) {
			setCursor(Cursor.getDefaultCursor());
			System.out.println("Search " + (status == Status.COMPLETED ? "completed!" : "interruped!"));						
		}	
		
	}

	/**
	 * Main for testing purpose.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				FilesListPaneSearchTestGui test = new FilesListPaneSearchTestGui();

				test.testSearch();	
				test.pack();
				test.setVisible(true);
			}
			
		});


	}
}
