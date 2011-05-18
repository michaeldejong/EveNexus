package nl.minicom.evenexus.gui.validation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.minicom.evenexus.core.report.engine.ReportModelListener;

public class ValidationListener implements KeyListener, ActionListener, ChangeListener, ReportModelListener {

	private List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	public ValidationListener(ValidationRule... rules) {
		for (ValidationRule rule : rules) {
			this.rules.add(rule);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		trigger();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		trigger();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		trigger();
	}

	@Override
	public void onValueChanged() {
		trigger();
	}
	
	public void preTrigger() {
		// Do nothing, allowed to be overridden.
	}
	
	public void postTrigger() {
		// Do nothing, allowed to be overridden.
	}

	public void trigger() {
		preTrigger();
		for (ValidationRule rule : rules) {
			rule.trigger();
		}
		postTrigger();
	}

}
