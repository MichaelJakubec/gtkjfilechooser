package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.BorderLayout;

import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.JPanelUtil.PanelElement;

public class RecentlyUsedTableTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());
		RecentlyUsedTable table = new RecentlyUsedTable();

		show(createPanel(new PanelElement(table, BorderLayout.CENTER)));
	}
}
