package sun.awt.X11;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel p = new JPanel();

		final JTextField tf = new JTextField(30);
		p.add(tf);

		JButton open = new JButton("...");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GtkFileDialog fd = new GtkFileDialog(frame, "My File Dialog");
				fd.setMode(0);
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
