package eu.kostia.gtkjfilechooser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * Decorator for {@link JTextComponent}s to add auto completion support. Based
 * on the Santhosh Kumar's <a
 * href="http://www.jroller.com/santhosh/entry/file_path_autocompletion"
 * >original code</a>.
 * 
 * @author Costantino Cerbo
 * 
 */
//TODO separate the handling for other JTextComponent, here process only JTextFiels.
//TODO All works right for JTextFiels but not yet for the other JTextComponents.
public abstract class Autocompleter {
	private JList list;
	private JPopupMenu popup;
	private JTextComponent textComp;
	private static final String AUTOCOMPLETER = "AUTOCOMPLETER"; // NOI18N
	private Action acceptAction;

	/**
	 * If true the auto completion is active also on deleting chars and the
	 * immediately selection of a singular suggestion is disabled.
	 */
	private boolean completeOnRemove = false;

	public Autocompleter(JTextComponent comp) {
		textComp = comp;
		textComp.putClientProperty(AUTOCOMPLETER, this);
		list = new JList() {
			@Override
			public Dimension getPreferredSize() {
				Dimension dim = super.getPreferredSize();
				if (textComp instanceof JTextField) {
					// In case of JTextFiled use always the same width as the
					// underlying JTextField.
					dim.width = textComp.getWidth() - textComp.getInsets().right;
				}
				return dim;
			}
		};

		addListMouseListener();
		popup = new JPopupMenu();
		JScrollPane scroll = new JScrollPane(list);
		scroll
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);

		list.setFocusable(false);
		scroll.getVerticalScrollBar().setFocusable(false);
		scroll.getHorizontalScrollBar().setFocusable(false);

		popup.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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

		registerAcceptAction();
	}

	private void registerAcceptAction() {
		acceptAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				acceptSuggestion();
			}
		};

		textComp.registerKeyboardAction(acceptAction, KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
	}

	private void addListMouseListener() {
		list.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Highlight list item on mouse over
				int index = list.locationToIndex(e.getPoint());
				list.setSelectedIndex(index);

			}
		});

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Accept the current selection
				acceptSuggestion();
			}
		});

	}

	private DocumentListener documentListener = new DocumentListener() {
		public void insertUpdate(DocumentEvent e) {
			showPopup();
		}

		public void removeUpdate(DocumentEvent e) {
			if (completeOnRemove) {
				showPopup();
			}
		}

		public void changedUpdate(DocumentEvent e) {
		}
	};

	private void setSuggestions(List<String> suggestions) {
		list.setListData(suggestions.toArray(new String[suggestions.size()]));
	}

	private void showPopup() {
		popup.setVisible(false);
		List<String> suggestions = updateSuggestions(textComp.getText());
		setSuggestions(suggestions);
		if (textComp.isEnabled() && suggestions != null && suggestions.size() > 0) {

			if (!(textComp instanceof JTextField)) {
				textComp.getDocument().addDocumentListener(documentListener);
			}

			int size = list.getModel().getSize();
			if (size == 1 && !completeOnRemove) {
				list.setSelectedIndex(0);
				int selectionStart = textComp.getCaretPosition();
				int selectionEnd = ((String) list.getSelectedValue()).length();
				acceptSuggestion();
				popup.setVisible(false);

				selectText(selectionStart, selectionEnd);

				return;
			}

			list.setVisibleRowCount(size < 10 ? size : 10);

			try {
				if (textComp instanceof JTextField) {
					int pos = (int) textComp.getAlignmentX();
					int offset = textComp.getInsets().left;
					int x = textComp.getUI().modelToView(textComp, pos).x - offset;
					popup.show(textComp, x, textComp.getHeight());
				} else {
					int pos = Math.min(textComp.getCaret().getDot(), textComp.getCaret().getMark()); 
					int x = textComp.getUI().modelToView(textComp, pos).x; 
					popup.show(textComp, x, textComp.getCaret().getMagicCaretPosition().y);
				}
			} catch (BadLocationException e) {
				// this should never happen!!!
				e.printStackTrace();
			}

		} else {
			popup.setVisible(false);
		}
		textComp.requestFocus();
	}

	private void selectText(final int selectionStart, final int selectionEnd) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textComp.select(selectionStart, selectionEnd);
			}
		});
	}

	static private Action showAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			Autocompleter completer = (Autocompleter) tf.getClientProperty(AUTOCOMPLETER);
			if (tf.isEnabled()) {
				if (completer.popup.isVisible()) {
					completer.selectNextPossibleValue();
				} else {
					completer.showPopup();
				}
			}
		}
	};

	static private Action upAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			Autocompleter completer = (Autocompleter) tf.getClientProperty(AUTOCOMPLETER);
			if (tf.isEnabled()) {
				if (completer.popup.isVisible()) {
					completer.selectPreviousPossibleValue();
				}
			}
		}
	};

	static private Action hidePopupAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JComponent tf = (JComponent) e.getSource();
			Autocompleter completer = (Autocompleter) tf.getClientProperty(AUTOCOMPLETER);
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

	/**
	 * Accept the current selected suggestion in the list.
	 */
	private void acceptSuggestion() {
		/**
		 * Insertion done in a separate thread with SwingUtilities.invokeLater()
		 * because we need to wait until the AbstractDocument#writeUnlock
		 * operation has completed. Otherwise AbstractDocument#writeLock throws
		 * an IllegalStateException ("Attempt to mutate in notification").
		 * 
		 */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				popup.setVisible(false);
				String selected = (String) list.getSelectedValue();
				int caretPosition = textComp.getCaretPosition();

				try {
					String append = selected.substring(caretPosition);
					textComp.getDocument().insertString(caretPosition, append, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Update the list that contains the auto completion suggestions depending
	 * on the data in text field.
	 * 
	 * @param value
	 *            The current text in the field.
	 * @return The list of the possible auto completions.
	 */
	protected abstract List<String> updateSuggestions(String value);
}
