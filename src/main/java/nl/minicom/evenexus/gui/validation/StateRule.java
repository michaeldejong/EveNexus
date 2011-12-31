package nl.minicom.evenexus.gui.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link StateRule} class allows the programmer to define what to do if all
 * the attached {@link ValidationRule}s are all valid.
 * 
 * @author michael
 */
public abstract class StateRule {

	private final List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	/**
	 * This constructs a new {@link StateRule} object.
	 * 
	 * @param validationRules
	 * 		The default {@link ValidationRule}s to listen to.
	 */
	public StateRule(ValidationRule... validationRules) {
		for (ValidationRule rule : validationRules) {
			addValidationRule(rule);
		}
	}
	
	/**
	 * This method adds a new {@link ValidationRule} to this {@link StateRule}.
	 * 
	 * @param rule
	 * 		The {@link ValidationRule} to add.
	 */
	public final void addValidationRule(ValidationRule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			rule.addStateRule(this);
		}
	}
	
	/**
	 * This method checks the related {@link ValidationRule}s and triggers this {@link StateRule} if required.
	 */
	public void checkValidationRules() {
		for (ValidationRule rule : rules) {
			if (!rule.isValid()) {
				onValid(false);
				return;
			}
		}
		onValid(true);
	}
	
	/**
	 * @return
	 * 		A {@link List} of the current {@link ValidationRule}s this {@link StateRule} depends on.
	 */
	public List<ValidationRule> getValidationRules() {
		return Collections.unmodifiableList(rules);
	}
	
	/**
	 * This method is called when the linked {@link ValidationRule}s are either ALL valid, or one or more are invalid.
	 * 
	 * @param isValid
	 * 		True if all the linked {@link ValidationRule}s are valid, or false otherwise.
	 */
	public abstract void onValid(boolean isValid);
	
}
