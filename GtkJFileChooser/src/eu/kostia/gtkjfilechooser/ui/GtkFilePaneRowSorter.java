package eu.kostia.gtkjfilechooser.ui;

import java.io.File;
import java.util.Comparator;

import javax.swing.table.TableRowSorter;

import sun.awt.shell.ShellFolderColumnInfo;

@SuppressWarnings("unchecked")
public class GtkFilePaneRowSorter extends TableRowSorter {
	private GtkFilePane filepane;

	public GtkFilePaneRowSorter(GtkFilePane filepane) {
		this.filepane = filepane;
		setModelWrapper(new SorterModelWrapper());
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
			if (filepane.getDetailsTableModel().getColumns()[column].isCompareByColumn()) {
				return comparator.compare(filepane.getDetailsTableModel().getFileColumnValue(f1,
						column), filepane.getDetailsTableModel().getFileColumnValue(f2, column));
			}
			// For this column we need to pass the file itself (not a
			// column value) to the comparator
			return comparator.compare(f1, f2);
		}
	}
}
