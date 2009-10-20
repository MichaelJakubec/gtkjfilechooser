package eu.kostia.gtkjfilechooser.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import eu.kostia.gtkjfilechooser.ActionDispatcher;
import eu.kostia.gtkjfilechooser.BasicActionDispatcher;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class ContextMenu extends JPopupMenu implements PropertyChangeListener, ActionDispatcher {
	static final public String SHOW_SIZE_COLUMN_CHANGED_PROPERTY = "ShowSizeColumnChanged";

	static final public String ACTION_ADD_BOOKMARK = "AddBookmark";

	static final public String ACTION_REFRESH = "refresh";

	static final public String ACTION_NEW_FOLDER = "New Folder";
	
	private ActionDispatcher actionDispatcher = new BasicActionDispatcher();
	
	private JMenuItem addToBookmarkMenuItem;
	
	public ContextMenu() {
		addPropertyChangeListener(this);


		addToBookmarkMenuItem = new JMenuItem();
		// TODO I18N
		addToBookmarkMenuItem.setText("Add to Bookmark");
		addToBookmarkMenuItem.setIcon(GtkStockIcon.get("gtk-add", Size.GTK_ICON_SIZE_MENU));
		addToBookmarkMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fireActionEvent(new ActionEvent(ContextMenu.this, ACTION_ADD_BOOKMARK.hashCode(), ACTION_ADD_BOOKMARK));				
			}			
		});
		
		add(addToBookmarkMenuItem);

		addSeparator();

		// Add "show hidden files" CheckBoxMenuItem
		JCheckBoxMenuItem showHiddenCheckBoxItem = new JCheckBoxMenuItem();
		// TODO I18N
		showHiddenCheckBoxItem.setText("Show hidden files");
		showHiddenCheckBoxItem.setSelected(GtkFileChooserSettings.get().getShowHidden());
		showHiddenCheckBoxItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
				boolean showHidden = source.isSelected();
				firePropertyChange(JFileChooser.FILE_HIDING_CHANGED_PROPERTY, showHidden, !showHidden);
				//getFileChooser().setFileHidingEnabled(!showHidden);
				// property 'showHidden' persisten in
				// GtkFileChooserUI#listenToFileChooserPropertyChanges

				// Update also the decorator for the filenameTextField
				//getFileChooserUIAccessor().showHiddenAutocompletion(showHidden);
			}
		});
		add(showHiddenCheckBoxItem);

		// Add "show file size column" CheckBoxMenuItem
		JCheckBoxMenuItem showFileSizeCheckBoxItem = new JCheckBoxMenuItem();
		// TODO I18N
		showFileSizeCheckBoxItem.setText("Show size column");
		showFileSizeCheckBoxItem.setSelected(GtkFileChooserSettings.get()
				.getShowSizeColumn());
		showFileSizeCheckBoxItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
				boolean showSizeColumn = source.isSelected();
				firePropertyChange(SHOW_SIZE_COLUMN_CHANGED_PROPERTY, !showSizeColumn, showSizeColumn);
			}
		});
		add(showFileSizeCheckBoxItem);
	}
	
	
	public void setAddToBookmarkMenuItemEnabled(boolean enabled) {
		// enable if path != null && path.isDirectory()
		addToBookmarkMenuItem.setEnabled(enabled);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		Object value = evt.getNewValue();
		
		Log.debug(property, " = ", value);		
	}


	@Override
	public void addActionListener(ActionListener l) {
		actionDispatcher.addActionListener(l);
		
	}

	@Override
	public void fireActionEvent(ActionEvent e) {
		actionDispatcher.fireActionEvent(e);
		
	}

	@Override
	public void removeActionListener(ActionListener l) {
		actionDispatcher.removeActionListener(l);
		
	}

	@Override
	public void removeAllActionListeners() {
		actionDispatcher.removeAllActionListeners();		
	}
	
}
