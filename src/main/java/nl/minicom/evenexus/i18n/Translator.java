package nl.minicom.evenexus.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The {@link Translator} class is responsible for delivering translations 
 * of a certain key in a specified {@link Locale}.
 * 
 * @author michael
 */
public class Translator {
	
	private ResourceBundle bundle;
	
	/**
	 * This method initializes the {@link Translator} with a certain locale.
	 * 
	 * @param locale
	 * 		The {@link Locale} to use.
	 */
	public void initialize(Locale locale) {
		bundle = ResourceBundle.getBundle("translations", locale);
	}
	
	/**
	 * This method retrieves the translation of a certain key.
	 * 
	 * @param key
	 * 		The key of the word to look for.
	 * 
	 * @return
	 * 		The translation associated with the key.
	 */
	public String translate(String key) {
		return bundle.getString(key);
	}

}
