package nl.minicom.evenexus;

import java.lang.Thread.UncaughtExceptionHandler;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.core.ApplicationModule;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.gui.utils.progresswindows.SplashFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		LOG.info("");
		SplashFrame frame = new SplashFrame();
		frame.buildGui();
		frame.setVisible(true);
		
		LOG.info("Creating Guice injector...");
		frame.update(9, 0, "Creating Guice injector...");
		final Injector injector = Guice.createInjector(new ApplicationModule());
		final Application application = injector.getInstance(Application.class);
		
		LOG.info("Preparing exception handler...");
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error(e.getLocalizedMessage(), e);
				
				BugReportDialog dialog = injector.getInstance(BugReportDialog.class);
				dialog.setFatal(true);
				dialog.setVisible(true);
			}
		});
		
		LOG.info("Launching application...");
		application.initialize(frame, args);
		frame.dispose();
		
		application.initializeGui();
	}
	
	private Main() {
		// Prevent instantiation.
	}
	
}
