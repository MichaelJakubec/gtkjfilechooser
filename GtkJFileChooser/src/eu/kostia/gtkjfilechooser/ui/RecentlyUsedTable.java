package eu.kostia.gtkjfilechooser.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import eu.kostia.gtkjfilechooser.DateUtil;
import eu.kostia.gtkjfilechooser.xbel.Bookmark;
import eu.kostia.gtkjfilechooser.xbel.RecentlyUsedManager;

public class RecentlyUsedTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final int NUMBER_OF_RECENTLY_USED = 30;

	public RecentlyUsedTable() {
		List<Bookmark> bookmarks = new RecentlyUsedManager()
				.readBookmarks(NUMBER_OF_RECENTLY_USED);

		setModel(new RecentlyUsedTableModel(bookmarks));
		setDefaultRenderer(Bookmark.class, new RecentlyUsedRenderer());
		setAutoscrolls(true);
		setRowSelectionAllowed(true);
		setPreferredSize(new Dimension(400,400));
		setShowGrid(false);
		getTableHeader().setResizingAllowed(true);
	}
	
	@Override
	public int getRowHeight() {
		// gnome rows are a taller
		return 22;
	}

	/**
	 * Model
	 */
	private class RecentlyUsedTableModel extends AbstractTableModel implements
			Serializable {

		private static final long serialVersionUID = 1L;

		private List<Bookmark> bookmarks;

		private String[] columnNames = { "Name", "Modified" }; // TODO I18N

		public RecentlyUsedTableModel(List<Bookmark> bookmarks) {
			this.bookmarks = bookmarks;
		}

		@Override
		public int getColumnCount() {
			// column "size" ?
			return columnNames.length;
		}

		@Override
		public Class<? extends Bookmark> getColumnClass(int c) {
			return Bookmark.class;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public int getRowCount() {
			return bookmarks.size();
		}

		@Override
		public Bookmark getValueAt(int rowIndex, int columnIndex) {
			return bookmarks.get(rowIndex);
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

			Bookmark bookmark = (Bookmark) value;

			if (column == 0) {
				File file = new File(bookmark.getHref().substring("file://".length()));
				setText(file.getName());
			} else if (column == 1) {
				setText(DateUtil.toPrettyFormat(bookmark.getModified()));
			}
			
			if (isSelected) {
				setForeground(UIManager.getColor("List.selectionForeground"));
				setBackground(UIManager.getColor("List.selectionBackground"));
			} else {
				setForeground(UIManager.getColor("List.foreground"));
				Color rowcolor = (row % 2 == 0) ? new Color(238, 238, 238) : UIManager.getColor("TextPane.background");
				setBackground(rowcolor);
			}

			return this;
		}

	}

}
