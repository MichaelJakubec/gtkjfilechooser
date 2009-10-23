package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
		SaveDialogPanel saveDialogPanel = new SaveDialogPanel(fileExplorer);
//		saveDialogPanel.setExpanded(true);
		
		saveDialogPanel.add(new JButton("Bottom"), BorderLayout.PAGE_END);
		
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
