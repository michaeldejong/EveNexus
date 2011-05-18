package nl.minicom.evenexus.gui.panels.report.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.engine.ReportModelListener;
import nl.minicom.evenexus.gui.GuiConstants;

public class ReportBuilderPageNavigationPanel extends JPanel {

	private static final long serialVersionUID = -1529645762801745132L;
	
	private final ReportBuilderPagePanel pageDisplay;
	private final ReportModel reportModel;
	
	private final JButton previousButton;
	private final JButton nextButton;
	private final JButton executeButton;

	public ReportBuilderPageNavigationPanel(ReportBuilderPagePanel displayPanel, ReportModel model) {
		this.pageDisplay = displayPanel;
		this.reportModel = model;
		
		previousButton = createNavigationButton("< Previous", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pageDisplay.showPreviousPage();
				updateButtonStates();
			}
		});
		
		nextButton = createNavigationButton("Next >", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pageDisplay.showNextPage();
				updateButtonStates();
			}
		});
		
		executeButton = createNavigationButton("Execute", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: execute the report.
			}
		});
		
		model.addListener(new ReportModelListener() {
			@Override
			public void onValueChanged() {
				executeButton.setEnabled(reportModel.isValid());
			}
		});
		
		updateButtonStates();
					
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		        		.addComponent(previousButton)
		        		.addGap(6)
		        		.addComponent(nextButton)
		        		.addGap(6)
		        		.addComponent(executeButton)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
	    				.addGap(6)
	    				.addGroup(layout.createParallelGroup()
	    						.addComponent(previousButton)
	    						.addComponent(nextButton)
	    						.addComponent(executeButton)
	    				)
    	);
	}
	
	private JButton createNavigationButton(String caption, ActionListener action) {
		JButton button = new JButton();
		button.setText(caption);		
		button.setMinimumSize(new Dimension(80, GuiConstants.BUTTON_HEIGHT));
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.BUTTON_HEIGHT));
		button.addActionListener(action);
		return button;
	}
	
	private void updateButtonStates() {
		previousButton.setEnabled(pageDisplay.hasPreviousPage());
		nextButton.setEnabled(pageDisplay.hasNextPage());
	}
	
}
