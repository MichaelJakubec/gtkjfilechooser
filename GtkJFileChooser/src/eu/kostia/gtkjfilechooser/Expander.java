package eu.kostia.gtkjfilechooser;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Expander extends JComponent implements PropertyChangeListener {
	static final public String EXPANDED_STATUS_CHANGED = "expanded_status_changed";
	private JLabel label;
	private JComponent component;
	private boolean expanded = false;

	public Expander(String text, JComponent aComponent) {
		this.component = aComponent;
		addPropertyChangeListener(this);

		setLayout(new BorderLayout());		

		label = new JLabel(text);
		label.setIcon(new ExpanderIcon(false, false));


		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Log.debug("mouseClicked");
				expanded = !expanded;
				firePropertyChange(EXPANDED_STATUS_CHANGED, !expanded, expanded);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Log.debug("mouseEntered");
				//label.setOpaque(true);
				//label.setBackground(new Color(230, 230, 230));			
				label.setIcon(expanded ? new ExpanderIcon(true, true) : new ExpanderIcon(false, true));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Log.debug("mouseExited");
				label.setOpaque(false);
				label.setBackground(UIManager.getColor("Label.background"));
				label.setIcon(expanded ? new ExpanderIcon(true, false) : new ExpanderIcon(false, false));
			}
		});

		add(label, BorderLayout.PAGE_START);

		component.setVisible(false);
		add(component, BorderLayout.CENTER);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		Object value = evt.getNewValue();
		Log.debug(property, " = ", value);

		if (EXPANDED_STATUS_CHANGED.equals(property)){
			component.setVisible(expanded);

			label.setIcon(expanded ? new ExpanderIcon(true, true) : new ExpanderIcon(false, true));

		}
	}
}

