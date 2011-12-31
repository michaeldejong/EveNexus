package nl.minicom.evenexus.gui.validation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.minicom.evenexus.core.report.engine.ModelListener;

/**
 * The {@link ValidationListener} class implements several Listeners found in Swing and AWT components.
 * The idea is that you can simply attach a {@link ValidationListener} to a component which in turn triggers 
 * {@link StateRule}s.
 *
 * @author michael
 */
public class ValidationListener implements KeyListener, ActionListener, ChangeListener, ModelListener {

	private final List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	/**
	 * Constructs a new {@link ValidationListener}.
	 * 
	 * @param rules
	 * 		The {@link ValidationRule}s to validate.
	 */
	public ValidationListener(ValidationRule... rules) {
		for (ValidationRule rule : rules) {
			this.rules.add(rule);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// Do nothing.
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		trigger();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Do nothing.
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

	@Override
	public void onStateChanged() {
		trigger();
	}
	
	/**
	 * This method triggers all {@link ValidationRule}s and tells them to validate themselves.
	 */
	public void trigger() {
		for (ValidationRule rule : rules) {
			rule.trigger();
		}
	}

}
