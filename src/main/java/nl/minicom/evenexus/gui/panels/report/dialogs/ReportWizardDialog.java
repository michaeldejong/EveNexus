package nl.minicom.evenexus.gui.panels.report.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ModelListener;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.panels.report.ReportPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportDisplayPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportFiltersPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportGroupingPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportItemsPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportWizardPage;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.titles.ReportItemTitle;

/**
 * This dialog allows the user to (re)-define a ReportModel object
 * in several steps.
 * 
 * @author michael
 */
public class ReportWizardDialog extends CustomDialog implements ModelListener {

	private static final long serialVersionUID = 5707542816331260941L;
	
	private final JPanel contentPanel;
	private final ReportWizardPage[] pages;
	private final ReportModel reportModel;
	private final ReportPanel reportPanel;
	
	private final JButton prev;
	private final JButton next;
	private final JButton execute;
	
	private int index = 0;
	
	/**
	 * This constructs a new {@link ReportWizardDialog}.
	 * 
	 * @param items
	 * 		A {@link ReportItemsPage} object.
	 * 
	 * @param groups
	 * 		A {@link ReportGroupingPage} object.
	 * 
	 * @param filters
	 * 		A {@link ReportFiltersPage} object.
	 * 
	 * @param displays
	 * 		A {@link ReportDisplayPage} object.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel}.
	 * 
	 * @param reportPanel
	 * 		The {@link ReportPanel}.
	 */
	@Inject
	public ReportWizardDialog(ReportItemsPage items, ReportGroupingPage groups,
			ReportFiltersPage filters, ReportDisplayPage displays, ReportModel reportModel,
			ReportPanel reportPanel) {
		
		super(new ReportItemTitle(), 360, 420);
		this.contentPanel = new JPanel();
		this.pages = new ReportWizardPage[] { items, groups, filters, displays };
		this.reportModel = reportModel;
		this.reportPanel = reportPanel;

		this.prev = createButton("< Previous");
		this.next = createButton("Next >");
		this.execute = createButton("Execute");
	}
	
	/**
	 * This method initializes the {@link ReportWizardDialog}.
	 */
	public void initialize() {
		reportModel.addListener(this);
		
		for (ReportWizardPage page : pages) {
			page.buildGui();
		}
		
		setTitle("Report creation wizard");
		buildGui();
		displayPage();
		setVisible(true);
	}

	@Override
	public void createGui(JPanel guiPanel) {
		JPanel buttonBar = createButtonBar();
		
		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);
		layout.setHorizontalGroup(
			  	layout.createParallelGroup()
			  	.addComponent(contentPanel)
			  	.addComponent(buttonBar)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(contentPanel)
			  	.addComponent(buttonBar)
		);
	}

	private JPanel createButtonBar() {
		
		prev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index--;
				displayPage();
			}
		});
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index++;
				displayPage();
			}
		});
		
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reportPanel.displayReport(reportModel);
				dispose();
			}
		});
		
		JPanel buttonBar = new JPanel();
		GroupLayout layout = new GroupLayout(buttonBar);
		buttonBar.setLayout(layout);
		layout.setHorizontalGroup(
			  	layout.createSequentialGroup()
			  	.addGap(14)
			  	.addComponent(prev)
			  	.addGap(7)
			  	.addComponent(next)
			  	.addGap(7)
			  	.addComponent(execute)
			  	.addGap(14)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(5)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(prev)
						.addComponent(next)
						.addComponent(execute)
				)
		);
		
		return buttonBar;
	}

	private JButton createButton(String content) {
		JButton button = new JButton(content);
		button.setMinimumSize(new Dimension(100, GuiConstants.BUTTON_HEIGHT));
		button.setMaximumSize(new Dimension(100, GuiConstants.BUTTON_HEIGHT));
		return button;
	}
	
	/**
	 * This method updates the states of the buttons.
	 */
	protected void updateButtonStates() {
		prev.setEnabled(index > 0);
		next.setEnabled(getCurrentPage().allowNext() && index < pages.length - 1);
		execute.setEnabled(reportModel.isValid());
	}

	private void displayPage() {
		contentPanel.removeAll();
		
		GroupLayout layout = new GroupLayout(contentPanel);
		contentPanel.setLayout(layout);
		layout.setHorizontalGroup(
			  	layout.createSequentialGroup()
			  	.addComponent(getCurrentPage())
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(getCurrentPage())
		);

		updateButtonStates();
	}
	
	private ReportWizardPage getCurrentPage() {
		return pages[index];
	}
	
	/**
	 * This method overrides the default dispose method. It will first 
	 * de-register itself as a {@link ModelListener} from the {@link ReportModel}.
	 */
	@Override
	public void dispose() {
		reportModel.removeListener(this);
		super.dispose();
	}

	@Override
	public void onValueChanged() {
		updateButtonStates();
	}

	@Override
	public void onStateChanged() {
		updateButtonStates();
	}
	
}
