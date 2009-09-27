package eu.kostia.gtkjfilechooser.ui;

import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.createPanel;
import static eu.kostia.gtkjfilechooser.ui.JPanelUtil.show;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import eu.kostia.gtkjfilechooser.ui.GtkButtonsCombo;



public class GtkButtonsComboTestGui {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		File here = new File(".");
		final GtkButtonsCombo pane = new GtkButtonsCombo(here);
		pane.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				GtkButtonsCombo src = (GtkButtonsCombo) e.getSource();
				System.out.println(src.getCurrentDirectory());				
			}			
		});

		JButton changeDirButton = new JButton("Change Dir");
		changeDirButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//pane.setCurrentDirectory(new File(SystemProperties.JAVA_IO_TMPDIR));
				pane.setCurrentDirectory(new File("/home/c.cerbo/workspaces/dm/dm430/head/Projektplanung/Dokumente/Spezifikationen"));
			}
		});

		show(createPanel(new GridLayout(2,1) , pane, createPanel(changeDirButton)));
	}
}
