package eu.kostia.gtkjfilechooser.ui;



import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;

import java.awt.BorderLayout;
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
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Serializable;
import java.util.Locale;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicFileChooserUI;

import sun.swing.FilePane;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.Path;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings.Mode;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;
import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

/**
 * GtkFileChooserUI basen on the Metal L&F implementation of a FileChooser.
 * 
 * @version 1.95 10/02/08
 * @author Costantino Cerbo, Jeff Dinkins
 */
public class GtkFileChooserUI extends BasicFileChooserUI implements Serializable {

	private static final long serialVersionUID = 10L;

	public GtkFileChooserUI(JFileChooser chooser) {
		super(chooser);
		chooser.setFileHidingEnabled(!GtkFileChooserSettings.get().getShowHidden());
	}

	private GtkPathBar comboButtons;

	// private FilterComboBoxChangeListener filterComboBoxModel;

	private JTextField fileNameTextField;

	/**
	 * The panel on the left with locations and bookmarks
	 */
	private GtkLocationsPane locationsPane;

	private GtkFilePane fileBrowserPane;

	private boolean useShellFolder;

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
	public void installUI(JComponent c) {
		super.installUI(c);
	}

	@Override
	public void uninstallComponents(JFileChooser fc) {
		fc.removeAll();
		buttonPanel = null;
	}

	class MyGTKFileChooserUIAccessor implements GtkFilePane.FileChooserUIAccessor {
		public JFileChooser getFileChooser() {
			return GtkFileChooserUI.this.getFileChooser();
		}

		public BasicDirectoryModel getModel() {
			return GtkFileChooserUI.this.getModel();
		}

		public JPanel createList() {
			return GtkFileChooserUI.this.createList(getFileChooser());
		}

		public JPanel createDetailsView() {
			return GtkFileChooserUI.this.createDetailsView(getFileChooser());
		}

		public boolean isDirectorySelected() {
			return GtkFileChooserUI.this.isDirectorySelected();
		}

		public File getDirectory() {
			return GtkFileChooserUI.this.getDirectory();
		}

		public Action getChangeToParentDirectoryAction() {
			return GtkFileChooserUI.this.getChangeToParentDirectoryAction();
		}

		public Action getApproveSelectionAction() {
			return GtkFileChooserUI.this.getApproveSelectionAction();
		}

		public Action getNewFolderAction() {
			return GtkFileChooserUI.this.getNewFolderAction();
		}

		public MouseListener createDoubleClickListener(JList list) {
			return GtkFileChooserUI.this.createDoubleClickListener(getFileChooser(),
					list);
		}

		public ListSelectionListener createListSelectionListener() {
			return GtkFileChooserUI.this.createListSelectionListener(getFileChooser());
		}

		public boolean usesShellFolder() {
			return useShellFolder;
		}

		public GtkLocationsPane getLocationsPane(){
			return GtkFileChooserUI.this.locationsPane;
		}
	}

	private JPanel createFilenamePanel(JFileChooser fc) {
		// FileName label and textfield
		JPanel fileNamePanel = new JPanel();
		fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.LINE_AXIS));

		JLabel fileNameLabel = new JLabel(fileNameLabelText);
		fileNameLabel.setDisplayedMnemonic(fileNameLabelMnemonic);
		fileNamePanel.add(fileNameLabel);

		fileNameTextField = new JTextField(35) {
			private static final long serialVersionUID = GtkFileChooserUI.serialVersionUID;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(Short.MAX_VALUE, super.getPreferredSize().height);
			}
		};
		fileNamePanel.add(fileNameTextField);
		fileNameLabel.setLabelFor(fileNameTextField);
		fileNameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!getFileChooser().isMultiSelectionEnabled()) {
					fileBrowserPane.clearSelection();
				}
			}
		});
		if (fc.isMultiSelectionEnabled()) {
			setFileName(fileNameString(fc.getSelectedFiles()));
		} else {
			setFileName(fileNameString(fc.getSelectedFile()));
		}

		return fileNamePanel;
	}

	@Override
	public void installComponents(final JFileChooser fc) {
		fc.setBorder(new EmptyBorder(12, 12, 11, 11));
		fc.setLayout(new BorderLayout(0, 11));

		fileBrowserPane = new GtkFilePane(new MyGTKFileChooserUIAccessor());
		fc.addPropertyChangeListener(fileBrowserPane);

		updateUseShellFolder();

		// ********************************* //
		// **** Construct the top panel **** //
		// ********************************* //

		JPanel topPanel1 = new JPanel(new BorderLayout());

		final JToggleButton showPositionButton = new JToggleButton(GtkStockIcon.get("gtk-edit", Size.GTK_ICON_SIZE_BUTTON));
		showPositionButton.setSelected(GtkFileChooserSettings.get().getLocationMode() == Mode.FILENAME_ENTRY);
		showPositionButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Mode mode = showPositionButton.isSelected() ? Mode.FILENAME_ENTRY : Mode.PATH_BAR;
				GtkFileChooserSettings.get().setLocationMode(mode);
			}			
		});


		// CurrentDir Combo Buttons
		File currentDirectory = fc.getCurrentDirectory();
		if (currentDirectory == null){
			currentDirectory = new File(System.getProperty("user.home"));
		}
		comboButtons = new GtkPathBar(currentDirectory);
		comboButtons.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setCurrentDirectory(comboButtons.getCurrentDirectory());			
			}

		});


		topPanel1.add(
				createPanel(
						new PanelElement(createPanel(showPositionButton), BorderLayout.LINE_START),
						new PanelElement(comboButtons, BorderLayout.CENTER)
				),
				BorderLayout.CENTER);


		if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
			// New Directory Button
			JButton newDirButton = new JButton(fileBrowserPane.getNewFolderAction());
			newDirButton.setText("New Directory"); // TODO I18N
			newDirButton.setToolTipText(newFolderToolTipText);
			newDirButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY,
					newFolderAccessibleName);
			newDirButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			newDirButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
			newDirButton.setMargin(shrinkwrap);

			//TODO add to panel in the right position
			//			topPanel1.add(JPanelUtil.createPanel(new FlowLayout(FlowLayout.RIGHT), newDirButton), BorderLayout.LINE_END);
		}


		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		topPanel.add(topPanel1);
		final JPanel filenamePanel = createFilenamePanel(fc);
		showPositionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton) e.getSource();
				filenamePanel.setVisible(btn.isSelected());							
			}
		});
		topPanel.add(filenamePanel);
		filenamePanel.setVisible(showPositionButton.isSelected());

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

		cancelButton = new JButton(cancelButtonText);
		cancelButton.setIcon(GtkStockIcon.get("gtk-cancel", Size.GTK_ICON_SIZE_BUTTON));
		cancelButton.setToolTipText(cancelButtonToolTipText);
		cancelButton.addActionListener(getCancelSelectionAction());
		getButtonPanel().add(cancelButton);

		approveButton = new JButton(getApproveButtonText(fc));
		if (fc.getDialogType() == JFileChooser.OPEN_DIALOG) {
			approveButton
			.setIcon(GtkStockIcon.get("gtk-open", Size.GTK_ICON_SIZE_BUTTON));
		} else if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
			approveButton
			.setIcon(GtkStockIcon.get("gtk-save", Size.GTK_ICON_SIZE_BUTTON));
		}

		approveButton.addActionListener(getApproveSelectionAction());
		approveButton.setToolTipText(getApproveButtonToolTipText(fc));
		getButtonPanel().add(approveButton);

		if (fc.getControlButtonsAreShown()) {
			fc.add(getButtonPanel(), BorderLayout.PAGE_END);
		}
	}


	private void addFileBrowserPane(final JFileChooser fc) {
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setContinuousLayout(true);

		// Left Panel (Bookmarks)
		addBookmarkButton = new JButton("Add"); // TODO I18N
		addBookmarkButton.setIcon(GtkStockIcon.get("gtk-add", Size.GTK_ICON_SIZE_BUTTON));
		addBookmarkButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Not yet implemented!");

			}			
		});

		removeBookmarkButton = new JButton("Remove"); // TODO I18N
		removeBookmarkButton.setIcon(GtkStockIcon.get("gtk-remove",
				Size.GTK_ICON_SIZE_BUTTON));
		removeBookmarkButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Not yet implemented!");

			}			
		});

		JPanel buttonPanel = JPanelUtil.createPanel(new GridLayout(1,2), addBookmarkButton,
				removeBookmarkButton);


		locationsPane = new GtkLocationsPane();
		locationsPane.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Path bookmark = ((GtkLocationsPane) e.getSource()).getCurrentPath();
				fc.setCurrentDirectory(new File(bookmark.getLocation()));
			}
		});

		mainPanel.add(JPanelUtil.createPanel(
				new PanelElement(locationsPane, BorderLayout.CENTER),
				new PanelElement(buttonPanel, BorderLayout.PAGE_END)
		));

		// Right Panel (file browser)
		fileBrowserPane.setPreferredSize(LIST_PREF_SIZE);
		fileBrowserPane.setViewType(FilePane.VIEWTYPE_DETAILS);

		// Filetype combobox
		// TODO add PropertyChangeListener to filterComboBox
		// fc.addPropertyChangeListener(new FilterComboBoxChangeListener());
		JComboBox filterComboBox = new JComboBox();
		fillFileFilterComboBox(filterComboBox);
		filterComboBox.putClientProperty(
				AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, filesOfTypeLabelText);	

		filterComboBox.setPreferredSize(new Dimension(150, (int)removeBookmarkButton.getPreferredSize().getHeight()));
		mainPanel.add(JPanelUtil.createPanel(
				new PanelElement(fileBrowserPane, BorderLayout.CENTER),
				new PanelElement(JPanelUtil.createPanel(new GridLayout(1,3), new JLabel(), new JLabel(), filterComboBox), BorderLayout.PAGE_END)
		));

		//ad to the file chooser
		fc.add(mainPanel, BorderLayout.CENTER);
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

	private void fillFileFilterComboBox(JComboBox comboBox) {
		FileFilter[] filters = getFileChooser().getChoosableFileFilters();
		for (final FileFilter filter : filters) {
			comboBox.addItem(new FileFilter() {

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
			});
		}

		if (comboBox.getItemCount() == 0) {
			// Add Default AcceptAll file filter
			comboBox.addItem(new FileFilter() {

				@Override
				public String getDescription() {
					return UIManager.getString("FileChooser.acceptAllFileFilterText");
				}

				@Override
				public boolean accept(File f) {
					return true;
				}

				@Override
				public String toString() {
					return getDescription();
				}
			});
		}
	}

	private void updateUseShellFolder() {
		// Decide whether to use the ShellFolder class to populate shortcut
		// panel and combobox.
		JFileChooser fc = getFileChooser();
		Boolean prop = (Boolean) fc.getClientProperty("FileChooser.useShellFolder");
		if (prop != null) {
			useShellFolder = prop.booleanValue();
		} else {
			useShellFolder = fc.getFileSystemView().equals(
					FileSystemView.getFileSystemView());
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

	@Override
	protected void installListeners(JFileChooser fc) {
		super.installListeners(fc);
		ActionMap actionMap = new ActionMapUIResource();
		FilePane.addActionsToMap(actionMap, fileBrowserPane.getActions());
		SwingUtilities.replaceUIActionMap(fc, actionMap);

		fc.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentMoved(ComponentEvent e) {
				persistBoundaries(e.getComponent().getBounds());
			}

			@Override
			public void componentResized(ComponentEvent e) {
				persistBoundaries(e.getComponent().getBounds());
			}

			private void persistBoundaries(Rectangle bound){
				GtkFileChooserSettings.get().setBound(bound);
			}
		});
	}

	protected JPanel createList(JFileChooser fc) {
		return fileBrowserPane.createList();
	}

	protected JPanel createDetailsView(JFileChooser fc) {
		return fileBrowserPane.createDetailsView();
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
		if (bound != null){
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

	private void doSelectedFileChanged(PropertyChangeEvent e) {
		File f = (File) e.getNewValue();
		JFileChooser fc = getFileChooser();
		if (f != null
				&& ((fc.isFileSelectionEnabled() && !f.isDirectory()) || (f.isDirectory() && fc
						.isDirectorySelectionEnabled()))) {

			setFileName(fileNameString(f));
		}
	}

	private void doSelectedFilesChanged(PropertyChangeEvent e) {
		File[] files = (File[]) e.getNewValue();
		JFileChooser fc = getFileChooser();
		if (files != null
				&& files.length > 0
				&& (files.length > 1 || fc.isDirectorySelectionEnabled() || !files[0]
				                                                                   .isDirectory())) {
			setFileName(fileNameString(files));
		}
	}

	private void doDirectoryChanged(PropertyChangeEvent e) {
		JFileChooser fc = getFileChooser();
		FileSystemView fsv = fc.getFileSystemView();

		clearIconCache();
		File currentDirectory = fc.getCurrentDirectory();
		if (currentDirectory != null) {
			comboButtons.setCurrentDirectory(currentDirectory);

			if (fc.isDirectorySelectionEnabled() && !fc.isFileSelectionEnabled()) {
				if (fsv.isFileSystem(currentDirectory)) {
					setFileName(currentDirectory.getPath());
				} else {
					setFileName(null);
				}
			}
		}
	}

	private void doFilterChanged(PropertyChangeEvent e) {
		clearIconCache();
	}

	private void doFileSelectionModeChanged(PropertyChangeEvent e) {
		clearIconCache();

		JFileChooser fc = getFileChooser();
		File currentDirectory = fc.getCurrentDirectory();
		if (currentDirectory != null && fc.isDirectorySelectionEnabled()
				&& !fc.isFileSelectionEnabled()
				&& fc.getFileSystemView().isFileSystem(currentDirectory)) {

			setFileName(currentDirectory.getPath());
		} else {
			setFileName(null);
		}
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

	private void doApproveButtonMnemonicChanged(PropertyChangeEvent e) {
		// Note: Metal does not use mnemonics for approve and cancel
	}

	/*
	 * Listen for filechooser property changes, such as the selected file
	 * changing, or the type of the dialog changing.
	 */
	@Override
	public PropertyChangeListener createPropertyChangeListener(JFileChooser fc) {
		return new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String s = e.getPropertyName();
				if (s.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
					doSelectedFileChanged(e);
				} else if (s.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
					doSelectedFilesChanged(e);
				} else if (s.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
					doDirectoryChanged(e);
				} else if (s.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
					doFilterChanged(e);
				} else if (s.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
					doFileSelectionModeChanged(e);
				} else if (s.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
					doAccessoryChanged(e);
				} else if (s.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY)
						|| s
						.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) {
					doApproveButtonTextChanged(e);
				} else if (s.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
					doDialogTypeChanged(e);
				} else if (s
						.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY)) {
					doApproveButtonMnemonicChanged(e);
				} else if (s.equals("componentOrientation")) {
					ComponentOrientation o = (ComponentOrientation) e.getNewValue();
					JFileChooser cc = (JFileChooser) e.getSource();
					if (o != (ComponentOrientation) e.getOldValue()) {
						cc.applyComponentOrientation(o);
					}
				} else if (s == "FileChooser.useShellFolder") {
					updateUseShellFolder();
					doDirectoryChanged(e);
				} else if (s.equals("ancestor")) {
					if (e.getOldValue() == null && e.getNewValue() != null) {
						// Ancestor was added, set initial focus
						fileNameTextField.selectAll();
						fileNameTextField.requestFocus();
					}
				}
			}
		};
	}

	@Override
	public void ensureFileIsVisible(JFileChooser fc, File f) {
		fileBrowserPane.ensureFileIsVisible(fc, f);
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
		// PENDING(jeff) - get the name from the directory combobox
		return null;
	}

	@Override
	public void setDirectoryName(String dirname) {
		// PENDING(jeff) - set the name in the directory combobox
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

}
