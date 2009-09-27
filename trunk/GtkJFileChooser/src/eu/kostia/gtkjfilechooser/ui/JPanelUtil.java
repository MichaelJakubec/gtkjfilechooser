package eu.kostia.gtkjfilechooser.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JPanelUtil {

	/**
	 * Create a panel using the given {@link LayoutManager} and adding the
	 * {@link Component}s in the order they are passed.
	 * 
	 * @param layoutManager
	 * @param components
	 * @return The desired panel
	 */
	static public JPanel createPanel(LayoutManager layoutManager, Component... components) {
		JPanel panel = new JPanel(layoutManager);
		for (Component component : components) {
			panel.add(component);
		}

		return panel;
	}

	static public JPanel createPanel(Component... components) {
		return createPanel(new FlowLayout(), components);
	}

	/**
	 * Create a panel using {@link BorderLayout} and adding the
	 * {@link Component}s in the specified position.
	 * 
	 * @param panelElements
	 * @return The desired panel
	 */
	static public JPanel createPanel(PanelElement... panelElements) {
		JPanel panel = new JPanel(new BorderLayout());
		for (PanelElement elem : panelElements) {
			panel.add(elem.component, elem.position);
		}

		return panel;
	}

	static public class PanelElement {
		private Component component;
		private String position;

		/**
		 * Build a new PanelElement
		 * 
		 * @param component a Component.
		 * @param position A {@link BorderLayout} position, for example {@link BorderLayout#CENTER}.
		 */
		public PanelElement(Component component, String position) {
			super();
			this.component = component;
			this.position = position;
		}
	}

	static public void show(JPanel panel){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().add(panel);
		f.pack();
		f.setVisible(true);
	}
}
