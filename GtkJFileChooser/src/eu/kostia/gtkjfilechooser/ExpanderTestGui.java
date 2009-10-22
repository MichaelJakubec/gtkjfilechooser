package eu.kostia.gtkjfilechooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil;

public class ExpanderTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		JPanel panel = new JPanel(new BorderLayout());

		panel.add(new JButton("Top"), BorderLayout.PAGE_START);

		JButton bottom = new JButton("A Button");
		bottom.setPreferredSize(new Dimension(400, 400));		
		Expander expander = new Expander("Esplora altre cartelle", bottom);

		panel.add(expander, BorderLayout.CENTER);

		panel.add(new JButton("Bottom"), BorderLayout.PAGE_END);



		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().add(panel);	
		f.pack();

		JPanelUtil.centerOnScreen(f);		
		f.setVisible(true);

		expander.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (Expander.EXPANDED_STATUS_CHANGED.equals(evt.getPropertyName())){
					System.out.println("pack");
					f.pack();
				}
			}
		});
	}
}
