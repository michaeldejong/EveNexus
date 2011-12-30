package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import nl.minicom.evenexus.core.report.engine.DisplayType;
import nl.minicom.evenexus.core.report.engine.Model;
import nl.minicom.evenexus.core.report.engine.ModelListener;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;
import nl.minicom.evenexus.gui.utils.dialogs.titles.ReportDisplayTitle;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * This class is responsible for selecting a representation form for a report.
 *
 * @author michael
 */
public class ReportDisplayPage extends ReportWizardPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private static final Color BORDER_SELECTED = new Color(0, 114, 186);
	private static final Color BORDER_HOVER = new Color(128, 184, 221);
	private static final Color BORDER_DEFAULT = new Color(255, 255, 255);
	
	private static final Color BACKGROUND_SELECTED = new Color(128, 184, 221);
	private static final Color BACKGROUND_HOVER = new Color(196, 214, 238);
	private static final Color BACKGROUND_DEFAULT = null;

	private final List<ReportDisplayEntry> entries;
	
	private ReportModel model;
	
	/**
	 * This constructs a new {@link ReportDisplayPage}.
	 * 
	 * @param model
	 * 		The {@link ReportModel}.
	 */
	@Inject
	public ReportDisplayPage(ReportModel model) {
		this.entries = Lists.newArrayList();
		this.model = model;
	}

	/**
	 * This method builds the gui, allowing the user to select a display type.
	 */
	@Override
	public void buildGui() {
		GroupLayout layout = new GroupLayout(this);
		Group horizontalGroup = layout.createParallelGroup();
		Group verticalGroup = layout.createSequentialGroup();
		
		for (DisplayType type : DisplayType.values()) {
			ReportDisplayEntry entry = new ReportDisplayEntry(type, model);
			horizontalGroup.addComponent(entry);
			verticalGroup.addComponent(entry);
			entries.add(entry);
		}		
		
		setLayout(layout);        
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));
    	layout.setVerticalGroup(verticalGroup);
	}

	@Override
	public DialogTitle getTitle() {
		return new ReportDisplayTitle();
	}
	
	/**
	 * The {@link ReportDisplayEntry} class is a gui panel which contains an
	 * image, which when clicked sets the {@link DisplayType} of the report.
	 * 
	 * @author michael
	 */
	public class ReportDisplayEntry extends JPanel {

		private static final long serialVersionUID = 1280615798706096140L;
		
		private final JPanel iconPanel;
		private final ReportModel model;
		
		/**
		 * This constructs a new {@link ReportDisplayEntry}.
		 * 
		 * @param type
		 * 		The {@link DisplayType} of this {@link ReportDisplayEntry}.
		 * 
		 * @param model
		 * 		The {@link ReportModel}.
		 */
		public ReportDisplayEntry(DisplayType type, ReportModel model) {
			this.model = model;
			this.iconPanel = createImageButton(type);
			JLabel title = GuiConstants.createBoldLabel(type.getTitle());
			JLabel description = createDescription(type.getDescription());
			
			onModelDisplayStateChange(type);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(4)
				.addComponent(iconPanel)
				.addGap(10)
				.addGroup(
					layout.createParallelGroup()
					.addComponent(title)
					.addComponent(description)
				)
				.addGap(4)
			);
			
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(4)
				.addGroup(layout.createParallelGroup()
						.addComponent(iconPanel)
						.addGroup(
							layout.createSequentialGroup()
							.addComponent(title)
							.addComponent(description)
						)
				)
				.addGap(4)
			);
		}
		
		private JLabel createDescription(String value) {
			JLabel label = new JLabel("<html>" + value + "</html>");
			label.setVerticalAlignment(JLabel.TOP);
			label.setMinimumSize(new Dimension(0, GuiConstants.TEXT_FIELD_HEIGHT));
			label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
			return label;
		}

		private JPanel createImageButton(final DisplayType type) {
			final JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setMaximumSize(new Dimension(64, 64));
			panel.setMinimumSize(new Dimension(64, 64));
			panel.setLayout(null);
			
			ImageIcon iconImage = Icon.getIcon(type.getIcon());
			final JLabel iconLabel = new JLabel(iconImage);
			iconLabel.setBounds(8, 8, 48, 48);
			panel.add(iconLabel);
			
			addListener(type);
			
			return panel;
		}
		
		private void addListener(final DisplayType type) {
			model.getDisplayType().addListener(new ModelListener() {
				@Override
				public void onValueChanged() {
					onModelDisplayStateChange(type);
				}

				@Override
				public void onStateChanged() {
					// Do nothing.
				}
			});
			
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					model.getDisplayType().setValue(type);
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					// Do nothing.
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					onModelDisplayStateChange(type);
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					Model<DisplayType> displayType = model.getDisplayType();
					if (displayType.isSet() && type.equals(displayType.getValue())) {
						setState(State.SELECTED);
					}
					else {
						setState(State.HOVER);
					}
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// Do nothing.
				}
			});
		}

		private void onModelDisplayStateChange(final DisplayType type) {
			Model<DisplayType> displayType = model.getDisplayType();
			if (displayType.isSet() && type.equals(displayType.getValue())) {
				setState(State.SELECTED);
			}
			else {
				setState(State.DEFAULT);
			}
		}

		private void setState(State newState) {
			switch (newState) {
				case SELECTED:
					setColors(BACKGROUND_SELECTED, BORDER_SELECTED);
					break;
				case HOVER:
					setColors(BACKGROUND_HOVER, BORDER_HOVER);
					break;
				default: 
					setColors(BACKGROUND_DEFAULT, BORDER_DEFAULT);
			}
		}
		
		private void setColors(Color background, Color border) {
			setBackground(background);
			iconPanel.setBorder(new CompoundBorder(
					new LineBorder(Color.GRAY, 1),
					new LineBorder(border, 3)
			));
		}
	}
	
	/**
	 * This enum represents the state of the {@link ReportDisplayEntry}.
	 * 
	 * @author michael
	 */
	private enum State {
		DEFAULT,
		HOVER,
		SELECTED;
	}
}
