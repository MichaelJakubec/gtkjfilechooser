package eu.kostia.gtkjfilechooser.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class TableFindActionTestGui extends JFrame {
	private JTable jTable;

	public TableFindActionTestGui() {
		jTable = new JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
				{ "Costantino", "Cerbo", "Napoli" },
				{ "Christiane", "Oellig", "Stuttgart" }, 
				{ "Davide", "Cerbo", "Roma" },
				{ "Federico", "Cerbo", "Salerno" } 
		}, 
		new String[] { "Nome", "Cognome", "Cittá" }));

		new TableFindAction().install(jTable);
		getContentPane().add(new JScrollPane(jTable));
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		TableFindActionTestGui test = new TableFindActionTestGui();
		test.pack();
		test.setVisible(true);


	}

}
