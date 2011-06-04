package nl.minicom.evenexus.gui;


import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.panels.accounts.AccountsPanel;
import nl.minicom.evenexus.gui.panels.dashboard.DashboardPanel;
import nl.minicom.evenexus.gui.panels.journals.JournalsPanel;
import nl.minicom.evenexus.gui.panels.marketorders.MarketOrdersPanel;
import nl.minicom.evenexus.gui.panels.profit.ProfitPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.ReportPanel;
import nl.minicom.evenexus.gui.panels.transactions.TransactionsPanel;
import nl.minicom.evenexus.gui.settings.SettingsDialog;
import nl.minicom.evenexus.gui.utils.GuiListener;
import nl.minicom.evenexus.gui.utils.dialogs.AboutDialog;
import nl.minicom.evenexus.gui.utils.dialogs.ExportDatabaseDialog;
import nl.minicom.evenexus.gui.utils.dialogs.ImportDatabaseDialog;
import nl.minicom.evenexus.gui.utils.progresswindows.SplashFrame;
import nl.minicom.evenexus.utils.SettingsManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Gui extends JFrame {

	private static final long serialVersionUID = 4615845773792192362L;
	
	private static final Logger logger = LogManager.getRootLogger();
	
	private static final int MIN_HEIGHT = 400;
	private static final int MIN_WIDTH = 700;
	private static final int HEIGHT = 550;
	private static final int WIDTH = 850;
	
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
		Application application = new Application();
		
		SplashFrame splashFrame = new SplashFrame();
		splashFrame.setVisible(true);		
		application.initialize(splashFrame, args);
		splashFrame.setVisible(false);		

		new Gui(application);
	}
	
	private final Application application;
	
	public Gui(Application application) {
		super();
		this.application = application;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(getClass().getPackage().getSpecificationTitle() + " - EVE Online trading overview");
		setIconImage(Icon.getImage("logo_128x128.png"));
		setLookAndFeel();
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPos = (int) (env.getMaximumWindowBounds().getWidth() - WIDTH) / 2;
		int yPos = (int) (env.getMaximumWindowBounds().getHeight() - HEIGHT) / 2;
		
		int x = application.getSettingsManager().loadInt(SettingsManager.APPLICATION_X, xPos);
		int y = application.getSettingsManager().loadInt(SettingsManager.APPLICATION_Y, yPos);
		int width = application.getSettingsManager().loadInt(SettingsManager.APPLICATION_WIDTH, WIDTH);
		int height = application.getSettingsManager().loadInt(SettingsManager.APPLICATION_HEIGHT, HEIGHT);
		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		setBounds(x, y, width, height);
		
		if (application.getSettingsManager().loadBoolean(SettingsManager.APPLICATION_MAXIMIZED, false)) {
			setExtendedState(MAXIMIZED_BOTH);
		}
		
		GuiListener guiListener = new GuiListener(this, application.getSettingsManager());
		addComponentListener(guiListener);
		addWindowStateListener(guiListener);
		addWindowListener(guiListener);
		
		createGUI();
		setVisible(true);		
	}

	public final Application getApplication() {
		return application;
	}

	private void createGUI() {
		setJMenuBar(createMenu());
		
		JTabbedPane pane = new JTabbedPane();
		pane.setFocusable(false);
		
		pane.addTab("Dashboard", new DashboardPanel(application));
		pane.addTab("Reports", new ReportPanel(application));
		pane.addTab("Journals", new JournalsPanel(application));
		pane.addTab("Transactions", new TransactionsPanel(application));
		pane.addTab("Market orders", new MarketOrdersPanel(application));
		pane.addTab("Profits", new ProfitPanel(application));
		pane.addTab("Accounts", new AccountsPanel(application));
				
		GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addComponent(pane)
        		.addGap(6)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(7)
	    		.addComponent(pane)
	    		.addGap(7)
    	);
	}

	private JMenuBar createMenu() {
		JMenuBar bar = new JMenuBar();
		
		JMenu applicationMenu = new JMenu("Application");
		
		JMenuItem importMenu = new JMenuItem("Import database", Icon.getIcon("database_down_16x16.png"));
		applicationMenu.add(importMenu);
		importMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ImportDatabaseDialog();
			}
		});
		
		JMenuItem exportMenu = new JMenuItem("Export database", Icon.getIcon("database_next_16x16.png"));
		applicationMenu.add(exportMenu);
		exportMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ExportDatabaseDialog();
			}
		});
		
		applicationMenu.addSeparator();
		
		final SettingsDialog settingsDialog = new SettingsDialog(application);
		
		JMenuItem proxyMenu = new JMenuItem("Settings", Icon.getIcon("process_16x16.png"));
		applicationMenu.add(proxyMenu);
		proxyMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settingsDialog.setVisible(true);
			}
		});
		
		applicationMenu.addSeparator();
		
		JMenuItem exitMenu = new JMenuItem("Exit", Icon.getIcon("remove_16x16.png"));
		applicationMenu.add(exitMenu);
		exitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		bar.add(applicationMenu);
		
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem aboutMenu = new JMenuItem("About", Icon.getIcon("info_16x16.png"));
		helpMenu.add(aboutMenu);
		aboutMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AboutDialog(application);
			}
		});
		
		bar.add(helpMenu);
		
		return bar;
	}

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}
	
}
