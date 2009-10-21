package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ActionPath.RECENTLY_USED_PANEL_ID;
import static eu.kostia.gtkjfilechooser.ActionPath.SEARCH_PANEL_ID;
import static eu.kostia.gtkjfilechooser.NavigationKeyBinding.*;
import static eu.kostia.gtkjfilechooser.ui.ContextMenu.ACTION_ADD_BOOKMARK;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static javax.swing.JFileChooser.*;
import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;

import eu.kostia.gtkjfilechooser.ActionPath;
import eu.kostia.gtkjfilechooser.FileFilterWrapper;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.Log;
import eu.kostia.gtkjfilechooser.NavigationKeyBinding;
import eu.kostia.gtkjfilechooser.Path;
import eu.kostia.gtkjfilechooser.BookmarkManager.GtkBookmark;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings.Mode;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

/**
 * GtkFileChooserUI basen on the Metal L&F implementation of a FileChooser.
 * 
 * @version 1.95 10/02/08
 * @author Costantino Cerbo, Jeff Dinkins
 */
public class GtkFileChooserUI extends BasicFileChooserUI implements Serializable,
PropertyChangeListener, ActionListener {

	private static final String ACTION_SELECTED_BOOKMARK = "selected bookmark";

	private static final String ANCESTOR_PROPERTY = "ancestor";

	private static final String COMPONENT_ORIENTATION_PROPERTY = "componentOrientation";

	private static final String CURRENT_PANEL_CHANGED = "CurrentPanelChanged";


	private static final long serialVersionUID = 10L;

	public GtkFileChooserUI(JFileChooser chooser) {
		super(chooser);
		chooser.setFileHidingEnabled(!GtkFileChooserSettings.get().getShowHidden());

		if (chooser.getCurrentDirectory() == null) {
			chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		}

		// Persist component bounds and sizes
		chooser.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				Rectangle bound = e.getComponent().getBounds();
				GtkFileChooserSettings.get().setBound(bound);
			}
		});

		// Add key binding
		NavigationKeyBinding keyBinding = new NavigationKeyBinding(chooser);
		keyBinding.addActionListener(this);
	}

	/**
	 * Method invoked when the FileChooser is closed.
	 */
	private void onClosing() {
		if (searchPanel != null) {
			searchPanel.stopSearch();
		}
	}

	private GtkPathBar comboButtons;

	// private FilterComboBoxChangeListener filterComboBoxModel;

	private JTextField fileNameTextField;

	/**
	 * Decorator for auto completion for #fileNameTextField
	 */
	private PathAutoCompleter pathAutoCompletion;

	/**
	 * The panel on the left with locations and bookmarks
	 */
	private GtkLocationsPane locationsPane;

	/**
	 * The panel on the top with the button to show/hide the location textfiels,
	 * the combo buttons for the path and the textfield for the location.
	 */
	private JPanel topPanel;

	/**
	 * Panel with the location text field.
	 */
	private JPanel filenamePanel;

	/**
	 * Button to enable/disable the location text field.
	 */
	private JToggleButton showPositionButton;

	/**
	 * Names of the "cards" in the topPanel.
	 */
	static private final String TOP_PATHBAR_PANEL = "Path bar panel on the top";
	static private final String TOP_SEARCH_PANEL = "Search panel on the top";

	/**
	 * Panel mit CardLayout used to show one of the following three panels: the
	 * File-Browser panel, the Recently-Used panel and the Search panel.
	 */
	private JPanel rightPanel = new JPanel(new CardLayout());

	/**
	 * Names of the "cards" in the rightPanel.
	 */
	static private final String FILEBROWSER_PANEL = "fileBrowserPane";
	static private final int FILEBROWSER_PANEL_ID = 1000;

	static private final String RECENTLY_USED_PANEL = "recentlyUsedPane";

	static private final String SEARCH_PANEL = "searchFilesPane";	

	private int currentPanelId = FILEBROWSER_PANEL_ID;

	/**
	 * The panel with for the file/directory navigation.
	 */
	private FileBrowserPane fileBrowserPane;

	/**
	 * Table to show the recent used files
	 */
	private FilesListPane recentlyUsedPane;

	/**
	 * Table to show the results of a search
	 */
	private FilesListPane searchFilesPane;

	/**
	 * Panel on the top with the text field to do the search.
	 */
	private SearchPanel searchPanel;

	/**
	 * Combox with file filters.
	 */
	private JComboBox filterComboBox;

	private JButton approveButton;
	private JButton cancelButton;

	private JPanel buttonPanel;

	private JButton addBookmarkButton;
	private JButton removeBookmarkButton;

	private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);

	// Preferred and Minimum sizes for the dialog box
	private static int PREF_WIDTH = 700;
	private static int PREF_HEIGHT = 326;
	private static Dimension PREF_SIZE = new Dimension(PREF_WIDTH, PREF_HEIGHT);

	private static int MIN_WIDTH = 700;
	private static int MIN_HEIGHT = 326;
	private static Dimension MIN_SIZE = new Dimension(MIN_WIDTH, MIN_HEIGHT);

	private static int LIST_PREF_WIDTH = 405;
	private static int LIST_PREF_HEIGHT = 135;
	private static Dimension LIST_PREF_SIZE = new Dimension(LIST_PREF_WIDTH,
			LIST_PREF_HEIGHT);

	private int fileNameLabelMnemonic = 0;
	private String fileNameLabelText = null;

	private String filesOfTypeLabelText = null;

	private String newFolderToolTipText = null;
	private String newFolderAccessibleName = null;

	//
	// ComponentUI Interface Implementation methods
	//
	public static ComponentUI createUI(JComponent c) {
		GtkFileChooserUI ui = new GtkFileChooserUI((JFileChooser) c);
		return ui;
	}

	@Override
	public void uninstallComponents(JFileChooser fc) {
		fc.removeAll();
		buttonPanel = null;
	}

	private void createFilenamePanel(JFileChooser fc) {
		// FileName label and textfield
		filenamePanel = new JPanel();
		filenamePanel.setLayout(new BoxLayout(filenamePanel, BoxLayout.LINE_AXIS));

		JLabel fileNameLabel = new JLabel(fileNameLabelText);
		fileNameLabel.setDisplayedMnemonic(fileNameLabelMnemonic);
		filenamePanel.add(fileNameLabel);

		fileNameTextField = new JTextField() {
			private static final long serialVersionUID = GtkFileChooserUI.serialVersionUID;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(Short.MAX_VALUE, super.getPreferredSize().height);
			}
		};
		filenamePanel.add(fileNameTextField);
		fileNameLabel.setLabelFor(fileNameTextField);
		fileNameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!getFileChooser().isMultiSelectionEnabled()) {
					fileBrowserPane.clearSelection();
				}
			}
		});

		fileNameTextField.addActionListener(new SelectPathAction() {
			@Override
			protected File getSelectedPath() {
				String text = fileNameTextField.getText();
				File path = new File(text);
				if (!path.isAbsolute()) {
					path = new File(getFileChooser().getCurrentDirectory()
							.getAbsolutePath()
							+ File.separator + text);
				}
				return path;
			}
		});

		// Add decorator for auto completion
		pathAutoCompletion = new PathAutoCompleter(fileNameTextField);
		pathAutoCompletion.setShowHidden(GtkFileChooserSettings.get().getShowHidden());
		pathAutoCompletion.setCurrentPath(getFileChooser().getCurrentDirectory()
				.getAbsolutePath());

		if (fc.isMultiSelectionEnabled()) {
			setFileName(fileNameString(fc.getSelectedFiles()));
		} else {
			setFileName(fileNameString(fc.getSelectedFile()));
		}
	}

	@Override
	public void installComponents(final JFileChooser fc) {
		fc.setBorder(new EmptyBorder(12, 12, 11, 11));
		fc.setLayout(new BorderLayout(0, 11));

		fileBrowserPane = new FileBrowserPane(getFileChooser().getCurrentDirectory());
		fc.addPropertyChangeListener(this);
		fileBrowserPane.addPropertyChangeListener(this);
		fileBrowserPane.addActionListener(this);


		// ********************************* //
		// **** Construct the top panel **** //
		// ********************************* //

		showPositionButton = new JToggleButton(GtkStockIcon.get("gtk-edit",
				Size.GTK_ICON_SIZE_BUTTON));
		showPositionButton
		.setSelected(GtkFileChooserSettings.get().getLocationMode() == Mode.FILENAME_ENTRY);
		showPositionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mode mode = showPositionButton.isSelected() ? Mode.FILENAME_ENTRY
						: Mode.PATH_BAR;
				GtkFileChooserSettings.get().setLocationMode(mode);
			}
		});

		// CurrentDir Combo Buttons
		comboButtons = new GtkPathBar(getFileChooser().getCurrentDirectory());
		comboButtons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDirectoryChanged(comboButtons.getCurrentDirectory());
			}
		});

		/**
		 * Pathbar
		 */
		JPanel pathbar = new JPanel(new BorderLayout());
		pathbar.add(createPanel(new PanelElement(createPanel(showPositionButton),
				BorderLayout.LINE_START), new PanelElement(comboButtons,
						BorderLayout.CENTER)), BorderLayout.CENTER);
		if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
			JButton newDirButton = new JButton(newFolderAccessibleName);
			newDirButton.setToolTipText(newFolderToolTipText);
			newDirButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY,	newFolderAccessibleName);
			newDirButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			newDirButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
			newDirButton.setMargin(shrinkwrap);

			// TODO add to panel in the right position
			// topPanel1.add(JPanelUtil.createPanel(new
			// FlowLayout(FlowLayout.RIGHT), newDirButton),
			// BorderLayout.LINE_END);

			//TODO add action
		}

		/**
		 * Filename textfield
		 */
		createFilenamePanel(fc);
		showPositionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton) e.getSource();
				filenamePanel.setVisible(btn.isSelected());
			}
		});
		filenamePanel.setVisible(showPositionButton.isSelected());

		// First card put in the topPanel
		JPanel topPanelDefault = new JPanel(new BorderLayout());
		topPanelDefault.setLayout(new BoxLayout(topPanelDefault, BoxLayout.PAGE_AXIS));
		topPanelDefault.add(pathbar);
		topPanelDefault.add(filenamePanel);

		topPanel = new JPanel(new CardLayout());
		topPanel.add(topPanelDefault, TOP_PATHBAR_PANEL);
		// Add the top panel to the fileChooser
		fc.add(topPanel, BorderLayout.NORTH);

		/***********************************************
		 * Accessory Panel (for example image preview) *
		 ***********************************************/
		fc.add(getAccessoryPanel(), BorderLayout.AFTER_LINE_ENDS);
		JComponent accessory = fc.getAccessory();
		if (accessory != null) {
			getAccessoryPanel().add(accessory);
		}

		/********************************
		 * Central Panel (File Browser) *
		 ********************************/
		addFileBrowserPane(fc);



		// Buttons
		getButtonPanel().setLayout(new ButtonAreaLayout());

		/************************
		 * File Filter Combobox *
		 ************************/
		if (filterComboBox == null) {
			createFilterComboBox();
		}	
		//TODO move to other position
		getButtonPanel().add(filterComboBox);

		cancelButton = new JButton(cancelButtonText);
		cancelButton.setIcon(GtkStockIcon.get("gtk-cancel", Size.GTK_ICON_SIZE_BUTTON));
		cancelButton.setToolTipText(cancelButtonToolTipText);
		cancelButton.addActionListener(getCancelSelectionAction());
		getButtonPanel().add(cancelButton);

		approveButton = new JButton(getApproveButtonText(fc));
		if (fc.getDialogType() == JFileChooser.OPEN_DIALOG) {
			approveButton.setIcon(GtkStockIcon.get("gtk-open", Size.GTK_ICON_SIZE_BUTTON));
		} else if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
			approveButton.setIcon(GtkStockIcon.get("gtk-save", Size.GTK_ICON_SIZE_BUTTON));
		}

		approveButton.addActionListener(getApproveSelectionAction());
		approveButton.setToolTipText(getApproveButtonToolTipText(fc));
		getButtonPanel().add(approveButton);

		if (fc.getControlButtonsAreShown()) {
			fc.add(getButtonPanel(), BorderLayout.PAGE_END);
		}
	}

	/**
	 * Update the decorator current path and empty the text field for the path.
	 */
	private void updateFileNameField() {
		pathAutoCompletion.setCurrentPath(getFileChooser().getCurrentDirectory()
				.getAbsolutePath());
		fileNameTextField.setText("");
	}

	private void addFileBrowserPane(final JFileChooser fc) {
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setContinuousLayout(true);

		// Left Panel (Bookmarks)
		addBookmarkButton = new JButton("Add"); // TODO I18N
		addBookmarkButton.setIcon(GtkStockIcon.get("gtk-add", Size.GTK_ICON_SIZE_BUTTON));
		addBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addToBookmarks();			
			}
		});
		addBookmarkButton.setEnabled(false);

		removeBookmarkButton = new JButton("Remove"); // TODO I18N
		// it will be enabled, when we select a bookmark.
		removeBookmarkButton.setEnabled(false);
		removeBookmarkButton.setIcon(GtkStockIcon.get("gtk-remove",
				Size.GTK_ICON_SIZE_BUTTON));
		removeBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				locationsPane.removeSelectedBookmark();
			}
		});

		JPanel buttonPanel = JPanelUtil.createPanel(new GridLayout(1, 2),
				addBookmarkButton, removeBookmarkButton);

		locationsPane = new GtkLocationsPane();
		locationsPane.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Path entry = ((GtkLocationsPane) e.getSource()).getCurrentPath();

				if (entry instanceof ActionPath) {
					ActionPath action = (ActionPath) entry;
					getFileChooser().firePropertyChange(CURRENT_PANEL_CHANGED, currentPanelId, action.getId());
					return;
				}

				getFileChooser().firePropertyChange(CURRENT_PANEL_CHANGED, currentPanelId, FILEBROWSER_PANEL_ID);				

				if (entry != null && entry.getLocation() != null) {
					ActionEvent evt = new ActionEvent(locationsPane, -1, ACTION_SELECTED_BOOKMARK);
					// fire an action event on the current ActionListener (GtkFileChooser)
					GtkFileChooserUI.this.actionPerformed(evt);					
				}
			}
		});

		mainPanel.add(JPanelUtil.createPanel(new PanelElement(locationsPane,
				BorderLayout.CENTER),
				new PanelElement(buttonPanel, BorderLayout.PAGE_END)));

		// Right Panel (file browser)
		fileBrowserPane.setPreferredSize(LIST_PREF_SIZE);

		rightPanel.add(fileBrowserPane, FILEBROWSER_PANEL);
		mainPanel.add(rightPanel);

		installListenersForBookmarksButtons();

		// ad to the file chooser
		fc.add(mainPanel, BorderLayout.CENTER);
	}

	//	/**
	//	 * Add on the botton-right corner a combobox for file filtering.
	//	 */
	//	private JPanel addFilterCombobox(JComponent component) {
	//		if (filterComboBox == null) {
	//			createFilterComboBox();
	//		}		
	//
	//		JPanel fileBrowserSubPanel = JPanelUtil.createPanel(new PanelElement(component,
	//				BorderLayout.CENTER), new PanelElement(JPanelUtil.createPanel(
	//						new GridLayout(1, 3), new JLabel(), new JLabel(), filterComboBox),
	//						BorderLayout.PAGE_END));
	//
	//		return fileBrowserSubPanel;
	//	}

	private void createFilterComboBox() {
		filterComboBox = new JComboBox();
		filterComboBox.putClientProperty(
				AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, filesOfTypeLabelText);

		filterComboBox.setPreferredSize(new Dimension(150, (int) removeBookmarkButton
				.getPreferredSize().getHeight()));
		filterComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileFilter filter = (FileFilter) filterComboBox.getSelectedItem();
				getFileChooser().setFileFilter(filter);
			}
		});
	}



	private void selectFilterInCombo() {
		FileFilter filterInChooser = getFileChooser().getFileFilter();
		FileFilter filterInCombo = (FileFilter) filterComboBox.getSelectedItem();

		if (filterInChooser == null || filterInCombo == null) {
			return;
		}
		if (!filterInCombo.getDescription().equals(filterInChooser.getDescription())) {
			// Select on the combo the just now changed
			// file-filter value if different.
			for (int i = 0; i < filterComboBox.getItemCount(); i++) {
				FileFilter item = (FileFilter) filterComboBox.getItemAt(i);
				if (item.getDescription().equals(filterInChooser.getDescription())) {
					filterComboBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	private void doChoosableFileFilterChanged(FileFilter[] filters) {
		filterComboBox.removeAllItems();

		for (FileFilter filter : filters) {
			filterComboBox.addItem(wrapFileFilter(filter));
		}

		if (filterComboBox.getItemCount() == 0) {
			// Add Default AcceptAll file filter
			filterComboBox.addItem(wrapFileFilter(getFileChooser()
					.getAcceptAllFileFilter()));
		}
	}

	/**
	 * Wrap a FileFiler setting toString() like getDescription()
	 */
	private FileFilter wrapFileFilter(final FileFilter filter) {
		return new FileFilter() {

			@Override
			public String getDescription() {
				return filter.getDescription();
			}

			@Override
			public boolean accept(File f) {
				return filter.accept(f);
			}

			@Override
			public String toString() {
				return getDescription();
			}
		};
	}

	/**
	 * Create the view for the search, with a text field on the top and a file
	 * list table on the center-right.
	 */
	private void createSearchPane() {
		searchFilesPane = new FilesListPane();
		int selectionMode = getFileChooser().isMultiSelectionEnabled() ? MULTIPLE_INTERVAL_SELECTION
				: SINGLE_SELECTION;
		searchFilesPane.setSelectionMode(selectionMode);
		searchFilesPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getFileChooser().isMultiSelectionEnabled()) {
					getFileChooser().setSelectedFiles(searchFilesPane.getSelectedFiles());
				} else {
					getFileChooser().setSelectedFile(searchFilesPane.getSelectedFile());
				}

				if (FilesListPane.DOUBLE_CLICK_ID == e.getID()) {
					// On double click close the file chooser.
					getFileChooser().approveSelection();
				}
			}
		});

		// add listener on ENTER pressed for select/browse
		searchFilesPane.addActionListener(new SelectPathAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (FilesListPane.ENTER_PRESSED_ID == e.getID()) {
					super.actionPerformed(e);
				}
			}

			@Override
			protected File getSelectedPath() {
				return searchFilesPane.getSelectedFile();
			}

		});

		searchPanel = new SearchPanel(searchFilesPane);

		topPanel.add(searchPanel, TOP_SEARCH_PANEL);
		rightPanel.add(searchFilesPane, SEARCH_PANEL);
	}

	FilesListPane getRecentlyUsedPane() {
		return recentlyUsedPane;
	}

	private void createRecentlyUsedPane() {
		/**
		 * Create an empty table
		 */
		recentlyUsedPane = new FilesListPane();
		int selectionMode = getFileChooser().isMultiSelectionEnabled() ? MULTIPLE_INTERVAL_SELECTION
				: SINGLE_SELECTION;
		recentlyUsedPane.setSelectionMode(selectionMode);
		recentlyUsedPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getFileChooser().isMultiSelectionEnabled()) {
					getFileChooser()
					.setSelectedFiles(recentlyUsedPane.getSelectedFiles());
				} else {
					getFileChooser().setSelectedFile(recentlyUsedPane.getSelectedFile());
				}

				if (FilesListPane.DOUBLE_CLICK_ID == e.getID()) {
					// On double click on a recent file, close the file chooser.
					getFileChooser().approveSelection();
				}
			}
		});

		rightPanel.add(recentlyUsedPane, RECENTLY_USED_PANEL);

		// add listener on ENTER pressed for select/browse
		recentlyUsedPane.addActionListener(new SelectPathAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (FilesListPane.ENTER_PRESSED_ID == e.getID()) {
					super.actionPerformed(e);
				}
			}

			@Override
			protected File getSelectedPath() {
				return recentlyUsedPane.getSelectedFile();
			}

		});

		/**
		 * Add the content
		 */		
		new RecentlyUsedFileWorker(this).execute();
	}

	/**
	 * Listeners for enable/disable the buttons "Add" and "Remove" below the
	 * LocationsPane.
	 */
	private void installListenersForBookmarksButtons() {
		// Behavior for the "Remove" button
		locationsPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Path path = locationsPane.getCurrentPath();
				// Enable only if a bookmark is selected.
				removeBookmarkButton.setEnabled(path instanceof GtkBookmark);
			}
		});
	}

	@Override
	public String getApproveButtonText(JFileChooser fc) {
		String buttonText = fc.getApproveButtonText();
		if (buttonText != null) {
			return buttonText;
		} else if (fc.getDialogType() == JFileChooser.OPEN_DIALOG) {
			return openButtonText;
		} else if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
			return saveButtonText;
		} else {
			return null;
		}
	}


	protected JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
		}
		return buttonPanel;
	}

	@Override
	protected void installStrings(JFileChooser fc) {
		super.installStrings(fc);

		Locale l = fc.getLocale();

		fileNameLabelMnemonic = UIManager.getInt("FileChooser.fileNameLabelMnemonic");
		fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", l);
		filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText", l);
		newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", l);
		newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", l);
	}

	/**
	 * Creates a selection listener for the list of files and directories.
	 * 
	 * @param fc
	 *            a <code>JFileChooser</code>
	 * @return a <code>ListSelectionListener</code>
	 */
	@Override
	public ListSelectionListener createListSelectionListener(JFileChooser fc) {
		return super.createListSelectionListener(fc);
	}

	// Obsolete class, not used in this version.
	protected class SingleClickListener extends MouseAdapter {
		public SingleClickListener(JList list) {
		}
	}

	@Override
	public void uninstallUI(JComponent c) {

		// Remove PropertyChangeListener
		PropertyChangeListener[] listeners = c.getPropertyChangeListeners();
		for (PropertyChangeListener listener : listeners) {
			c.removePropertyChangeListener(listener);
		}

		cancelButton.removeActionListener(getCancelSelectionAction());
		approveButton.removeActionListener(getApproveSelectionAction());
		fileNameTextField.removeActionListener(getApproveSelectionAction());

		if (fileBrowserPane != null) {
			fileBrowserPane.uninstallUI();
			fileBrowserPane = null;
		}

		super.uninstallUI(c);
	}

	/**
	 * Returns the preferred size of the specified <code>JFileChooser</code>.
	 * The preferred size is at least as large, in both height and width, as the
	 * preferred size recommended by the file chooser's layout manager.
	 * 
	 * @param c
	 *            a <code>JFileChooser</code>
	 * @return a <code>Dimension</code> specifying the preferred width and
	 *         height of the file chooser
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Rectangle bound = GtkFileChooserSettings.get().getBound();
		if (bound != null) {
			return new Dimension(bound.width, bound.height);
		}

		int prefWidth = PREF_SIZE.width;
		Dimension d = c.getLayout().preferredLayoutSize(c);
		if (d != null) {
			return new Dimension(d.width < prefWidth ? prefWidth : d.width,
					d.height < PREF_SIZE.height ? PREF_SIZE.height : d.height);
		} else {
			return new Dimension(prefWidth, PREF_SIZE.height);
		}
	}

	/**
	 * Returns the minimum size of the <code>JFileChooser</code>.
	 * 
	 * @param c
	 *            a <code>JFileChooser</code>
	 * @return a <code>Dimension</code> specifying the minimum width and height
	 *         of the file chooser
	 */
	@Override
	public Dimension getMinimumSize(JComponent c) {
		return MIN_SIZE;
	}

	/**
	 * Returns the maximum size of the <code>JFileChooser</code>.
	 * 
	 * @param c
	 *            a <code>JFileChooser</code>
	 * @return a <code>Dimension</code> specifying the maximum width and height
	 *         of the file chooser
	 */
	@Override
	public Dimension getMaximumSize(JComponent c) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	private String fileNameString(File file) {
		if (file == null) {
			return null;
		} else {
			JFileChooser fc = getFileChooser();
			if ((fc.isDirectorySelectionEnabled() && !fc.isFileSelectionEnabled())
					|| (fc.isDirectorySelectionEnabled() && fc.isFileSelectionEnabled() && fc
							.getFileSystemView().isFileSystemRoot(file))) {
				return file.getPath();
			} else {
				return file.getName();
			}
		}
	}

	private String fileNameString(File[] files) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; files != null && i < files.length; i++) {
			if (i > 0) {
				buf.append(" ");
			}
			if (files.length > 1) {
				buf.append("\"");
			}
			buf.append(fileNameString(files[i]));
			if (files.length > 1) {
				buf.append("\"");
			}
		}
		return buf.toString();
	}

	/* The following methods are used by the PropertyChange Listener */

	private void doSelectedFileChanged(File file) {
		JFileChooser fc = getFileChooser();
		if (file != null
				&& ((fc.isFileSelectionEnabled() && !file.isDirectory()) || (file.isDirectory() && fc
						.isDirectorySelectionEnabled()))) {

			setFileName(fileNameString(file));
		}

		if (file != null && !file.equals(fc.getSelectedFile())){
			fc.setSelectedFile(file);
		}

		// Enable/disable the "Add to Bookamark" button
		addBookmarkButton.setEnabled(file != null && file.isDirectory());
	}

	private void doSelectedFilesChanged(File[] files) {
		JFileChooser fc = getFileChooser();

		if (files != null) {
			List<File> fileList = new ArrayList<File>();
			for (File file : files) {
				// If directory, don't add to the selection when isDirectorySelectionEnabled is false
				if (file.isDirectory()) {
					if (fc.isDirectorySelectionEnabled()) {
						fileList.add(file);
					}					
				} else {
					fileList.add(file);
				}
			}

			setFileName(fileNameString(fileList.toArray(new File[fileList.size()])));
		}

		// Update the property in the JFileChooser if not yet happened
		if (files != null && !files.equals(fc.getSelectedFiles())){
			fc.setSelectedFiles(files);
		}

		// Enable/disable the "Add to Bookamark" button
		if (files != null) {
			boolean enable = true;
			for (File file : files) {
				if (!file.isDirectory()) {
					enable = false;
					break;
				}
			}

			addBookmarkButton.setEnabled(enable);
		}
	}

	private void doDirectoryChanged(File dir) {
		if (getFileChooser().getCurrentDirectory().equals(dir)) {
			// to avoid repeated invocations on the same directory.
			return;
		}

		JFileChooser fc = getFileChooser();
		FileSystemView fsv = fc.getFileSystemView();

		if (dir != null) {
			getFileChooser().setCurrentDirectory(dir);
			comboButtons.setCurrentDirectory(dir);
			updateFileNameField();
			fileBrowserPane.setCurrentDir(dir);

			if (fc.isDirectorySelectionEnabled() && !fc.isFileSelectionEnabled()) {
				if (fsv.isFileSystem(dir)) {
					setFileName(dir.getPath());
				} else {
					setFileName(null);
				}
			}
		}
	}

	private void doFilterChanged(javax.swing.filechooser.FileFilter filter) {
		fileBrowserPane.setCurrentFilter(filter);
		pathAutoCompletion.setCurrentFilter(filter);
		selectFilterInCombo();

		// Update the recent used file panel
		if (recentlyUsedPane != null) {
			new RecentlyUsedFileWorker(this).execute();	
		}		

		// Set the new filter in the Search panel
		if (searchPanel != null){
			searchPanel.setFileFilter(new FileFilterWrapper(filter));
		}		
	}

	private void doFileSelectionModeChanged(Integer fileSelectionMode) {
		JFileChooser fc = getFileChooser();
		File currentDirectory = fc.getCurrentDirectory();
		if (currentDirectory != null && fc.isDirectorySelectionEnabled()
				&& !fc.isFileSelectionEnabled()
				&& fc.getFileSystemView().isFileSystem(currentDirectory)) {

			setFileName(currentDirectory.getPath());
		} else {
			setFileName(null);
		}

		pathAutoCompletion.setFileSelectionMode(fileSelectionMode);
		fileBrowserPane.setFileSelectionMode(fileSelectionMode);
	}

	private void doAccessoryChanged(PropertyChangeEvent e) {
		if (getAccessoryPanel() != null) {
			if (e.getOldValue() != null) {
				getAccessoryPanel().remove((JComponent) e.getOldValue());
			}
			JComponent accessory = (JComponent) e.getNewValue();
			if (accessory != null) {
				getAccessoryPanel().add(accessory, BorderLayout.CENTER);
			}
		}
	}

	private void doApproveButtonTextChanged(PropertyChangeEvent e) {
		JFileChooser chooser = getFileChooser();
		approveButton.setText(getApproveButtonText(chooser));
		approveButton.setToolTipText(getApproveButtonToolTipText(chooser));
	}

	private void doDialogTypeChanged(PropertyChangeEvent e) {
		JFileChooser chooser = getFileChooser();
		approveButton.setText(getApproveButtonText(chooser));
		approveButton.setToolTipText(getApproveButtonToolTipText(chooser));
	}

	@Override
	public void ensureFileIsVisible(JFileChooser fc, File f) {
		// Not implemented
	}

	@Override
	public void rescanCurrentDirectory(JFileChooser fc) {
		fileBrowserPane.rescanCurrentDirectory();
	}

	@Override
	public String getFileName() {
		if (fileNameTextField != null) {
			return fileNameTextField.getText();
		} else {
			return null;
		}
	}

	@Override
	public void setFileName(String filename) {
		if (fileNameTextField != null) {
			fileNameTextField.setText(filename);
		}
	}

	@Override
	public String getDirectoryName() {
		return getFileChooser().getCurrentDirectory().getName();
	}

	@Override
	public void setDirectoryName(String dirname) {
		getFileChooser().setCurrentDirectory(new File(dirname));
	}

	final static int space = 10;

	class IndentIcon implements Icon {

		Icon icon = null;
		int depth = 0;

		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (c.getComponentOrientation().isLeftToRight()) {
				icon.paintIcon(c, g, x + depth * space, y);
			} else {
				icon.paintIcon(c, g, x, y);
			}
		}

		public int getIconWidth() {
			return icon.getIconWidth() + depth * space;
		}

		public int getIconHeight() {
			return icon.getIconHeight();
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		JFileChooser fc = getFileChooser();
		File f = fc.getSelectedFile();
		if (!e.getValueIsAdjusting() && f != null && !getFileChooser().isTraversable(f)) {
			setFileName(fileNameString(f));
		}
	}

	@Override
	protected JButton getApproveButton(JFileChooser fc) {
		return approveButton;
	}

	/**
	 * <code>ButtonAreaLayout</code> behaves in a similar manner to
	 * <code>FlowLayout</code>. It lays out all components from left to right,
	 * flushed right. The widths of all components will be set to the largest
	 * preferred size width.
	 */
	private static class ButtonAreaLayout implements LayoutManager {
		private int hGap = 5;
		private int topMargin = 17;

		public void addLayoutComponent(String string, Component comp) {
		}

		public void layoutContainer(Container container) {
			Component[] children = container.getComponents();

			if (children != null && children.length > 0) {
				int numChildren = children.length;
				Dimension[] sizes = new Dimension[numChildren];
				Insets insets = container.getInsets();
				int yLocation = insets.top + topMargin;
				int maxWidth = 0;

				for (int counter = 0; counter < numChildren; counter++) {
					sizes[counter] = children[counter].getPreferredSize();
					maxWidth = Math.max(maxWidth, sizes[counter].width);
				}
				int xLocation, xOffset;
				if (container.getComponentOrientation().isLeftToRight()) {
					xLocation = container.getSize().width - insets.left - maxWidth;
					xOffset = hGap + maxWidth;
				} else {
					xLocation = insets.left;
					xOffset = -(hGap + maxWidth);
				}
				for (int counter = numChildren - 1; counter >= 0; counter--) {
					children[counter].setBounds(xLocation, yLocation, maxWidth,
							sizes[counter].height);
					xLocation -= xOffset;
				}
			}
		}

		public Dimension minimumLayoutSize(Container c) {
			if (c != null) {
				Component[] children = c.getComponents();

				if (children != null && children.length > 0) {
					int numChildren = children.length;
					int height = 0;
					Insets cInsets = c.getInsets();
					int extraHeight = topMargin + cInsets.top + cInsets.bottom;
					int extraWidth = cInsets.left + cInsets.right;
					int maxWidth = 0;

					for (int counter = 0; counter < numChildren; counter++) {
						Dimension aSize = children[counter].getPreferredSize();
						height = Math.max(height, aSize.height);
						maxWidth = Math.max(maxWidth, aSize.width);
					}
					return new Dimension(extraWidth + numChildren * maxWidth
							+ (numChildren - 1) * hGap, extraHeight + height);
				}
			}
			return new Dimension(0, 0);
		}

		public Dimension preferredLayoutSize(Container c) {
			return minimumLayoutSize(c);
		}

		public void removeLayoutComponent(Component c) {
		}
	}

	/**
	 * Action to select a file or select/browse a directory (according to the
	 * FileSelectionMode).
	 */
	private abstract class SelectPathAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			File path = getSelectedPath();

			if (path == null) {
				return;
			}

			if (path.isDirectory()) {
				doDirectoryChanged(path);
				if (getFileChooser().getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY) {
					approveSelection(path);
				}
			} else {
				approveSelection(path);
			}
		}

		protected abstract File getSelectedPath();
	}

	private void approveSelection(File path) {
		getFileChooser().setSelectedFile(path);
		getFileChooser().approveSelection();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();
		Object value = e.getNewValue();

		Log.debug("Property: ", property, " = ", value, " ; source :", e.getSource().getClass());

		if (DIRECTORY_CHANGED_PROPERTY.equals(property)) {
			doDirectoryChanged((File) value);
		} else if (SELECTED_FILE_CHANGED_PROPERTY.equals(property)) {
			doSelectedFileChanged((File)value);
		} else if (SELECTED_FILES_CHANGED_PROPERTY.equals(property)) {
			doSelectedFilesChanged((File[])value);
		} else if (CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY.equals(property)) {
			doChoosableFileFilterChanged((FileFilter[])value);
		} else if (CURRENT_PANEL_CHANGED.equals(property)) {
			doCurrentPanelChanged((Integer)value);
		} else if (FILE_FILTER_CHANGED_PROPERTY.equals(property)) {
			doFilterChanged((javax.swing.filechooser.FileFilter)value);
		} else if (FILE_SELECTION_MODE_CHANGED_PROPERTY.equals(property)) {
			doFileSelectionModeChanged((Integer) value);			
		} else if (FILE_HIDING_CHANGED_PROPERTY.equals(property)) {
			doFileHidingChanged((Boolean) value);			
		} else if (MULTI_SELECTION_ENABLED_CHANGED_PROPERTY.equals(property)) {
			doMultiSelectionEnabledChanged((Boolean)value);
		} else if (ACCESSORY_CHANGED_PROPERTY.equals(property)) {
			doAccessoryChanged(e);
		} else if (APPROVE_BUTTON_TEXT_CHANGED_PROPERTY.equals(property)) {
			doApproveButtonTextChanged(e);
		} else if (APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY.equals(property)) {
			doApproveButtonTextChanged(e);
		} else if (DIALOG_TYPE_CHANGED_PROPERTY.equals(property)) {
			doDialogTypeChanged(e);
		} else if ("JFileChooserDialogIsClosingProperty".equals(property)) {
			onClosing();
		} else if(CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY.equals(property)){
			getButtonPanel().setVisible((Boolean) value);
		} else if (COMPONENT_ORIENTATION_PROPERTY.equals(property)) {
			ComponentOrientation o = (ComponentOrientation) e.getNewValue();
			JFileChooser cc = (JFileChooser) e.getSource();
			if (o != (ComponentOrientation) e.getOldValue()) {
				cc.applyComponentOrientation(o);
			}
		} else if (ANCESTOR_PROPERTY.equals(property)) {
			if (e.getOldValue() == null && e.getNewValue() != null) {
				// Ancestor was added, set initial focus
				fileNameTextField.selectAll();
				fileNameTextField.requestFocus();
			}
		}

	}



	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		Log.debug("GtkFileChooserUI: Action: ", e.getActionCommand());
		if (APPROVE_SELECTION.equals(cmd)) {
			approveSelection(fileBrowserPane.getSelectedFile());
		} else if(ACTION_ADD_BOOKMARK.equals(cmd)){
			addToBookmarks();
		} else if(ACTION_SELECTED_BOOKMARK.equals(cmd)) {	
			File location = new File(locationsPane.getCurrentPath().getLocation());
			doDirectoryChanged(location);
		} else if (LOCATION_POPUP.equals(cmd)) {
			showPositionButton.doClick();
		} else if (UP_FOLDER.equals(cmd)) {
			comboButtons.upFolder();
		} else if (DOWN_FOLDER.equals(cmd)) {
			comboButtons.downFolder();
		} else if (HOME_FOLDER.equals(cmd)) {
			doDirectoryChanged(new File(System.getProperty("user.home")));
		} else if (DESKTOP_FOLDER.equals(cmd)) {
			doDirectoryChanged(FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP));
		} else if (QUICK_BOOKMARK.equals(cmd)) {
			int id = e.getID();
			locationsPane.selectBookmark(id);		
			File location = new File(locationsPane.getCurrentPath().getLocation());
			doDirectoryChanged(location);
		}
	}

	private void doMultiSelectionEnabledChanged(Boolean multiSelectionEnabled) {
		int selectionMode = multiSelectionEnabled ? MULTIPLE_INTERVAL_SELECTION	: SINGLE_SELECTION;
		if (getRecentlyUsedPane() != null) {
			getRecentlyUsedPane().setSelectionMode(selectionMode);
		}
		if (searchFilesPane != null) {
			searchFilesPane.setSelectionMode(selectionMode);
		}

		fileBrowserPane.setIsMultiSelectionEnabled(multiSelectionEnabled);
	}

	private void doFileHidingChanged(Boolean hide) {
		Boolean showHidden = !hide;
		GtkFileChooserSettings.get().setShowHidden(showHidden);
		fileBrowserPane.setShowHidden(showHidden);
	}



	private void addToBookmarks() {
		File[] paths = fileBrowserPane.getSelectedFiles();
		if (paths == null) {
			locationsPane.addBookmark(fileBrowserPane.getSelectedFile());
		} else {
			for (File path : paths) {
				locationsPane.addBookmark(path);
			}	
		}
	}

	private void doCurrentPanelChanged(int id) {
		currentPanelId = id;

		// Top panel
		CardLayout top = (CardLayout) topPanel.getLayout();

		// Right panel
		CardLayout right = (CardLayout) rightPanel.getLayout();

		switch (id) {
		case FILEBROWSER_PANEL_ID:
			Log.debug("   >>> Panel: ", FILEBROWSER_PANEL);
			topPanel.setVisible(true);
			filenamePanel.setVisible(showPositionButton.isSelected());
			top.show(topPanel, TOP_PATHBAR_PANEL);
			right.show(rightPanel, FILEBROWSER_PANEL);
			break;

		case RECENTLY_USED_PANEL_ID:
			Log.debug("   >>> Panel: ", RECENTLY_USED_PANEL);
			// show recent used files panel
			if (recentlyUsedPane == null) {
				createRecentlyUsedPane();
			}
			topPanel.setVisible(false);
			right.show(rightPanel, RECENTLY_USED_PANEL);
			break;

		case SEARCH_PANEL_ID:
			Log.debug("   >>> Panel: ", SEARCH_PANEL);
			if (searchFilesPane == null) {
				createSearchPane();
			}			
			filenamePanel.setVisible(false);
			topPanel.setVisible(true);
			top.show(topPanel, TOP_SEARCH_PANEL);
			right.show(rightPanel, SEARCH_PANEL);
			searchPanel.setFileFilter(new FileFilterWrapper(getFileChooser().getFileFilter()));
			searchPanel.requestFocusInWindow();
			break;
		}
	}
}
