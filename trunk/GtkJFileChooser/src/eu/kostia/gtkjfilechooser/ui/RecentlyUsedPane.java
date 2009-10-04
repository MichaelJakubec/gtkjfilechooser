package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.table.TableRowSorter;

import eu.kostia.gtkjfilechooser.DateUtil;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.xbel.Bookmark;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedPane extends JComponent {

	private static final String SELECTED = "selected";
	private static final int SELECTED_ID = 1;

	private static final String DOUBLE_CLICK = "double_click";
	private static final int DOUBLE_CLICK_ID = 2;

	private static final long serialVersionUID = 1L;

	private static final int NUMBER_OF_RECENTLY_USED = 10;

	private JTable table;

	private List<ActionListener> actionListeners;

	public RecentlyUsedPane() {
		setLayout(new BorderLayout());

		table = new JTable();
		actionListeners = new ArrayList<ActionListener>();
		table.setColumnModel(new RecentlyUsedTableColumnModel());

		List<Bookmark> bookmarks = new RecentlyUsedManager()
		.readBookmarks(NUMBER_OF_RECENTLY_USED);

		table.getTableHeader().setReorderingAllowed(false);
		RecentlyUsedTableModel dataModel = new RecentlyUsedTableModel(bookmarks);
		table.setModel(dataModel);
		table.setRowSorter(new RecentlyUsedTableRowSorter(dataModel));
		table.setDefaultRenderer(Object.class, new RecentlyUsedRenderer());
		table.setAutoscrolls(true);
		table.setRowSelectionAllowed(true);
		table.setPreferredSize(new Dimension(400, 400));
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

		add(new JScrollPane(table), BorderLayout.CENTER);
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

	/**
	 * Model
	 */
	private class RecentlyUsedTableModel extends AbstractTableModel implements
	Serializable {

		private static final long serialVersionUID = 1L;

		private Object[][] data;

		private String[] columnNames;

		/**
		 * Indicates which columns are visible
		 */
		private boolean[] columnsVisible;

		public RecentlyUsedTableModel(List<Bookmark> bookmarks) {
			// TODO I18N
			this.columnNames = new String[] { "Name", "Size", "Modified" };
			this.columnsVisible = new boolean[columnNames.length];
			this.columnsVisible[0] = true;
			this.columnsVisible[1] = true; // GtkFileChooserSettings.get().getShowSizeColumn();
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

			File file = (File) table.getValueAt(row, 0);
			setToolTipText(file.getAbsolutePath());

			if (value instanceof File) {
				// filename column
				setText(file.getName());
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
		/**
		 * Width of the columns that aren't the file name (size or modified)
		 */
		private static final int OTHER_COLUMNS_WIDTH = 125;

		private static final long serialVersionUID = 1L;

		@Override
		public TableColumn getColumn(int columnIndex) {
			TableColumn col = super.getColumn(columnIndex);

			if (columnIndex > 0) {
				col.setPreferredWidth(OTHER_COLUMNS_WIDTH);
			} else {
				col.setPreferredWidth(getTotalColumnWidth() - OTHER_COLUMNS_WIDTH);
			}

			return col;
		}

	}

	private class RecentlyUsedTableRowSorter extends
	TableRowSorter<RecentlyUsedTableModel> {
		public RecentlyUsedTableRowSorter(RecentlyUsedTableModel model) {
			super(model);
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
