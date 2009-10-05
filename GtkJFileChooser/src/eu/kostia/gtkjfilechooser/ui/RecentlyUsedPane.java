package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import eu.kostia.gtkjfilechooser.DateUtil;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkStockIcon;
import eu.kostia.gtkjfilechooser.GtkStockIcon.Size;
import eu.kostia.gtkjfilechooser.xbel.Bookmark;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedPane extends JComponent {	
	private static final String FILE_NAME_COLUMN_ID = "FileChooser.fileNameHeaderText";
	private static final String FILE_SIZE_COLUMN_ID = "FileChooser.fileSizeHeaderText";
	private static final int FILE_SIZE_COLUMN_WIDTH = 100;
	private static final String FILE_DATE_COLUMN_ID = "FileChooser.fileDateHeaderText";
	private static final int FILE_DATE_COLUMN_WIDTH = 125;

	public static final String SELECTED = "selected";
	public static final int SELECTED_ID = 1;

	public static final String DOUBLE_CLICK = "double_click";
	public static final int DOUBLE_CLICK_ID = 2;

	private static final long serialVersionUID = 1L;

	private static final int NUMBER_OF_RECENTLY_USED = 30;

	private JTable table;

	private List<ActionListener> actionListeners;

	public RecentlyUsedPane() {
		setLayout(new BorderLayout());

		table = new JTable();
		table.setAutoCreateColumnsFromModel(false);
		actionListeners = new ArrayList<ActionListener>();
		table.setColumnModel(new RecentlyUsedTableColumnModel());
		table.getTableHeader().setReorderingAllowed(false);
		
		updateModel();
		
		table.setRowSorter(new RecentlyUsedTableRowSorter());
		table.setDefaultRenderer(Object.class, new RecentlyUsedRenderer());
		table.setRowSelectionAllowed(true);
		table.setShowGrid(false);
		table.getTableHeader().setResizingAllowed(true);
		// gnome rows are taller
		table.setRowHeight(22);
		final RecentlyUsedPane thisPane = this;
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ActionEvent event = null;
				if (e.getClickCount() == 2) {
					event = new ActionEvent(thisPane, DOUBLE_CLICK_ID, DOUBLE_CLICK);
				} else {
					event = new ActionEvent(thisPane, SELECTED_ID, SELECTED);
				}

				fireActionEvent(event);
			}
		});

		createColumnsFromModel(table);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public void updateModel() {
		List<Bookmark> bookmarks = new RecentlyUsedManager()
		.readBookmarks(NUMBER_OF_RECENTLY_USED);		
		RecentlyUsedTableModel dataModel = new RecentlyUsedTableModel(bookmarks);
		table.setModel(dataModel);
	}
	
    private void createColumnsFromModel(JTable aTable) {
    	RecentlyUsedTableModel m = (RecentlyUsedTableModel) aTable.getModel();
        if (m != null) {
            // Remove any current columns
            TableColumnModel cm = aTable.getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
	    }

            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumn newColumn = new TableColumn(i);
                newColumn.setIdentifier(m.getColumnId(i));
                aTable.addColumn(newColumn);
            }
        }
    }

	public File getSelectedFile() {
		int row = table.getSelectedRow();
		if (row == -1) {
			return null;
		}

		return (File) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
	}

	public void addActionListeners(ActionListener l) {
		this.actionListeners.add(l);
	}

	public void removeActionListeners(ActionListener l) {
		this.actionListeners.remove(l);
	}

	public void fireActionEvent(ActionEvent e) {
		for (ActionListener l : actionListeners) {
			l.actionPerformed(e);
		}
	}
	
	private Boolean getShowSizeColumn() {
		return GtkFileChooserSettings.get().getShowSizeColumn();
	}

	/**
	 * Model
	 */
	private class RecentlyUsedTableModel extends AbstractTableModel implements
	Serializable {

		private static final long serialVersionUID = 1L;

		private Object[][] data;

		private String[] columnNames;
		private String[] columnIds;

		/**
		 * Indicates which columns are visible
		 */
		private boolean[] columnsVisible;

		public RecentlyUsedTableModel(List<Bookmark> bookmarks) {
			this.columnIds = new String[] { FILE_NAME_COLUMN_ID, FILE_SIZE_COLUMN_ID, FILE_DATE_COLUMN_ID };
			this.columnNames = new String[columnIds.length];
			for (int i = 0; i < columnIds.length; i++) {
				String columnId = columnIds[i];
				columnNames[i] = UIManager.getString(columnId);				
			}
			
			this.columnsVisible = new boolean[columnNames.length];
			this.columnsVisible[0] = true;
			this.columnsVisible[1] = getShowSizeColumn();
			this.columnsVisible[2] = true;

			this.data = new Object[bookmarks.size()][columnNames.length];
			for (int i = 0; i < bookmarks.size(); i++) {
				Bookmark bookmark = bookmarks.get(i);
				File file = new File((bookmark.getHref()).substring("file://".length()));
				data[i][0] = file;
				data[i][1] = file.length();
				data[i][2] = bookmark.getModified();
			}
		}


		/**
		 * Maps the index of the column in the table model at the index of the
		 * visible column in the view. Returns the index of the corresponding
		 * column in the view
		 * 
		 * @param modelColumnIndex
		 *            the index of the column in the model
		 * @return the index of the corresponding column in the view
		 * 
		 */
		protected int convertToVisibleColumnIndex(int col) {
			int n = col; // right number to return
			int i = 0;
			do {
				if (!(columnsVisible[i])) n++;
				i++;
			} while (i < n);
			// If we are on an invisible column,
			// we have to go one step further
			while (!(columnsVisible[n]))
				n++;
			return n;
		}

		// *** TABLE MODEL METHODS ***

		public int getColumnCount() {
			int n = 0;
			for (int i = 0; i < columnsVisible.length; i++)
				if (columnsVisible[i]) n++;
			return n;
		}

		public int getRowCount() {
			return data.length;
		}

		public Object getValueAt(int row, int col) {
			return data[row][convertToVisibleColumnIndex(col)];
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[convertToVisibleColumnIndex(col)];
		}
		
		/**
		 * Return the unmodifiable not localized column identifier.
		 * 
		 * @param col The model column index.
		 * @return The column identifier.
		 */
		public String getColumnId(int col) {
			return columnIds[convertToVisibleColumnIndex(col)];
		}

		@Override
		public Class<?> getColumnClass(int col) {
			return data[0][convertToVisibleColumnIndex(col)] != null ? data[0][convertToVisibleColumnIndex(col)]
			                                                                   .getClass()
			                                                                   : Object.class;
		}
	}

	/**
	 * Cell renderer
	 */
	private class RecentlyUsedRenderer extends DefaultTableCellRenderer {

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
				setText(FreeDesktopUtil.humanreadble(bytes, 0));
			} else if (value instanceof Date) {
				// last modified column
				Date date = (Date) value;
				setText(DateUtil.toPrettyFormat(date));
			}

			if (isSelected) {
				setForeground(UIManager.getColor("List.selectionForeground"));
				setBackground(UIManager.getColor("List.selectionBackground"));
			} else {
				setForeground(UIManager.getColor("List.foreground"));
				Color rowcolor = (row % 2 == 0) ? new Color(238, 238, 238) : UIManager
						.getColor("TextPane.background");
				setBackground(rowcolor);
			}

			return this;
		}

	}

	private class RecentlyUsedTableColumnModel extends DefaultTableColumnModel {
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
				if(getShowSizeColumn()) {
					offset += FILE_SIZE_COLUMN_WIDTH;
				}
				col.setPreferredWidth(getTotalColumnWidth() - offset);
			}

			return col;
		}

	}

	private class RecentlyUsedTableRowSorter extends
	TableRowSorter<RecentlyUsedTableModel> {
		public RecentlyUsedTableRowSorter() {
			super((RecentlyUsedTableModel) table.getModel());
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

}
