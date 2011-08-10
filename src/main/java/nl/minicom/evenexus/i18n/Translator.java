package nl.minicom.evenexus.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
	
	private ResourceBundle bundle;
	
	public Translator() {
	}
	
	public void initialize(Locale locale) {
		bundle = ResourceBundle.getBundle("translations", locale);
	}
	
	public String translate(String key) {
		return bundle.getString(key);
	}

}
