package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.I18N._;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import eu.kostia.gtkjfilechooser.ActionDispatcher;
import eu.kostia.gtkjfilechooser.BasicActionDispatcher;
import eu.kostia.gtkjfilechooser.DateUtil;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;

public class FilesListPane extends JComponent implements ActionDispatcher {

	public static final Color PEARL_GRAY = new Color(238, 238, 238);

	private static final String FILE_NAME_COLUMN_ID = "Name";
	private static final String FILE_SIZE_COLUMN_ID = "Size";
	private static final int FILE_SIZE_COLUMN_WIDTH = 100;
	private static final String FILE_DATE_COLUMN_ID = "Modified";
	private static final int FILE_DATE_COLUMN_WIDTH = 125;

	public static final String SELECTED = "selected";
	public static final int SELECTED_ID = 1;

	public static final String DOUBLE_CLICK = "double_click";
	public static final int DOUBLE_CLICK_ID = 2;

	public static final String ENTER_PRESSED = "enter pressed";
	public static final int ENTER_PRESSED_ID = 3;

	private static final long serialVersionUID = 1L;

	protected JTable table;

	private ActionDispatcher actionDispatcher = new BasicActionDispatcher();

	private boolean filesSelectable = true;

	public FilesListPane() {
		this(new ArrayList<File>());
	}

	public FilesListPane(List<File> fileEntries) {
		setLayout(new BorderLayout());

		table = new JTable() {
			@Override
			public void changeSelection(int row, int column, boolean toggle,
					boolean extend) {
				File file = (File) getValueAt(row, 0);
				if (FilesListPane.this.isRowEnabled(file)) {
					// If the row isn't enabled, don't allow the selection.
					super.changeSelection(row, column, toggle, extend);
				}
			}
		};

		table.setColumnModel(new FilesListTableColumnModel());
		table.setAutoCreateColumnsFromModel(false);
		table.setBackground(UIManager.getColor("TextPane.background"));
		table.getTableHeader().setReorderingAllowed(false);

		Boolean showSizeColumn = GtkFileChooserSettings.get().getShowSizeColumn();
		setModel(fileEntries, showSizeColumn);

		table.setDefaultRenderer(Object.class, new FilesListRenderer());
		table.setRowSelectionAllowed(true);
		table.setShowGrid(false);
		table.getTableHeader().setResizingAllowed(true);
		// Gnome rows are taller
		table.setRowHeight(22);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ActionEvent event = null;
				if (e.getClickCount() == 2) {
					event = new ActionEvent(FilesListPane.this, DOUBLE_CLICK_ID,
							DOUBLE_CLICK);
				} else {
					event = new ActionEvent(FilesListPane.this, SELECTED_ID, SELECTED);
				}

				fireActionEvent(event);
			}
		});

		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int ch = e.getKeyChar();

				if (ch == KeyEvent.VK_ENTER) {
					fireActionEvent(new ActionEvent(FilesListPane.this, ENTER_PRESSED_ID,
							ENTER_PRESSED));
				}
			}
		});


		// Add interactive file search support
		new FileFindAction().install(table);

		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void setShowSizeColumn(boolean showSizeColumn) {
		GtkFileChooserSettings.get().setShowSizeColumn(showSizeColumn);
		setModel(new ArrayList<File>(), showSizeColumn);
		table.createDefaultColumnsFromModel();
	}

	public void uninstallUI() {
		table = null;
		removeAllActionListeners();
	}

	/**
	 * Sets the table's selection mode to allow only single selections, a single
	 * contiguous interval, or multiple intervals.
	 * 
	 * @see JList#setSelectionMode
	 */
	public void setSelectionMode(int selectionMode) {
		table.setSelectionMode(selectionMode);
	}

	/**
	 * Append a new {@link File} to this table.Notification of the row being
	 * added will be generated.
	 * 
	 * @param entry
	 *            the {@link File} to be inserted.
	 */
	public void addFile(File entry) {
		FilesListTableModel dataModel = (FilesListTableModel) table.getModel();
		dataModel.addFile(entry);
	}

	/**
	 * Set if the the files are enabled/selectable; for example when
	 * FileSelectionMode = DIRECTORIES_ONLY.
	 * 
	 * @param filesSelectable
	 */
	public void setFilesSelectable(boolean filesSelectable) {
		this.filesSelectable = filesSelectable;
	}

	public void setModel(List<File> fileEntries, Boolean showSizeColumn) {		
		FilesListTableModel dataModel = new FilesListTableModel(fileEntries,
				showSizeColumn);
		table.setModel(dataModel);

		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));

		FilesListTableRowSorter sorter = new FilesListTableRowSorter();
		sorter.setSortKeys(sortKeys);
		table.setRowSorter(sorter);

		createColumnsFromModel();
	}

	private void createColumnsFromModel() {
		FilesListTableModel m = (FilesListTableModel) table.getModel();
		if (m != null) {
			// Remove any current columns
			TableColumnModel cm = table.getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				TableColumn newColumn = new TableColumn(i);
				newColumn.setIdentifier(m.getColumnId(i));
				table.addColumn(newColumn);
			}
		}
	}



	public FilesListTableModel getModel() {
		return (FilesListTableModel) table.getModel();
	}

	private boolean isRowEnabled(File file) {
		// Directory are always enabled
		if (file.isDirectory()) {
			return true;
		}

		// When FileSelectionMode = DIRECTORIES_ONLY, disable files
		// Use !file.isDirectory() instead of file.isFile() because
		// the last one doesn't return true for links
		return !file.isDirectory() && filesSelectable;
	}



	public File getSelectedFile() {
		int row = table.getSelectedRow();
		if (row == -1) {
			return null;
		}

		return (File) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
	}

	public File[] getSelectedFiles() {
		int[] rows = table.getSelectedRows();
		if (rows.length == 0) {
			return null;
		}

		File[] selectesFiles = new File[rows.length];
		for (int i = 0; i < rows.length; i++) {
			int rowIndex = rows[i];
			selectesFiles[i] = (File) table.getModel().getValueAt(
					table.convertRowIndexToModel(rowIndex), 0);
		}
		return selectesFiles;
	}

	public void clearSelection() {
		table.getSelectionModel().clearSelection();
	}

	/**
	 * Model
	 */
	protected class FilesListTableModel extends AbstractTableModel implements
	Serializable, TableModelListener {

		private static final long serialVersionUID = 1L;

		private List<Object[]> data;

		private String[] columnNames;
		private String[] columnIds;

		private boolean showSizeColumn;

		public FilesListTableModel(List<File> fileEntries, boolean showSizeColumn) {
			this.data = new ArrayList<Object[]>();
			this.showSizeColumn = showSizeColumn;

			addTableModelListener(this);

			if (getShowSizeColumn()) {
				this.columnIds = new String[] { FILE_NAME_COLUMN_ID, FILE_SIZE_COLUMN_ID, FILE_DATE_COLUMN_ID };
			} else {
				this.columnIds = new String[] { FILE_NAME_COLUMN_ID, FILE_DATE_COLUMN_ID };
			}


			this.columnNames = new String[columnIds.length];
			for (int i = 0; i < columnIds.length; i++) {
				String columnId = columnIds[i];
				columnNames[i] = _(columnId);
			}


			for (File file : fileEntries) {
				addFileEntryInternal(file);
			}
		}

		private Boolean getShowSizeColumn() {
			return showSizeColumn;
		}

		public void clear() {
			this.data = new ArrayList<Object[]>();
			fireTableDataChanged();
		}

		private void addFileEntryInternal(File file) {
			Object[] row = new Object[getColumnCount()];
			row[0] = file;

			if (getShowSizeColumn()) {
				row[1] = file != null && !file.isDirectory() ? file.length() : -1L;
				row[2] = new Date(file.lastModified());
			} else {
				row[1] = new Date(file.lastModified());
			}			

			data.add(row);
		}

		/**
		 * Append a new {@link File} to this table.Notification of the row being
		 * added will be generated.
		 * 
		 * @param entry
		 *            the {@link File} to be inserted.
		 */
		public void addFile(File entry) {
			addFileEntryInternal(entry);
			int row = getRowCount() - 1;
			fireTableRowsInserted(row, row);
		}


		// *** TABLE MODEL METHODS ***

		public int getColumnCount() {
			return getShowSizeColumn() ? 3 : 2;
		}

		public int getRowCount() {
			return data.size();
		}

		public Object getValueAt(int row, int col) {
			checkColumnIndex(col);
			return data.get(row)[col];
		}

		@Override
		public String getColumnName(int col) {
			checkColumnIndex(col);
			return columnNames[col];
		}

		/**
		 * Return the unmodifiable not localized column identifier.
		 * 
		 * @param col
		 *            The model column index.
		 * @return The column identifier.
		 */
		public String getColumnId(int col) {
			checkColumnIndex(col);
			return columnIds[col];
		}

		@Override
		public Class<?> getColumnClass(int col) {
			checkColumnIndex(col);

			if (!data.isEmpty() && data.get(0)[col] != null) {
				return data.get(0)[col].getClass();
			}

			return Object.class;
		}

		private void checkColumnIndex(int col) {
			if (col >= getColumnCount()) {
				throw new IllegalArgumentException(col + " greater the the column count");
			}			
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			table.getRowSorter().allRowsChanged();
		}
	}

	/**
	 * Cell renderer
	 */
	protected class FilesListRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			// reset the icon for all columns
			setIcon(null);

			File file = (File) table.getValueAt(row, 0);
			setToolTipText(file.getAbsolutePath());

			if (value instanceof File) {
				// filename column
				setText(file.getName());
				setIcon(GtkStockIcon.get(file, Size.GTK_ICON_SIZE_MENU));
			} else if (value instanceof Long) {
				// size column
				Long bytes = (Long) value;				
				setText(bytes != -1 ? FreeDesktopUtil.humanreadble(bytes, 0) : "");
			} else if (value instanceof Date) {
				// last modified column
				Date date = (Date) value;
				setText(DateUtil.toPrettyFormat(date));
			}

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());

				Color rowcolor = (row % 2 == 0) ? PEARL_GRAY : table.getBackground();
				setBackground(rowcolor);
			}

			setEnabled(FilesListPane.this.isRowEnabled(file));

			return this;
		}
	}

	private class FilesListTableColumnModel extends DefaultTableColumnModel {
		private static final long serialVersionUID = 1L;

		@Override
		public TableColumn getColumn(int columnIndex) {
			TableColumn col = super.getColumn(columnIndex);
			String columnId = col.getIdentifier().toString();
			if (FILE_SIZE_COLUMN_ID.equals(columnId)) {
				col.setPreferredWidth(FILE_SIZE_COLUMN_WIDTH);
			} else if (FILE_DATE_COLUMN_ID.equals(columnId)) {
				col.setPreferredWidth(FILE_DATE_COLUMN_WIDTH);
			} else {
				// The filename column fills the remaining space.
				int offset = FILE_DATE_COLUMN_WIDTH;
				if (table.getModel().getColumnCount() == 3) {
					offset += FILE_SIZE_COLUMN_WIDTH;
				}
				col.setPreferredWidth(getTotalColumnWidth() - offset);
			}

			return col;
		}
	}

	protected class FilesListTableRowSorter extends TableRowSorter<FilesListTableModel> {
		public FilesListTableRowSorter() {
			super((FilesListTableModel) table.getModel());
		}

		@SuppressWarnings("unchecked")
		@Override
		public Comparator<?> getComparator(int column) {

			Class<?> columnClass = getModel().getColumnClass(column);

			// filename column
			if (columnClass.equals(File.class)) {
				return new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						// directories go first
						if (o1.isDirectory() && !o2.isDirectory()) {
							return -1;
						}
						if (!o1.isDirectory() && o2.isDirectory()) {
							return 1;
						}
						return o1.getName().compareTo(o2.getName());
					}
				};
			}

			// generic comparator
			return new Comparator<Comparable>() {
				@Override
				public int compare(Comparable o1, Comparable o2) {
					return o1.compareTo(o2);
				}
			};

		}

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
