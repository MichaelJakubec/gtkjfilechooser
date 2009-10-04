package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class RecentlyUsedPaneTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		RecentlyUsedPane pane = new RecentlyUsedPane();
		pane.addActionListeners(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RecentlyUsedPane p = (RecentlyUsedPane)e.getSource();
				System.out.println(e.getActionCommand()+": "+p.getSelectedFile());
			}
		});

		show(createPanel(new PanelElement(pane, BorderLayout.CENTER)));
	}
}
