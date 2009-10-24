package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class SaveDialogPanelTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JButton fileExplorer = new JButton("File Explorer");
		fileExplorer.setPreferredSize(new Dimension(400, 400));
		final SaveDialogPanel saveDialogPanel = new SaveDialogPanel(fileExplorer);
		//		saveDialogPanel.setExpanded(true);

		JButton showFileButton = new JButton("Show filename");
		showFileButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Filename: " + saveDialogPanel.getFilename());				
			}
		});
		saveDialogPanel.add(showFileButton, BorderLayout.PAGE_END);

		f.getContentPane().add(saveDialogPanel);
		f.pack();

		JPanelUtil.centerOnScreen(f);
		f.setVisible(true);

		saveDialogPanel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (Expander.EXPANDED_STATUS_CHANGED.equals(evt.getPropertyName())) {
					f.pack();
				}
			}
		});
	}
}
