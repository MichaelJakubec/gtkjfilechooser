package eu.kostia.gtkjfilechooser.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public abstract class AutoCompleter {
	private JList list = new JList();
	private JPopupMenu popup = new JPopupMenu();
	private JTextComponent textComp;
	private static final String AUTOCOMPLETER = "AUTOCOMPLETER"; // NOI18N

	public AutoCompleter(JTextComponent comp) {
		textComp = comp;
		textComp.putClientProperty(AUTOCOMPLETER, this);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(null);

		list.setFocusable(false);
		scroll.getVerticalScrollBar().setFocusable(false);
		scroll.getHorizontalScrollBar().setFocusable(false);

		popup.setBorder(BorderFactory.createLineBorder(Color.black));
		popup.add(scroll);

		if (textComp instanceof JTextField) {
			textComp.registerKeyboardAction(showAction, KeyStroke.getKeyStroke(
					KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
			textComp.getDocument().addDocumentListener(documentListener);
		} else {
			textComp.registerKeyboardAction(showAction, KeyStroke.getKeyStroke(
					KeyEvent.VK_SPACE, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
		}

		textComp.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP,
				0), JComponent.WHEN_FOCUSED);
		textComp.registerKeyboardAction(hidePopupAction, KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);

		popup.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				textComp.unregisterKeyboardAction(KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0));
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		list.setRequestFocusEnabled(false);
	}

	DocumentListener documentListener = new DocumentListener() {
		public void insertUpdate(DocumentEvent e) {
			showPopup();
		}

		public void removeUpdate(DocumentEvent e) {
			showPopup();
		}

		public void changedUpdate(DocumentEvent e) {
		}
	};

	private void setSuggestions(List<String> suggestions) {
		list.setListData(suggestions.toArray(new String[suggestions.size()]));
	}

	private void showPopup() {
		popup.setVisible(false);
		List<String> suggestions = updateListData(textComp.getText());
		if (textComp.isEnabled() && suggestions != null && suggestions.size() > 0 && list.getModel().getSize() != 0) {
			
			setSuggestions(suggestions);
			
			if (!(textComp instanceof JTextField)) {
				textComp.getDocument().addDocumentListener(documentListener);
			}

			Action acceptAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					JComponent tf = (JComponent) e.getSource();
					AutoCompleter completer = (AutoCompleter) tf
							.getClientProperty(AUTOCOMPLETER);
					completer.popup.setVisible(false);
					String selectedValue = (String) completer.list.getSelectedValue();
					completer.acceptedListItem(selectedValue);
				}
			};

			textComp.registerKeyboardAction(acceptAction, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
			int size = list.getModel().getSize();
			debug("size: " + size);
			if (size == 1) {
				list.setSelectedIndex(0);
			}

			list.setVisibleRowCount(size < 10 ? size : 10);

			int x = 0;
			try {
				int pos = Math.min(textComp.getCaret().getDot(), textComp.getCaret()
						.getMark());
				x = textComp.getUI().modelToView(textComp, pos).x;
			} catch (BadLocationException e) {
				// this should never happen!!!
				e.printStackTrace();
			}
			popup.show(textComp, x, textComp.getHeight());
		} else {
			popup.setVisible(false);
		}
		textComp.requestFocus();
	}

	static Action showAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
			if (tf.isEnabled()) {
				if (completer.popup.isVisible()) {
					completer.selectNextPossibleValue();
				} else {
					completer.showPopup();
				}
			}
		}
	};

	static Action upAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
			if (tf.isEnabled()) {
				if (completer.popup.isVisible()) {
					completer.selectPreviousPossibleValue();
				}
			}
		}
	};

	static Action hidePopupAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
			if (tf.isEnabled()) {
				completer.popup.setVisible(false);
			}
		}
	};

	/**
	 * Selects the next item in the list. It won't change the selection if the
	 * currently selected item is already the last item.
	 */
	protected void selectNextPossibleValue() {
		int si = list.getSelectedIndex();

		if (si < list.getModel().getSize() - 1) {
			list.setSelectedIndex(si + 1);
			list.ensureIndexIsVisible(si + 1);
		}
	}

	/**
	 * Selects the previous item in the list. It won't change the selection if
	 * the currently selected item is already the first item.
	 */
	protected void selectPreviousPossibleValue() {
		int si = list.getSelectedIndex();

		if (si > 0) {
			list.setSelectedIndex(si - 1);
			list.ensureIndexIsVisible(si - 1);
		}
	}

	private void debug(String msg) {
		System.out.println(msg);
	}

	// update list model depending on the data in textfield
	protected abstract List<String> updateListData(String value);

	/**
	 * Update the textfield according the user selection.
	 */
	protected void acceptedListItem(String selected) {
		int caretPosition = textComp.getCaretPosition();

		try {
			textComp.getDocument().insertString(caretPosition,
					selected.substring(caretPosition), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
