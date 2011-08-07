package nl.minicom.evenexus.gui;

import javax.inject.Singleton;

import nl.minicom.evenexus.gui.panels.accounts.AccountsPanel;
import nl.minicom.evenexus.gui.panels.dashboard.DashboardPanel;
import nl.minicom.evenexus.gui.panels.journals.JournalsPanel;
import nl.minicom.evenexus.gui.panels.marketorders.MarketOrdersPanel;
import nl.minicom.evenexus.gui.panels.profit.ProfitPanel;
import nl.minicom.evenexus.gui.panels.transactions.TransactionsPanel;
import nl.minicom.evenexus.gui.settings.ApiServerTab;
import nl.minicom.evenexus.gui.settings.ProxyTab;
import nl.minicom.evenexus.gui.settings.SettingsDialog;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.renderers.CurrencyRenderer;
import nl.minicom.evenexus.gui.tables.renderers.DateTimeRenderer;
import nl.minicom.evenexus.gui.tables.renderers.IntegerRenderer;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;

import com.google.inject.AbstractModule;

public class GuiModule extends AbstractModule {

	@Override
	protected void configure() {
		// Dialogs
		bind(BugReportDialog.class).in(Singleton.class);
		bind(SettingsDialog.class).in(Singleton.class);
		bind(ApiServerTab.class).in(Singleton.class);
		bind(ProxyTab.class).in(Singleton.class);
		
		// Renderers
		bind(CurrencyRenderer.class).in(Singleton.class);
		bind(IntegerRenderer.class).in(Singleton.class);
		bind(DateTimeRenderer.class).in(Singleton.class);
		
		// Panels
		bind(AccountsPanel.class).in(Singleton.class);
		bind(DashboardPanel.class).in(Singleton.class);
		bind(JournalsPanel.class).in(Singleton.class);
		bind(MarketOrdersPanel.class).in(Singleton.class);
		bind(ProfitPanel.class).in(Singleton.class);
		bind(TransactionsPanel.class).in(Singleton.class);
		
		// Utils
		requestStaticInjection(ColumnModel.class);
	}

}
