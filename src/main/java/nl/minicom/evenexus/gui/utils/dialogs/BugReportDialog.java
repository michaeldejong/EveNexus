package nl.minicom.evenexus.gui.utils.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import nl.minicom.evenexus.bugreport.BugReporter;
import nl.minicom.evenexus.gui.GuiConstants;

public class BugReportDialog extends CustomDialog {

	private static final long serialVersionUID = -5916859438472895593L;

	private final BugReporter reporter;
	
	@Inject
	public BugReportDialog(BugReporter reporter) {
		super(DialogTitle.BUG_REPORT, 400, 500);
		this.reporter = reporter;
		buildGui();
	}

	@Override
	public void createGui(JPanel guiPanel) {
		JLabel submitterNameLabel = new JLabel("Submitter name (optional):");
		JLabel submitterMailLabel = new JLabel("Submitter e-mail (optional):");
		JLabel bugDescriptionLabel = new JLabel("Bug description (preferred):");

		setLabelOrFieldDimensions(submitterNameLabel);
		setLabelOrFieldDimensions(submitterMailLabel);
		setLabelOrFieldDimensions(bugDescriptionLabel);
		
		JTextField submitterNameField = new JTextField();
		JTextField submitterMailField = new JTextField();
		JTextArea bugDescriptionArea = new JTextArea();
		
		setLabelOrFieldDimensions(submitterNameField);
		setLabelOrFieldDimensions(submitterMailField);
		
		bugDescriptionArea.setBorder(new LineBorder(Color.GRAY));
		bugDescriptionArea.setLineWrap(true);
		bugDescriptionArea.setFont(submitterNameField.getFont());
		bugDescriptionArea.setMargin(new Insets(13, 13, 13, 13));
		
		JButton reportButton = new JButton("Report bug");
		JButton cancelButton = new JButton("Cancel");
		
		setButtonDimensions(reportButton);
		setButtonDimensions(cancelButton);
		
		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.LEADING)
        			.addComponent(submitterNameLabel)
        			.addComponent(submitterNameField)
        			.addComponent(submitterMailLabel)
        			.addComponent(submitterMailField)
        			.addComponent(bugDescriptionLabel)
        			.addComponent(bugDescriptionArea)
        			.addGroup(layout.createSequentialGroup()
    					.addComponent(reportButton)
	    				.addGap(7)
    					.addComponent(cancelButton)
        			)
        		)
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
    			.addComponent(submitterNameLabel)
    			.addComponent(submitterNameField)
	    		.addGap(7)
    			.addComponent(submitterMailLabel)
    			.addComponent(submitterMailField)
    			.addGap(7)
    			.addComponent(bugDescriptionLabel)
    			.addComponent(bugDescriptionArea)
	    		.addGap(7)
	    		.addGroup(layout.createParallelGroup()
    				.addComponent(reportButton)
    				.addComponent(cancelButton)
	    		)
	    		.addGap(7)
    	);
	}
	
	private void setLabelOrFieldDimensions(JComponent component) {
		setDimensions(component, GuiConstants.TEXT_FIELD_HEIGHT);
	}
	
	private void setButtonDimensions(JButton component) {
		setDimensions(component, GuiConstants.BUTTON_HEIGHT);
	}
	
	private void setDimensions(JComponent component, int height) {
		component.setMinimumSize(new Dimension(0, height));
		component.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
	}

}
