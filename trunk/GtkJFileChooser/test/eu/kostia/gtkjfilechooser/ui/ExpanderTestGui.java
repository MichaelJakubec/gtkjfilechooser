package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.SpringLayoutUtil;

public class ExpanderTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		JPanel panel = new JPanel(new BorderLayout());

		JPanel saveTopPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		saveTopPanel.setLayout(layout);
	

		JLabel nameLabel = new JLabel("Name:");
		JTextField nameTextField = new JTextField();
		JLabel saveFolderLabel = new JLabel("Save in folder:");
		JComboBox folderComboBox = new JComboBox(new String[] { "Item 1", "Item 2",
				"Item 3" });

		saveTopPanel.add(nameLabel);
		saveTopPanel.add(nameTextField);

		saveTopPanel.add(saveFolderLabel);
		saveTopPanel.add(folderComboBox);

		//Lay out the panel.
        SpringLayoutUtil.makeCompactGrid(saveTopPanel,
                                        2, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad

        Dimension size = saveTopPanel.getPreferredSize();
        size.width = 600;
        saveTopPanel.setPreferredSize(size);
		panel.add(saveTopPanel, BorderLayout.PAGE_START);

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
				if (Expander.EXPANDED_STATUS_CHANGED.equals(evt.getPropertyName())) {
					System.out.println("pack");
					f.pack();
				}
			}
		});
	}
}
