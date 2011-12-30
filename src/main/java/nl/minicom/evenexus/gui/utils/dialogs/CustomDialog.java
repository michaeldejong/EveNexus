package nl.minicom.evenexus.gui.utils.dialogs;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.utils.GradientPanel;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;

/**
 * This class represents a dialog which is drawn in the GUI.
 *
 * @author michael
 */
public abstract class CustomDialog extends JDialog {

	private static final long serialVersionUID = 3922331594697041683L;
	
	private final JPanel guiPanel;
	private final GradientPanel dialogPanel;
	
	/**
	 * This constructs a new {@link CustomDialog} object.
	 * 
	 * @param dialogTitle
	 * 		The title of this dialog.
	 * 
	 * @param width
	 * 		The width of this dialog.
	 * 
	 * @param height
	 * 		The height of this dialog.
	 */
	public CustomDialog(DialogTitle dialogTitle, int width, int height) {
		super();
		this.dialogPanel = new GradientPanel();
		this.guiPanel = new JPanel();
		createTitle(dialogTitle);
		
		setResizable(false);
		setTitle(dialogTitle.getTitle());
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setIconImage(Icon.getImage(Icon.LOGO));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPosition = (int) (env.getMaximumWindowBounds().getWidth() - width) / 2;
		int yPosition = (int) (env.getMaximumWindowBounds().getHeight() - height) / 2;
		setBounds(xPosition, yPosition, width, height);
		Gui.setLookAndFeel();
	}
	
	/**
	 * This method removes all elements from this {@link CustomDialog}.
	 */
	protected void clearComponents() {
		for (Component component : guiPanel.getComponents()) {
			guiPanel.remove(component);
		}
	}

	/**
	 * This method builds the GUI.
	 */
	public void buildGui() {
		clearComponents();
		
		createGui(guiPanel);
		
		GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    			.addComponent(dialogPanel)
    			.addGroup(layout.createSequentialGroup()
    		        .addGap(7)
    				.addComponent(guiPanel)
    	        	.addGap(7)
    			)
    		)
		);
        layout.setVerticalGroup(
    		layout.createSequentialGroup()
        	.addComponent(dialogPanel)
        	.addGap(7)
    		.addComponent(guiPanel)
    		.addGap(7)
        );
        setLayout(layout); 
	}
	
	/**
	 * This method sets the specified {@link DialogTitle} as this {@link CustomDialog}'s title.
	 * 
	 * @param title
	 * 		The new {@link DialogTitle} of this {@link CustomDialog}.
	 */
	public final void createTitle(DialogTitle title) {
		createTitle(title, dialogPanel);
	}

	private void createTitle(DialogTitle title, GradientPanel panel) {	
		panel.removeAll();

		MatteBorder outsideBorder = new MatteBorder(0, 0, 1, 0, Color.WHITE);
		MatteBorder insideBorder = new MatteBorder(0, 0, 1, 0, Color.GRAY);
		panel.setBorder(new CompoundBorder(outsideBorder, insideBorder));
		
		panel.setMinimumSize(new Dimension(0, 70));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		
		if (title.getSubIcon() != null) {
			JLabel subIcon = new JLabel(title.getSubIcon());
			subIcon.setBounds(27, 30, 32, 32);
			panel.add(subIcon);

			JLabel icon = new JLabel(title.getIcon());
			icon.setBounds(9, 7, 48, 48);
			panel.add(icon);
		}
		else {
			JLabel icon = new JLabel(title.getIcon());
			icon.setBounds(9, 9, 48, 48);
			panel.add(icon);
		}
		
		String htmlTitle = String.format("<html><b>%s</b><br>%s</html>", title.getTitle(), title.getDescription());
		JLabel titleLabel = new JLabel(htmlTitle);
		titleLabel.setBounds(67, 0, 450, 70);
		panel.add(titleLabel);
	}
	
	/**
	 * This method is called when creating the inner gui elements of the {@link CustomDialog}.
	 * 
	 * @param guiPanel
	 * 		The {@link JPanel} to draw on.
	 */
	public abstract void createGui(JPanel guiPanel);
	
	/**
	 * @return
	 * 		The {@link JPanel} to draw on.
	 */
	public JPanel getGuiPanel() {
		return guiPanel;
	}

}
