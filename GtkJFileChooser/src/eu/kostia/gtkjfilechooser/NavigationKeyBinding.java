package eu.kostia.gtkjfilechooser;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Bind key action to a {@link JComponent}. The Key actions dispatch the
 * following signals:
 * <table border="1">
 * <tr>
 * <td>Signal name</td>
 * 
 * <td>Default key combinations</td>
 * </tr>
 * <tr>
 * <td>location-popup</td>
 * <td>
 * <b>Control+L</b> (empty path); <b>/</b> (path of "/") <b>~</b>; (path of "~")
 * </td>
 * 
 * </tr>
 * <tr>
 * <td>up-folder</td>
 * <td>
 * <b>Alt+Up</b>; <b>Backspace</b></td>
 * </tr>
 * <tr>
 * <td>down-folder</td>
 * <td>
 * <b>Alt+Down</b></span></td>
 * </tr>
 * <tr>
 * <td>home-folder</td>
 * <td>
 * 
 * <b>Alt+Home</b></td>
 * </tr>
 * <tr>
 * <td>desktop-folder</td>
 * <td>
 * <b>Alt+D</b></td>
 * </tr>
 * <tr>
 * 
 * <td>quick-bookmark</td>
 * <td>
 * <b>Alt+1</b> through <b>Alt+0</b></td>
 * </tr>
 * </table>
 * 
 * @author c.cerbo
 * 
 */
public class NavigationKeyBinding extends BasicActionDispatcher {

	static final public String LOCATION_POPUP = "location-popup";
	static final public String UP_FOLDER = "up-folder";
	static final public String DOWN_FOLDER = "down-folder";
	static final public String HOME_FOLDER = "home-folder";
	static final public String DESKTOP_FOLDER = "desktop-folder";
	static final public String QUICK_BOOKMARK = "quick-bookmark";

	private JComponent component;

	public NavigationKeyBinding(JComponent component) {
		this.component = component;
		bindKeyAction();
	}

	private void bindKeyAction() {

		// Desktop-folder: Alt+D
		// TODO disable incremental search
		KeyStroke altD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK);
		bind(altD, DESKTOP_FOLDER);

		// Home-folder: Alt+Home
		KeyStroke altHome = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.ALT_DOWN_MASK);
		bind(altHome, HOME_FOLDER);
	}

	private void bind(KeyStroke key, String actionName) {
		if (actionName == null) {
			throw new IllegalArgumentException("The action must have a name.");
		}

		this.component.getInputMap().put(key, actionName);
		this.component.getActionMap().put(actionName, new AbstractAction(actionName) {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireActionEvent(e);
			}
		});
	}
}
