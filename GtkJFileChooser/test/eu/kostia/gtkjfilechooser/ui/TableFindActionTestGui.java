package eu.kostia.gtkjfilechooser.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class TableFindActionTestGui extends JFrame {
	private JTable jTable;
	private boolean cellEditable = false;


	public TableFindActionTestGui() {
		jTable = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Cell editable ONLY when double-clicked
				return cellEditable;
			}
		};

		jTable.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if ( e.getClickCount() == 2 )
				{
					cellEditable = true;
					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint( e.getPoint() );
					int column = source.columnAtPoint( e.getPoint() );
					source.editCellAt(row, column);
					DefaultCellEditor editor = (DefaultCellEditor)source.getCellEditor();
					JTextField textField = (JTextField)editor.getComponent();
					textField.requestFocusInWindow();
					textField.selectAll();
					cellEditable = false;
				}
			}
		});


		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jTable.setModel(new DefaultTableModel(new Object[][] {
				{ "Costantino", "Cerbo", "Napoli" },
				{ "Christiane", "Oellig", "Stuttgart" }, 
				{ "Davide", "Cerbo", "Roma" },
				{ "Federico", "Cerbo", "Salerno" } }, 

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
