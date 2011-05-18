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

public abstract class CustomDialog extends JDialog {

	private static final long serialVersionUID = 3922331594697041683L;
	
	private final JPanel guiPanel;
	private GradientPanel dialogPanel;
	
	public CustomDialog(DialogTitle dialogTitle, int width, int height) {
		super();
		this.dialogPanel = new GradientPanel();
		this.guiPanel = new JPanel();
		createTitle(dialogTitle);
		
		setResizable(false);
		setTitle(dialogTitle.getTitle());
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setIconImage(Icon.getImage("logo_128x128.png"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPosition = (int) (env.getMaximumWindowBounds().getWidth() - width) / 2;
		int yPosition = (int) (env.getMaximumWindowBounds().getHeight() - height) / 2;
		setBounds(xPosition, yPosition, width, height);
		Gui.setLookAndFeel();
	}
	
	protected void clearComponents() {
		for (Component component : guiPanel.getComponents()) {
			guiPanel.remove(component);
		}
	}

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
	
	public final void createTitle(DialogTitle title) {
		createTitle(title, dialogPanel);
	}

	private final void createTitle(DialogTitle title, GradientPanel panel) {	
		panel.removeAll();
		panel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE), new MatteBorder(0, 0, 1, 0, Color.GRAY)));
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
		
		JLabel titleLabel = new JLabel(String.format("<html><b>%s</b><br>%s</html>", title.getTitle(), title.getDescription()));
		titleLabel.setBounds(67, 0, 450, 70);
		panel.add(titleLabel);
	}
	
	public abstract void createGui(JPanel guiPanel);
	
	public JPanel getGuiPanel() {
		return guiPanel;
	}

}
