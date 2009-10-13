package eu.kostia.gtkjfilechooser.ui;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Position;

public abstract class FindAction extends AbstractAction implements DocumentListener,
KeyListener {
	private JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
	protected JTextField searchField;
	private JPopupMenu popup = new JPopupMenu();
	static final private int TIMEOUT = 5;
	private long lastKeyPressed;

	/**
	 * Interrupt the timeout thread for the popup
	 */
	private boolean stop = false;

	public FindAction() {
		super("Incremental Search"); // NOI18N
		searchField = new JTextField(11);
		searchPanel.add(searchField);

		popup.add(searchPanel);

		// when the window containing the "comp" has registered Esc key
		// then on pressing Esc instead of search popup getting closed
		// the event is sent to the window. to overcome this we
		// register an action for Esc.
		searchField.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.setVisible(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
	}

	public String getName() {
		return (String) getValue(Action.NAME);
	}

	protected JComponent comp = null;

	/*-------------------------------------------------[ ActionListener ]---------------------------------------------------*/

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == searchField) {
			popup.setVisible(false);
		} else {
			comp = (JComponent) ae.getSource();

			searchField.removeActionListener(this);
			searchField.removeKeyListener(this);
			searchField.getDocument().removeDocumentListener(this);
			initSearch(ae);
			searchField.addActionListener(this);
			searchField.addKeyListener(this);
			searchField.getDocument().addDocumentListener(this);

			JComponent parent = (JComponent) comp.getParent();
			Rectangle rect = parent.getVisibleRect();

			int x = rect.x + rect.width - popup.getPreferredSize().width;
			int y = rect.y + rect.height - popup.getPreferredSize().height;
			popup.show(comp, x, y);
			searchField.requestFocus();
		}
	}

	/**
	 * Can be overridden by subclasses to change initial search text etc.
	 */
	protected void initSearch(ActionEvent ae) {
		searchField.setText(ae.getActionCommand()); // NOI18N
		changed(null);
	}

	private void changed(Position.Bias bias) {
		popup.pack();

		searchField.requestFocus();
		Color color = changed(comp, searchField.getText(), bias) ? BLACK : RED;
		searchField.setForeground(color);
		lastKeyPressed = System.currentTimeMillis();
	}

	/**
	 * Should search for given text and select item and
	 * 
	 * @return true if search is successful
	 */
	protected abstract boolean changed(JComponent comp, String text, Position.Bias bias);

	/*-------------------------------------------------[ DocumentListener ]---------------------------------------------------*/

	public void insertUpdate(DocumentEvent e) {
		changed(null);
	}

	public void removeUpdate(DocumentEvent e) {
		changed(null);
	}

	public void changedUpdate(DocumentEvent e) {
	}

	/*-------------------------------------------------[ KeyListener ]---------------------------------------------------*/

	protected boolean shiftDown = false;
	protected boolean controlDown = false;

	public void keyPressed(KeyEvent ke) {
		shiftDown = ke.isShiftDown();
		controlDown = ke.isControlDown();

		switch (ke.getKeyCode()) {
		case KeyEvent.VK_UP:
			changed(Position.Bias.Backward);
			break;
		case KeyEvent.VK_DOWN:
			changed(Position.Bias.Forward);
			break;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	/*-------------------------------------------------[ Installation ]---------------------------------------------------*/

	public void install(final JComponent comp) {
		// comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('I',
		// InputEvent.CTRL_MASK), getName());
		// comp.getActionMap().put(getName(), this);

		comp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String ch = String.valueOf(e.getKeyChar());
				ActionEvent ae = new ActionEvent(comp, 1001, ch);
				actionPerformed(ae);
			}
		});

		// Thread to close the popup after the timeout (5 seconds)
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stop) {
					long time = (System.currentTimeMillis() - lastKeyPressed) / 1000L;
					System.out.println(time);
					if (time > TIMEOUT && popup.isShowing()) {
						popup.setVisible(false);
					}
				}

			}
		}).start();

		//TODO find a better solution to stop the timeout thread.
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {			
			@Override
			public void run() {
				stop = true;
				System.out.println("shutdownHook");
			}
		}));
	}
}