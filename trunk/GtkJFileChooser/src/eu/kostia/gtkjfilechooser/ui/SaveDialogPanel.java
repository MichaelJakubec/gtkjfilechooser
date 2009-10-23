package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import eu.kostia.gtkjfilechooser.BookmarkManager;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.Path;
import eu.kostia.gtkjfilechooser.SpringLayoutUtil;

public class SaveDialogPanel extends JPanel implements PropertyChangeListener {
	private JTextField nameTextField;
	private JLabel saveFolderLabel;
	private JComboBox foldersComboBox;
	private Expander expander;
	
	public SaveDialogPanel(JComponent fileExplorerPanel) {
		super(new BorderLayout());

		JPanel saveTopPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		saveTopPanel.setLayout(layout);

		//TODO I18N
		JLabel nameLabel = new JLabel("Name:");
		nameTextField = new JTextField();
		saveFolderLabel = new JLabel("Save in folder:");
		initFoldersComboBox();
		
		saveTopPanel.add(nameLabel);
		saveTopPanel.add(nameTextField);

		saveTopPanel.add(saveFolderLabel);
		saveTopPanel.add(foldersComboBox);

		// Lay out the panel.
		SpringLayoutUtil.makeCompactGrid(saveTopPanel, 2, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		Dimension size = saveTopPanel.getPreferredSize();
		size.width = 600;
		saveTopPanel.setPreferredSize(size);
		add(saveTopPanel, BorderLayout.PAGE_START);

		//TODO I18N
		expander = new Expander("Browse for other folders", fileExplorerPanel);
		expander.addPropertyChangeListener(this);
		add(expander, BorderLayout.CENTER);		
	}

	private void initFoldersComboBox() {		
		foldersComboBox = new JComboBox();
		foldersComboBox.setMaximumRowCount(30);
		foldersComboBox.setRenderer(new FileComboBoxRenderer(foldersComboBox));
				
		List<Path> locations = new ArrayList<Path>();
		locations.addAll(FreeDesktopUtil.getBasicLocations());
		locations.addAll(FreeDesktopUtil.getRemovableDevices());
		locations.addAll(new BookmarkManager().getAll());
			
		foldersComboBox.setModel(new DefaultComboBoxModel(locations.toArray()));				
	}

	public boolean isExpanded() {
		return expander.isExpanded();
	}

	public void setExpanded(boolean expanded) {
		expander.setExpanded(expanded);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (Expander.EXPANDED_STATUS_CHANGED.equals(property)) {
			saveFolderLabel.setEnabled(!expander.isExpanded());
			foldersComboBox.setEnabled(!expander.isExpanded());			
		}
		firePropertyChange(property, evt.getOldValue(), evt.getNewValue());		
	}
}
