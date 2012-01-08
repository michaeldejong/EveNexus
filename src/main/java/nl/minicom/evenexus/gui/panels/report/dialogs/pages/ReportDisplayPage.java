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
public class ReportDisplayPage extends ReportWizardPage implements DisplayEntryListener {

	private static final long serialVersionUID = 3066113966844699181L;

	private static final Color BORDER_SELECTED = new Color(0, 114, 186);
	private static final Color BORDER_HOVER = new Color(128, 184, 221);
	private static final Color BORDER_DEFAULT = new Color(255, 255, 255);
	
	private static final Color BACKGROUND_SELECTED = new Color(128, 184, 221);
	private static final Color BACKGROUND_HOVER = new Color(196, 214, 238);
	private static final Color BACKGROUND_DEFAULT = null;

	private final List<ReportDisplayEntry> entries;
	
	private ReportModel model;
	private ReportPageListener listener = null;
	
	/**
	 * This constructs a new {@link ReportDisplayPage}.
	 * 
	 * @param model
	 * 		The {@link ReportModel}.
	 */
	@Inject
	public ReportDisplayPage(ReportModel model) {
		super(model);
		this.entries = Lists.newArrayList();
		this.model = model;
	}

	/**
	 * This method builds the gui, allowing the user to select a display type.
	 * 
	 * @param listener
	 * 		The {@link ReportPageListener}.
	 */
	@Override
	public void buildGui(ReportPageListener listener) {
		GroupLayout layout = new GroupLayout(this);
		Group horizontalGroup = layout.createParallelGroup();
		Group verticalGroup = layout.createSequentialGroup();
		
		for (DisplayType type : DisplayType.values()) {
			ReportDisplayEntry entry = new ReportDisplayEntry(this, type, model);
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
		private final DisplayType type;
		
		private State state = State.DEFAULT;
		
		/**
		 * This constructs a new {@link ReportDisplayEntry}.
		 * 
		 * @param listener
		 * 		The {@link ReportPageListener}. 
		 * 
		 * @param type
		 * 		The {@link DisplayType} of this {@link ReportDisplayEntry}.
		 * 
		 * @param model
		 * 		The {@link ReportModel}.
		 */
		public ReportDisplayEntry(final DisplayEntryListener listener, DisplayType type, ReportModel model) {
			this.model = model;
			this.iconPanel = createImageButton(type);
			this.type = type;
			
			JLabel title = GuiConstants.createBoldLabel(type.getTitle());
			JLabel description = createDescription(type.getDescription());
			
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
			
			final ReportDisplayEntry entry = this;
			addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// Do nothing.
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					// Do nothin.
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					listener.onDeselectionMove(entry);
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					listener.onSelectionMove(entry);
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					listener.onStateChange(entry);
				}
			});
			
			Model<DisplayType> displayType = model.getDisplayType();
			if (displayType.isSet() && displayType.getValue().equals(type)) {
				onStateChange(this);
			}
		}
		
		private DisplayType getType() {
			return type;
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
			
			return panel;
		}
		
		private void setState(State newState) {
			this.state = newState;
			switch (newState) {
				case SELECTED:
					model.getDisplayType().setValue(getType());
					setColors(BACKGROUND_SELECTED, BORDER_SELECTED);
					break;
				case HOVER:
					setColors(BACKGROUND_HOVER, BORDER_HOVER);
					break;
				default: 
					setColors(BACKGROUND_DEFAULT, BORDER_DEFAULT);
			}
		}

		private State getState() {
			return state;
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

	@Override
	public boolean allowPrevious() {
		return true;
	}

	@Override
	public boolean allowNext() {
		return false;
	}

	@Override
	public void onStateChange(ReportDisplayEntry selected) {
		for (ReportDisplayEntry entry : entries) {
			if (entry.equals(selected)) {
				entry.setState(State.SELECTED);
			}
			else {
				entry.setState(State.DEFAULT);
			}
		}
		
		if (listener != null) {
			listener.onModification();
		}
	}

	@Override
	public void onSelectionMove(ReportDisplayEntry selected) {
		for (ReportDisplayEntry entry : entries) {
			if (entry.equals(selected)) {
				entry.setState(State.HOVER);
			}
			else if (entry.getState() == State.HOVER){
				entry.setState(State.DEFAULT);
			}
		}
	}

	@Override
	public void onDeselectionMove(ReportDisplayEntry deselected) {
		for (ReportDisplayEntry entry : entries) {
			if (entry.equals(deselected)) {
				entry.setState(State.DEFAULT);
			}
		}
	}
	
}
