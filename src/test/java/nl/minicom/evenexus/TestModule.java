package nl.minicom.evenexus;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.persistence.interceptor.TransactionalInterceptor;

import org.junit.Ignore;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

@Ignore
public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
		Database database = new Database();
		bind(Database.class).toInstance(database);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), 
				new TransactionalInterceptor(database));
	}

}
