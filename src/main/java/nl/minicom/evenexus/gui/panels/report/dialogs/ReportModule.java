package nl.minicom.evenexus.gui.panels.report.dialogs;

import javax.inject.Singleton;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;

import com.google.inject.AbstractModule;

public class ReportModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ReportDefinition.class).in(Singleton.class);
		
	}

}
