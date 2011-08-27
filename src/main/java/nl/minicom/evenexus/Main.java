package nl.minicom.evenexus;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.core.ApplicationModule;
import nl.minicom.evenexus.gui.utils.progresswindows.SplashFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		LOG.info("");
		SplashFrame frame = new SplashFrame();
		frame.buildGui();
		frame.setVisible(true);
		
		LOG.info("Creating Guice injector...");
		frame.update(9, 0, "Creating Guice injector...");
		Injector injector = Guice.createInjector(new ApplicationModule());
		Application application = injector.getInstance(Application.class);
		
		LOG.info("Launching application...");
		application.initialize(frame, args);
		frame.dispose();
		
		application.initializeGui();
	}
	
}
