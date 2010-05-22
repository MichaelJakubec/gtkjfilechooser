package eu.kostia.gtkfiledialog;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class JFileChooserTest {
	public void testSimple() throws Exception {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	    }
	}
	
	public void testInsidePanel() throws Exception {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	    panel.add(new JButton("Press me!"));
	    
	    JFileChooser chooser = new JFileChooser();
	    panel.add(chooser);
	    	    
	    frame.getContentPane().add(panel);
	    
	    frame.pack();
	    frame.setVisible(true);
	}
	
	
	public static void main(String[] args) throws Exception {		
//		UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new JFileChooserTest().testInsidePanel();

	}
}
