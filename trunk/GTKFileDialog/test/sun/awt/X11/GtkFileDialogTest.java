package sun.awt.X11;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GtkFileDialogTest {

	public void testAlone() {
		GtkFileDialog fd = new GtkFileDialog(null, "My File Dialog");
		fd.setMode(0);
		fd.setVisible(true);
		System.out.println("File: " + fd.getFile());
	}

	public void testInFrame() {
		final JFrame frame = new JFrame();
		//DISPOSE_ON_CLOSE to reproduce the error:
//		Gdk-ERROR **: The program '<unknown>' received an X Window System error.
//		This probably reflects a bug in the program.
//		The error was 'BadWindow (invalid Window parameter)'.
//		  (Details: serial 317 error_code 3 request_code 20 minor_code 0)
//		  (Note to programmers: normally, X errors are reported asynchronously;
//		   that is, you will receive the error a while after causing it.
//		   To debug your program, run it with the --sync command line
//		   option to change this behavior. You can then get a meaningful
//		   backtrace from your debugger if you break on the gdk_x_error() function.)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();

		final JTextField tf = new JTextField(30);
		p.add(tf);

		JButton open = new JButton("...");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GtkFileDialog fd = new GtkFileDialog(frame, "My File Dialog");
				fd.setMode(0);
//				fd.setFilenameFilter(createTextFileFilter());
				fd.setVisible(true);

				if (fd.getFile() != null) {
					tf.setText(fd.getFile());
				}
			}
		});
		p.add(open);

		frame.getContentPane().add(p);
		frame.pack();
		frame.setVisible(true);
	}

	FilenameFilter createTextFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// return name.endsWith(".log");
				return name.endsWith(".txt");
			}
		};
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// new GtkFileDialogTest().testAlone();
				new GtkFileDialogTest().testInFrame();
			}
		});

	}
}
