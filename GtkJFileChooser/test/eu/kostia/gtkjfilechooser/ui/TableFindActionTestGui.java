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

import eu.kostia.gtkjfilechooser.Log;

public class TableFindActionTestGui extends JFrame {
	private JTable table;
	private boolean cellEditable = false;


	public TableFindActionTestGui() {
		table = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Cell editable ONLY when double-clicked
				return cellEditable;
			}
		};

		table.addMouseListener( new MouseAdapter()
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

		table.setModel(new DefaultTableModel(new Object[][] {
				{ "Costantino", "Cerbo", "Napoli" },
				{ "Christiane", "Oellig", "Stuttgart" }, 
				{ "Davide", "Cerbo", "Roma" },
				{ "Federico", "Cerbo", "Salerno" } }, 

				new String[] { "Nome", "Cognome", "Cittá" }));

		Log.debug(table.getSelectionModel().getClass());
		new TableFindAction().install(table);
		getContentPane().add(new JScrollPane(table));
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());

		TableFindActionTestGui test = new TableFindActionTestGui();
		test.pack();
		test.setVisible(true);

	}






}
