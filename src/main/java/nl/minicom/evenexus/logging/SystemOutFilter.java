package nl.minicom.evenexus.logging;

import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;


/**
 * This class is responsible for filtering {@link LoggingEvent}s according to their {@link Level}.
 *
 * @author michael
 */
public class SystemOutFilter extends AbstractMatcherFilter<Object> {

	@Override
	public FilterReply decide(Object event) {
		if (!isStarted()) {
			return FilterReply.NEUTRAL;
		}

		LoggingEvent loggingEvent = (LoggingEvent) event;
		List<Level> eventsToKeep = Arrays.asList(Level.TRACE, Level.DEBUG, Level.INFO);
		
		if (eventsToKeep.contains(loggingEvent.getLevel())) {
			return FilterReply.ACCEPT;
		} 
		else {
			return FilterReply.DENY;
		}
	}

}
