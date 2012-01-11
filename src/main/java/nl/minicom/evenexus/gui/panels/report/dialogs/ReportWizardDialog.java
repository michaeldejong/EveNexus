package nl.minicom.evenexus.gui.panels.report.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.panels.report.ReportPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ModificationListener;
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
public class ReportWizardDialog extends CustomDialog implements ModificationListener {

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
	public ReportWizardDialog(ReportPanel reportPanel, ReportItemsPage items, ReportGroupingPage groups,
			ReportFiltersPage filters, ReportDisplayPage displays, ReportModel reportModel) {
		
		super(new ReportItemTitle(), 360, 520);
		this.contentPanel = new JPanel();
		this.pages = new ReportWizardPage[] { items, groups, filters, displays };
		this.reportModel = reportModel;
		this.reportPanel = reportPanel;

		this.prev = createButton("< Previous");
		this.next = createButton("Next >");
		this.execute = createButton("Execute");
		
		reportModel.addListener(this);
	}

	/**
	 * This method initializes the {@link ReportWizardDialog}.
	 */
	public void initialize() {
		index = 0;
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
				getCurrentPage().removeListeners();
				index--;
				displayPage();
			}
		});
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPage().removeListeners();
				index++;
				displayPage();
			}
		});
		
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPage().removeListeners();
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
		
		onModification();
	}
	
	private ReportWizardPage getCurrentPage() {
		return pages[index];
	}
	
	@Override
	public void dispose() {
		super.dispose();
		reportModel.removeListener(this);
	}

	@Override
	public void onModification() {
		ReportWizardPage currentPage = getCurrentPage();
		prev.setEnabled(currentPage.allowPrevious());
		next.setEnabled(currentPage.allowNext());
		execute.setEnabled(currentPage.allowExecute());
	}
	
}
