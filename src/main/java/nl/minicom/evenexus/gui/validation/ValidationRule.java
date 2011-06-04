package nl.minicom.evenexus.gui.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public abstract class ValidationRule {

	private List<StateRule> rules = new ArrayList<StateRule>();
	
	private static final Logger logger = LogManager.getRootLogger();
	
	public ValidationRule(StateRule... stateRules) {
		for (StateRule rule : stateRules) {
			addStateRule(rule);
		}
	}
	
	public void addStateRule(StateRule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			rule.addValidationRule(this);
		}
	}
	
	public void trigger() {
		for (StateRule rule : rules) {
			rule.checkValidationRules();
		}
	}
	
	public List<StateRule> getStateRules() {
		return Collections.unmodifiableList(rules);
	}
	
	public abstract boolean isValid();
	
	public static boolean isLetterOrDigit(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;		
	}
	
	public static boolean isDigit(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
		
	public static boolean isBoundDigit(String text, int lowerLimit, int upperLimit) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}

		try {
			int value = Integer.parseInt(text);
			if (value >= lowerLimit && value <= upperLimit) {
				return true;
			}
		}
		catch (NumberFormatException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		
		return false;
	}
	
	public static boolean isNotEmpty(String text) {
		return text != null && !hasLength(text, 0);
	}

	public static boolean hasLength(String text, int length) {
		if (text == null || text.length() != length) {
			return false;
		}
		return true;
	}
	
}
