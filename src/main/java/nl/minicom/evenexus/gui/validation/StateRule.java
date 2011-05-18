package nl.minicom.evenexus.gui.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class StateRule {

	private List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	public StateRule(ValidationRule... validationRules) {
		for (ValidationRule rule : validationRules) {
			addValidationRule(rule);
		}
	}
	
	public void addValidationRule(ValidationRule rule) {
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
	
	public void onValid(boolean isValid) {};
	
}
