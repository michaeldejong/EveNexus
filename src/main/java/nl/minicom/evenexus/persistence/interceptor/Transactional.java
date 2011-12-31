package nl.minicom.evenexus.persistence.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on methods in objects constructed by Guice.
 * The TransactionalInterceptor prepares an Session which can then be used within
 * the method. When exiting the method the Session is automatically flushed and closed.
 *
 * @author michael
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
	
}
