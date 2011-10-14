package nl.minicom.evenexus.gui.utils.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
import nl.minicom.evenexus.gui.utils.dialogs.titles.BugReportTitle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This dialog allows the user to report a bug to the GitHub repo.
 *
 * @author michael
 */
public class BugReportDialog extends CustomDialog {

	private static final long serialVersionUID = -5916859438472895593L;

	private static final Logger LOG = LoggerFactory.getLogger(BugReportDialog.class);

	private final BugReporter reporter;

	private JTextField submitterNameField;
	private JTextField submitterMailField;
	private JTextArea bugDescriptionArea;
	private boolean fatal = false;

	/**
	 * This constructs a new {@link BugReportDialog} object.
	 * 
	 * @param reporter
	 * 		The {@link BugReporter}.
	 */
	@Inject
	public BugReportDialog(BugReporter reporter) {
		super(new BugReportTitle(), 400, 500);
		this.reporter = reporter;
		
		setModal(true);
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

		submitterNameField = new JTextField();
		submitterMailField = new JTextField();
		bugDescriptionArea = new JTextArea();

		setLabelOrFieldDimensions(submitterNameField);
		setLabelOrFieldDimensions(submitterMailField);

		bugDescriptionArea.setBorder(new LineBorder(Color.GRAY));
		bugDescriptionArea.setLineWrap(true);
		bugDescriptionArea.setFont(submitterNameField.getFont());
		bugDescriptionArea.setMargin(new Insets(13, 13, 13, 13));

		final JButton reportButton = new JButton("Report bug");
		JButton cancelButton = new JButton("Cancel");

		setButtonDimensions(reportButton);
		setButtonDimensions(cancelButton);

		reportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reportButton.setEnabled(false);
				new Thread() {
					@Override
					public void run() {
						try {
							createReport();
							
							if (fatal) {
								System.exit(1);
							}
						} 
						catch (Exception e1) {
							LOG.warn(e1.getLocalizedMessage(), e1);
						}
					}
				}.start();
				setVisible(false);
				reportButton.setEnabled(true);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
				if (fatal) {
					System.exit(1);
				}
			}
		});

		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGap(7)
				.addGroup(
						layout.createParallelGroup(Alignment.LEADING)
								.addComponent(submitterNameLabel)
								.addComponent(submitterNameField)
								.addComponent(submitterMailLabel)
								.addComponent(submitterMailField)
								.addComponent(bugDescriptionLabel)
								.addComponent(bugDescriptionArea)
								.addGroup(
										layout.createSequentialGroup()
												.addComponent(reportButton)
												.addGap(7)
												.addComponent(cancelButton)))
				.addGap(7));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
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
				.addGroup(
						layout.createParallelGroup().addComponent(reportButton)
								.addComponent(cancelButton)).addGap(7));
	}

	private void createReport() throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("\n*Submitter name:* ");
		String name = submitterNameField.getText();
		if (name == null || name.isEmpty()) {
			builder.append("unknown");
		} else {
			builder.append(name);
		}

		builder.append("\n*Submitter mail:* ");
		String mail = submitterMailField.getText();
		if (mail == null || mail.isEmpty()) {
			builder.append("unknown");
		} else {
			builder.append(mail);
		}

		builder.append("\n\n*Bug description:* \n");
		builder.append(bugDescriptionArea.getText() + "\n");

		builder.append("\n*Full log:*\n");
		builder.append("<pre>" + readLog() + "</pre>");

		reporter.createNewIssue("EveNexus bug report", builder.toString());
	}

	private String readLog() throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader("log.txt"));

		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line + "\n");
		}

		reader.close();
		return builder.toString();
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

	/**
	 * This method notifies this {@link BugReportDialog} that the encountered bug was fatal.
	 * 
	 * @param fatal
	 * 		True if the encountered bug was fatal, or false if it was not.
	 */
	public void setFatal(boolean fatal) {
		this.fatal  = fatal;
	}

}
