package eu.kostia.gtkjfilechooser.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import sun.swing.SwingUtilities2;

public class GtkFilePaneMouseListener implements MouseListener {
	private MouseListener doubleClickListener;

	private GtkFilePane filepane;

	public GtkFilePaneMouseListener(GtkFilePane filepane) {
		this.filepane = filepane;
	}

	private JList getFilesList() {
		return filepane.getFilesList();
	}

	private ListSelectionModel getListSelectionModel() {
		return filepane.getListSelectionModel();
	}

	private JFileChooser getFileChooser() {
		return filepane.getFileChooser();
	}

	private int getRowIndex(MouseEvent evt) {
		JComponent source = (JComponent) evt.getSource();

		if (source instanceof JList) {
			return SwingUtilities2.loc2IndexFileList(getFilesList(), evt.getPoint());
		} else if (source instanceof JTable) {
			JTable table = (JTable) source;
			Point p = evt.getPoint();
			return table.rowAtPoint(p);
		}

		return -1;
	}

	public void mouseClicked(MouseEvent evt) {
		int index = getRowIndex(evt);

		// Translate point from table to filesList
		if (index >= 0 && getFilesList() != null
				&& getListSelectionModel().isSelectedIndex(index)) {

			// Make a new event with the filesList as source, placing the
			// click in the corresponding filesList cell.
			Rectangle r = getFilesList().getCellBounds(index, index);
			evt = new MouseEvent(getFilesList(), evt.getID(), evt.getWhen(), evt
					.getModifiers(), r.x + 1, r.y + r.height / 2, evt.getXOnScreen(), evt
					.getYOnScreen(), evt.getClickCount(), evt.isPopupTrigger(), evt
					.getButton());
		}

		if (index >= 0 && SwingUtilities.isLeftMouseButton(evt)) {
			if (evt.getClickCount() == 2) {
				// on double click (open or drill down one directory) be
				// sure to clear the edit index
				filepane.resetEditIndex();
			}
		}

		// Forward event to Basic
		if (getDoubleClickListener() != null) {
			getDoubleClickListener().mouseClicked(evt);
		}
	}



	public void mouseEntered(MouseEvent evt) {
		JComponent source = (JComponent) evt.getSource();
		if (source instanceof JTable) {
			JTable table = (JTable) evt.getSource();

			TransferHandler th1 = getFileChooser().getTransferHandler();
			TransferHandler th2 = table.getTransferHandler();
			if (th1 != th2) {
				table.setTransferHandler(th1);
			}

			boolean dragEnabled = getFileChooser().getDragEnabled();
			if (dragEnabled != table.getDragEnabled()) {
				table.setDragEnabled(dragEnabled);
			}
		} else if (source instanceof JList) {
			// Forward event to Basic
			if (getDoubleClickListener() != null) {
				getDoubleClickListener().mouseEntered(evt);
			}
		}
	}



	public void mousePressed(MouseEvent evt) {		
		int index = getRowIndex(evt);
		getListSelectionModel().setSelectionInterval(index, index);

		if (SwingUtilities.isRightMouseButton(evt)) {
			onRightMouseButtonClick(evt);
		}
	}

	private void onRightMouseButtonClick(MouseEvent evt) {
		JPopupMenu popupMenu = filepane.createContextMenu();
		popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	public void mouseReleased(MouseEvent evt) {
		if (evt.getSource() instanceof JList) {
			// Forward event to Basic
			if (getDoubleClickListener() != null) {
				getDoubleClickListener().mouseReleased(evt);
			}
		}
	}

	private MouseListener getDoubleClickListener() {
		// Lazy creation of Basic's listener
		if (doubleClickListener == null && getFilesList() != null) {
			doubleClickListener = filepane.getFileChooserUIAccessor().createDoubleClickListener(getFilesList());
		}
		return doubleClickListener;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing		
	}
}