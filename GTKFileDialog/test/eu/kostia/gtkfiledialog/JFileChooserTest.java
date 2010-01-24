package eu.kostia.gtkfiledialog;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class JFileChooserTest {
	public static void main(String[] args) throws Exception {
		LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lookAndFeelInfo : lafs) {
			System.out.println(lookAndFeelInfo.getName());
		}
		
		UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	    }

	}
}
