package nl.minicom.evenexus.gui.tables.columns;


import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;

public abstract class ColumnSelectionFrame extends JDialog {

	private static final long serialVersionUID = 4091649495384411402L;
	
	private final JTable columnTable;
	
	public ColumnSelectionFrame() {
		super();		
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);

		this.columnTable = new JTable();
		setTitle("Show / hide columns");
		setIconImage(Icon.getImage("/img/other/logo.png"));
		Gui.setLookAndFeel();
		setBounds(300, 200, 300, 380);
		setResizable(false);
	}

	public void createGUI() {
		columnTable.setRowHeight(23);		
		columnTable.getTableHeader().setReorderingAllowed(false);
		columnTable.setFillsViewportHeight(true);
		columnTable.setSelectionBackground(new Color(51, 153, 255));
		columnTable.setGridColor(new Color(223, 223, 223));
		columnTable.setModel(createTableModel());
		columnTable.setFocusable(false);
		
		columnTable.getColumn("Column").setPreferredWidth(120);
		columnTable.getColumn("Visible").setPreferredWidth(50);
		
		JScrollPane scrollPane = new JScrollPane(columnTable);
		
		JButton confirm = new JButton("Confirm");
		confirm.setMinimumSize(new Dimension(90, 32));
		confirm.setMaximumSize(new Dimension(90, 32));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				columnTable.getSelectionModel().clearSelection();
			}
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        			.addComponent(scrollPane)
        			.addComponent(confirm)
        		)
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(7)
    			.addComponent(scrollPane)
	    		.addGap(7)
    			.addComponent(confirm)
	    		.addGap(7)
    	);
	}

	protected abstract TableModel createTableModel();

	public void setVisible() {
		setVisible(true);
	}

}
