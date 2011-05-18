package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

public abstract class ReportBuilderPage extends JPanel {

	private static final long serialVersionUID = -518048166321128035L;
	
	public ReportBuilderPage() {
		setDimensions();
	}
	
	protected final void setDimensions() {	
		setMinimumSize(new Dimension(0, 0));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	public abstract DialogTitle getTitle();

	protected JLabel createBoldLabel(String value) {
		JLabel label = new JLabel(value);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setForeground(Color.BLACK);
		label.setMinimumSize(new Dimension(0, GuiConstants.COMBO_BOX_HEIGHT));
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		return label;
	}

}
