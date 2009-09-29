package eu.kostia.gtkjfilechooser.ui;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.table.TableRowSorter;

import sun.awt.shell.ShellFolderColumnInfo;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings;
import eu.kostia.gtkjfilechooser.GtkFileChooserSettings.Column;

@SuppressWarnings("unchecked")
public class GtkFilePaneRowSorter extends TableRowSorter implements RowSorterListener {

	private final GtkFilePane filepane;

	public GtkFilePaneRowSorter(GtkFilePane filepane) {
		this.filepane = filepane;
		setModelWrapper(new SorterModelWrapper());
		addRowSorterListener(this);
	}

	@Override
	public void sorterChanged(RowSorterEvent e) {
		if (e.getType() != Type.SORTED) {
			// do nothing if the contents wasn't transformed in some way.
			return;
		}

		RowSorter source = e.getSource();
		List<SortKey> keys = source.getSortKeys();
		
		Column[] columns = filepane.getDetailsTable().getColumnCount() == 2 ? 
				new Column[] { Column.NAME, Column.MODIFIED }	: 
				new Column[] { Column.NAME, Column.SIZE, Column.MODIFIED };
		// The first column in the list is always that one that was sorted.
		SortKey sortKey = keys.get(0);
		int columnIndex = sortKey.getColumn();

		SortOrder order = sortKey.getSortOrder();

		if (order != SortOrder.UNSORTED) {
			// Store sort settings
			GtkFileChooserSettings.get().setSortBy(columns[columnIndex], order );
		}

	}

	public void updateComparators(ShellFolderColumnInfo[] columns) {
		for (int i = 0; i < columns.length; i++) {
			Comparator c = columns[i].getComparator();
			if (c != null) {
				c = new DirectoriesFirstComparatorWrapper(i, c);
			}
			setComparator(i, c);
		}
	}

	@Override
	public void modelStructureChanged() {
		super.modelStructureChanged();
		updateComparators(filepane.getDetailsTableModel().getColumns());
	}

	private class SorterModelWrapper extends ModelWrapper {
		@Override
		public Object getModel() {
			return filepane.getDetailsTableModel();
		}

		@Override
		public int getColumnCount() {
			return filepane.getDetailsTableModel().getColumnCount();
		}

		@Override
		public int getRowCount() {
			return filepane.getDetailsTableModel().getRowCount();
		}

		@Override
		public Object getValueAt(int row, int column) {
			return filepane.getModel().getElementAt(row);
		}

		@Override
		public Object getIdentifier(int row) {
			return row;
		}
	}

	/**
	 * This class sorts directories before files, comparing directory to
	 * directory and file to file using the wrapped comparator.
	 */
	private class DirectoriesFirstComparatorWrapper implements Comparator<File> {
		private Comparator comparator;
		private int column;

		public DirectoriesFirstComparatorWrapper(int column, Comparator comparator) {
			this.column = column;
			this.comparator = comparator;
		}

		public int compare(File f1, File f2) {
			if (f1 != null && f2 != null) {
				boolean traversable1 = filepane.getFileChooser().isTraversable(f1);
				boolean traversable2 = filepane.getFileChooser().isTraversable(f2);
				// directories go first
				if (traversable1 && !traversable2) {
					return -1;
				}
				if (!traversable1 && traversable2) {
					return 1;
				}
			}
			boolean compareByColumn = filepane.getDetailsTableModel().getColumns()[column]
					.isCompareByColumn();
			if (compareByColumn) {
				Object fileColumnValue1 = filepane.getDetailsTableModel()
						.getFileColumnValue(f1, column);
				Object fileColumnValue2 = filepane.getDetailsTableModel()
						.getFileColumnValue(f2, column);

				return comparator.compare(fileColumnValue1, fileColumnValue2);
			}

			// For this column we need to pass the file itself (not a
			// column value) to the comparator
			return comparator.compare(f1, f2);
		}
	}

}
