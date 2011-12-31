package nl.minicom.evenexus.gui.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ValidationRule} class allows the programmer to define a rule, based on user 
 * input, and trigger certain {@link StateRule}s as a response.
 * 
 * @author michael
 */
public abstract class ValidationRule {

	private static final Logger LOG = LoggerFactory.getLogger(ValidationRule.class);
	
	private final List<StateRule> rules = new ArrayList<StateRule>();
	
	/**
	 * Constructs a new {@link ValidationRule} object.
	 * 
	 * @param stateRules
	 * 		The {@link StateRule}s to trigger.
	 */
	public ValidationRule(StateRule... stateRules) {
		for (StateRule rule : stateRules) {
			addStateRule(rule);
		}
	}
	
	/**
	 * This method adds a new {@link StateRule} to this {@link ValidationRule}.
	 * 
	 * @param rule
	 * 		The {@link StateRule} to add.
	 */
	public final void addStateRule(StateRule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			rule.addValidationRule(this);
		}
	}
	
	/**
	 * This method triggers the validation of this {@link ValidationRule}.
	 */
	public void trigger() {
		for (StateRule rule : rules) {
			rule.checkValidationRules();
		}
	}
	
	/**
	 * @return
	 * 		A {@link List} of linked {@link StateRule}s.
	 */
	public List<StateRule> getStateRules() {
		return Collections.unmodifiableList(rules);
	}
	
	/**
	 * This method needs to be implemented by the programmer. It defines when this {@link ValidationRule} is valid.
	 * 
	 * @return
	 * 		True if this {@link ValidationRule} is in a valid state, or false otherwise.
	 */
	public abstract boolean isValid();
	
	/**
	 * Helper method to check text.
	 * 
	 * @param text
	 * 		The String to check.
	 * 
	 * @return
	 * 		False if the text contains anything but letters and numbers.
	 */
	public static boolean isLetterOrDigit(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;		
	}
	
	/**
	 * Helper method to check the specified text.
	 * 
	 * @param text
	 * 		The message to check.
	 * 
	 * @return
	 * 		True if the specified text, consists only of numbers.
	 */
	public static boolean isDigit(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Helper method which checks the specified text.
	 * 
	 * @param text
	 * 		The {@link String} to check.
	 * 
	 * @param lowerLimit
	 * 		The lower limit.
	 * 
	 * @param upperLimit
	 * 		The upper limit.
	 * 
	 * @return
	 * 		True if the specified text consists of a single number, between the specified limits.
	 */
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
			LOG.error(e.getLocalizedMessage(), e);
		}
		
		return false;
	}
	
	/**
	 * This method checks if the {@link String} is not null and not empty.
	 * 
	 * @param text
	 * 		The {@link String} to check.
	 * 
	 * @return
	 * 		True if the specified {@link String} is not empty.
	 */
	public static boolean isNotEmpty(String text) {
		return text != null && !hasLength(text, 0);
	}

	/**
	 * This method checks if the specified {@link String} has a certain length.
	 * 
	 * @param text
	 * 		The {@link String} to check.
	 * 
	 * @param length
	 * 		The length of the {@link String}.
	 * 
	 * @return
	 * 		True if the specified {@link String} has the specified length.
	 */
	public static boolean hasLength(String text, int length) {
		if (text == null || text.length() != length) {
			return false;
		}
		return true;
	}
	
}
