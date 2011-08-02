package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.engine.DisplayType;
import nl.minicom.evenexus.core.report.engine.ReportModelListener;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

public class ReportDisplayPanel extends ReportBuilderPage {

	private static final long serialVersionUID = 3066113966844699181L;

//	private final ReportDefinition definition;
	private final ReportModel model;

	public ReportDisplayPanel(ReportDefinition definition, ReportModel model) {
//		this.definition = definition;
		this.model = model;
		
		buildGui();
	}

	private void buildGui() {
		GroupLayout layout = new GroupLayout(this);
		Group horizontalGroup = layout.createParallelGroup();
		Group verticalGroup = layout.createSequentialGroup();
		
		for (DisplayType type : DisplayType.values()) {
			JPanel icon = createImageButton(type);
			JLabel title = createBoldLabel(type.getTitle());
			JLabel description = createDescription(type.getDescription());
			
			horizontalGroup.addGroup(
				layout.createSequentialGroup()
				.addComponent(icon)
				.addGap(10)
				.addGroup(
					layout.createParallelGroup()
					.addComponent(title)
					.addComponent(description)
				)
			);
			
			verticalGroup.addGroup(
				layout.createParallelGroup()
				.addComponent(icon)
				.addGroup(
					layout.createSequentialGroup()
					.addComponent(title)
					.addComponent(description)
				)
			);
			verticalGroup.addGap(4);
		}		
		
		setLayout(layout);        
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));
    	layout.setVerticalGroup(verticalGroup);
	}
	
	private JLabel createDescription(String value) {
		JLabel label = new JLabel("<html>" + value + "</html>");
		label.setVerticalAlignment(JLabel.TOP);
		label.setMinimumSize(new Dimension(0, GuiConstants.TEXT_FIELD_HEIGHT));
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
		return label;
	}

	private JPanel createImageButton(final DisplayType type) {
		final JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setMaximumSize(new Dimension(64, 64));
		panel.setMinimumSize(new Dimension(64, 64));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.setLayout(null);
		
		ImageIcon iconImage = Icon.getIcon(type.getIcon());
		final JLabel iconLabel = new JLabel(iconImage);
		iconLabel.setBounds(8, 8, 48, 48);
		panel.add(iconLabel);
		
		model.addListener(new ReportModelListener() {
			@Override
			public void onValueChanged() {
				setBorderColor(panel, model.getDisplayType().getValue().getId(), false);
			}
		});
		
		panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				model.getDisplayType().setValue(type);
				setBorderColor(panel, type.getId(), true);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				setBorderColor(panel, type.getId(), true);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBorderColor(panel, type.getId(), false);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBorderColor(panel, type.getId(), true);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) { }
		});
		
		return panel;
	}
	
	private void setBorderColor(JPanel panel, int id, boolean hover) {
		DisplayType type = model.getDisplayType().getValue();
		if (type == null) {
			setBorderColor(panel, new Color(0, 0, 0, 0));
		}
		
		if (id == type.getId()) {
			setBorderColor(panel, new Color(0, 114, 186));
		}
		else if (hover) {
			setBorderColor(panel, new Color(128, 184, 221));
		}
		else {
			setBorderColor(panel, new Color(0, 0, 0, 0));
		}
	}
	
	private void setBorderColor(JPanel label, Color color) {
		label.setBorder(new CompoundBorder(
				new LineBorder(Color.GRAY, 1),
				new LineBorder(color, 3)
		));
	}

	@Override
	public DialogTitle getTitle() {
		return DialogTitle.REPORT_DISPLAY_TITLE;
	}
	
}
