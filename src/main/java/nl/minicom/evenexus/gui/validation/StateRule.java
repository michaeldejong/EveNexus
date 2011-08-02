package nl.minicom.evenexus.gui.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class StateRule {

	private final List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	public StateRule(ValidationRule... validationRules) {
		for (ValidationRule rule : validationRules) {
			addValidationRule(rule);
		}
	}
	
	public final void addValidationRule(ValidationRule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			rule.addStateRule(this);
		}
	}
	
	public void checkValidationRules() {
		for (ValidationRule rule : rules) {
			if (!rule.isValid()) {
				onValid(false);
				return;
			}
		}
		onValid(true);
	}
	
	public List<ValidationRule> getValidationRules() {
		return Collections.unmodifiableList(rules);
	}
	
	public abstract void onValid(boolean isValid);
	
}
