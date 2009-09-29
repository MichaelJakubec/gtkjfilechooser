package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import eu.kostia.gtkjfilechooser.BookmarkManager;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.Path;
import eu.kostia.gtkjfilechooser.BookmarkManager.GtkBookmark;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class GtkLocationsPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private final BookmarkManager manager;
	private JTable bookmarksTable;
	private Path path;

	private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	public GtkLocationsPane() {
		if (UIManager.getLookAndFeel().getName().indexOf("GTK") == -1) {
			throw new IllegalStateException(
					"GtkLocationsPane requires the GTK look and feel. Current LAF: "
					+ UIManager.getLookAndFeel());
		}

		this.manager = new BookmarkManager();

		setLayout(new BorderLayout());

		bookmarksTable = new JTable();
		bookmarksTable.setRowHeight(22);
		bookmarksTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		bookmarksTable.setModel(new GtkBookmarksTableModel(manager.getAll()));

		bookmarksTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		bookmarksTable.setBackground(UIManager.getColor("TextPane.background"));
		bookmarksTable.setShowGrid(false);
		bookmarksTable.setDefaultRenderer(Object.class,
				new GtkBookmarksTableCellRenderer());
		GtkBookmarksTableCellEditor defaultCellEditor = new GtkBookmarksTableCellEditor(
				bookmarksTable);

		// Necessary to avoid the exception: java.lang.ClassCastException:
		// eu.kostia.gtkjfilechooser.ui.GtkLocationsPane$2 cannot be cast to
		// eu.kostia.gtkjfilechooser.ui.GtkLocationsPane
		final GtkLocationsPane thisPane = this;

		defaultCellEditor.addCellEditorListener(new CellEditorListener() {

			@Override
			public void editingStopped(ChangeEvent e) {
				TableCellEditor editor = (TableCellEditor) e.getSource();
				String newName = (String) editor.getCellEditorValue();
				String oldName = thisPane.getCurrentPath().getName();

				manager.rename(oldName, newName);
				refreshBookmarks();
			}

			@Override
			public void editingCanceled(ChangeEvent e) {
				// do nothing
			}
		});
		bookmarksTable.setDefaultEditor(Object.class, defaultCellEditor);

		bookmarksTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				JTable table = (JTable) evt.getSource();
				Point p = evt.getPoint();
				int rowIndex = table.rowAtPoint(p);
				table.setRowSelectionInterval(rowIndex, rowIndex);
				Object valueAt = table.getModel().getValueAt(rowIndex, 0);
				path = (Path) valueAt;
				for (ActionListener listener : actionListeners) {
					ActionEvent actionEvent = new ActionEvent(thisPane, 1,
					"location_selected");
					listener.actionPerformed(actionEvent);
				}
				if (SwingUtilities.isRightMouseButton(evt)) {
					onRightMouseButtonClick(evt, path);
				}
			}
		});

		// JLabel header = new
		// JLabel(bookmarksTable.getModel().getColumnName(0));
		// header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		// add(header, BorderLayout.PAGE_START);
		JScrollPane scrollpane = new JScrollPane(bookmarksTable);
		scrollpane.setPreferredSize(new Dimension(200, 300));
		add(scrollpane, BorderLayout.CENTER);
	}

	public void addActionListener(ActionListener l) {
		actionListeners.add(l);
	}

	public void removeActionListener(ActionListener l) {
		actionListeners.remove(l);
	}

	public void refreshBookmarks() {
		Dimension size = bookmarksTable.getSize();
		bookmarksTable.setModel(new GtkBookmarksTableModel(manager.getAll()));

		// Workaround to maintain the same size before the refreshing
		bookmarksTable.setPreferredSize(size);
	}

	/**
	 * Returns the current selected bookmarks.
	 * 
	 * @return the current selected bookmarks
	 */
	public Path getCurrentPath() {
		return path;
	}

	protected void onRightMouseButtonClick(MouseEvent evt, Path path) {
		if (path instanceof GtkBookmark) {
			GtkBookmark bookmark = (GtkBookmark) path;
			JPopupMenu editPopup = createEditPopup(evt, bookmark);
			editPopup.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	private JPopupMenu createEditPopup(final MouseEvent evt, final GtkBookmark bookmark) {
		JPopupMenu popup = new JPopupMenu();
		// TODO I18N
		JMenuItem removeItem = new JMenuItem("Remove");
		removeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.delete(bookmark.getName());
				refreshBookmarks();
			}
		});
		removeItem.setIcon(GtkStockIcon.get("gtk-remove", Size.GTK_ICON_SIZE_MENU));
		popup.add(removeItem);

		// TODO I18N
		JMenuItem renameItem = new JMenuItem("Rename...");
		renameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JTable table = (JTable) evt.getSource();
				Point p = evt.getPoint();
				final int row = table.rowAtPoint(p);
				table.editCellAt(row, 0);
			}
		});
		popup.add(renameItem);

		return popup;
	}

	/**
	 * GtkBookmarksTableCellRenderer
	 * 
	 * @author c.cerbo
	 * 
	 */
	class GtkBookmarksTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			Path path = (Path) value;
			setText(path.getName());
			setToolTipText(value instanceof GtkBookmark ? path.getLocation() : null);

			setText(path.getName());
			setIcon(GtkStockIcon.get(path.getIconName(), Size.GTK_ICON_SIZE_MENU));

			if (isSelected) {
				setForeground(UIManager.getColor("List.selectionForeground"));
				setBackground(UIManager.getColor("List.selectionBackground"));
			} else {
				setForeground(UIManager.getColor("List.foreground"));
				setBackground(UIManager.getColor("TextPane.background"));
			}

			if ((row + 1) < table.getRowCount()) { // if has next row
				Object nextValue = table.getValueAt(row + 1, 0);
				if (!(value instanceof GtkBookmark) && nextValue instanceof GtkBookmark) {
					// last NOT GtkBookmark row
					setBorder(new LowerBorder(Color.GRAY, 1));
				}
			}

			return this;

		}
	}

	class GtkBookmarksTableCellEditor implements TableCellEditor {

		private static final long serialVersionUID = 1L;

		private TableCellEditor delegate;

		public GtkBookmarksTableCellEditor(JTable table) {
			this.delegate = table.getDefaultEditor(Object.class);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {

			Path path = (Path) value;
			return delegate.getTableCellEditorComponent(table, path.getName(),
					isSelected, row, column);
		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {
			delegate.addCellEditorListener(l);
		}

		@Override
		public void cancelCellEditing() {
			delegate.cancelCellEditing();
		}

		@Override
		public Object getCellEditorValue() {
			return delegate.getCellEditorValue();
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			return delegate.isCellEditable(anEvent);
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
			delegate.removeCellEditorListener(l);
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return delegate.shouldSelectCell(anEvent);
		}

		@Override
		public boolean stopCellEditing() {
			return delegate.stopCellEditing();
		}

	}

	/**
	 * GtkBookmarksTableModel
	 * 
	 * @author c.cerbo
	 * 
	 */
	class GtkBookmarksTableModel implements TableModel {

		private static final long serialVersionUID = 1L;

		private List<Path> locations = new ArrayList<Path>();

		private List<TableModelListener> tableModelListeners;

		public GtkBookmarksTableModel(List<GtkBookmark> bookmarks) {
			this.locations = new ArrayList<Path>();
			this.tableModelListeners = new ArrayList<TableModelListener>();

			locations.addAll(FreeDesktopUtil.getBasicLocations());
			locations.addAll(FreeDesktopUtil.getRemovableDevices());
			locations.addAll(bookmarks);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// There is only a column
			return String.class;
		}

		@Override
		public int getColumnCount() {
			// There is only a column
			return 1;
		}

		@Override
		public String getColumnName(int columnIndex) {
			// TODO I18N
			return "Places";
		}

		@Override
		public int getRowCount() {
			return locations.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return locations.get(rowIndex);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return getValueAt(rowIndex, columnIndex) instanceof GtkBookmark;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			// not used

		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			tableModelListeners.add(l);
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			tableModelListeners.remove(l);
		}

	}

}
