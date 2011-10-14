package nl.minicom.evenexus.gui.utils.toolbar;


import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;

public class ToolBarButton extends JButton {

	private static final long serialVersionUID = -7696380876971354114L;
	
	public ToolBarButton(String icon, String tooltip) {
		super(Icon.getIcon(icon));
		setToolTipText(tooltip);
        setFocusable(false);
		setBackground(GuiConstants.getTabBackground());
        setMinimumSize(new Dimension(48, 48));
        setMaximumSize(new Dimension(48, 48));

		hoverOver(false);
        addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// Do nothing.
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// Do nothing.
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// Do nothing.
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				hoverOver(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hoverOver(true);
			}
			
		});
	}
	
	private void hoverOver(boolean hoverOver) {
		setContentAreaFilled(hoverOver);
		setRolloverEnabled(hoverOver);
	}
	
}
