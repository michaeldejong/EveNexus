package nl.minicom.evenexus.gui.utils.dialogs;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import nl.minicom.evenexus.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AboutDialog extends CustomDialog {

	private static final long serialVersionUID = 8860569373371189189L;
	private static final Logger LOG = LoggerFactory.getLogger(AboutDialog.class);
		
	private final Application application;
	
	public AboutDialog(Application application) {
		super(DialogTitle.ABOUT_TITLE, 370, 368);
		this.application = application;
		
		buildGui();
		setVisible(true);
		setResizable(true);
	}

	@Override
	public void createGui(JPanel guiPanel) {	
		
		JPanel devPanel = createDevPanel();
		JPanel versionPanel = createVersionPanel();
		JPanel contributionPanel = createContributionPanel();
		
		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);        
		layout.setHorizontalGroup(
      	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(versionPanel)
        				.addComponent(devPanel)
        				.addComponent(contributionPanel)
        		)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addComponent(versionPanel)
	    		.addGap(7)
	    		.addComponent(devPanel)
	    		.addGap(7)
	    		.addComponent(contributionPanel)
    	);
	}
	
	private JPanel createVersionPanel() {
		JPanel versionPanel = new JPanel();
		versionPanel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Version"), new EmptyBorder(0, 10, 0, 10)));
		versionPanel.setMinimumSize(new Dimension(0, 78));
		versionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
		
		JLabel versions = new JLabel(createVersionString());
		versions.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		
		GroupLayout layout = new GroupLayout(versionPanel);
		versionPanel.setLayout(layout);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
        		.addComponent(versions)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addComponent(versions)
    	);
		
		return versionPanel;
	}

	private String createVersionString() {
		String applicationVersion = getClass().getPackage().getSpecificationVersion();
		String databaseVersion = application.getDatabaseVersion();
		String contentVersion = application.getContentVersion();
		String buildNumber = getClass().getPackage().getImplementationVersion();
		
		if (buildNumber == null || buildNumber.trim().isEmpty()) {
			buildNumber = "unknown";
		}
		
		return 	"<html>Application version: " + applicationVersion + " (" + 
				"build: " + buildNumber + ")<br>" + 
				"Database version: " + databaseVersion + "<br>" + 
				"Content version: " + contentVersion + "</html>";
	}
	
	private JPanel createContributionPanel() {
		JPanel contributionPanel = new JPanel();
		contributionPanel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Special thanks to"), new EmptyBorder(0, 10, 0, 10)));
		contributionPanel.setMinimumSize(new Dimension(0, 112));
		contributionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
				
		JEditorPane contribution = new JEditorPane("text/html", createContributionString());
		contribution.setEditable(false);
		contribution.setBackground(contributionPanel.getBackground());
		contribution.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		contribution.addHyperlinkListener(new HyperlinkListener() {			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(event.getURL().toURI());
					}
					catch (Exception e) {
						LOG.error(e.getLocalizedMessage(), e);
					} 
				}
			}
		});
		
		GroupLayout layout = new GroupLayout(contributionPanel);
		contributionPanel.setLayout(layout);        
		layout.setHorizontalGroup(
      	layout.createSequentialGroup()
        		.addComponent(contribution)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addComponent(contribution)
    	);
		
		return contributionPanel;
	}

	private String createContributionString() {
		Font font = UIManager.getFont("Label.font");
       
		return 	"<html>" + 
				"<head>" +
				"<style>" + 
				"BODY { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }" + 
				"</style>" + 
				"</head>" + 
				"<body>" + 
				"<a href='http://www.eve-online.com/'>CCP Games</a>, for the game itself and an awesome API<br>" + 
				"<a href='http://wiki.eve-id.net/'>EVEDev</a>, for their extensive documentation of the API<br>" + 
				"<a href='http://cemagraphics.deviantart.com/'>=cemagraphics</a>, for designing the logo<br>" + 
				"<a href='http://www.wefunction.com/'>WeFunction.com</a>, for all the other icons<br>" + 
				"<a href='https://github.com/michaeldejong/EveNexus'>GitHub.com</a>, for being a tremendous Git host!<br>" + 
				"</body>" + 
				"</html>";
	}

	private JPanel createDevPanel() {
		JPanel devPanel = new JPanel();
		devPanel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Developer"), new EmptyBorder(0, 10, 0, 10)));
		devPanel.setMinimumSize(new Dimension(0, 50));
		devPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		
		JLabel developer = new JLabel("Michael de Jong");
		developer.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		
		GroupLayout layout = new GroupLayout(devPanel);
		devPanel.setLayout(layout);        
		layout.setHorizontalGroup(
      	layout.createSequentialGroup()
        		.addComponent(developer)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addComponent(developer)
    	);
		
		return devPanel;
	}
	

}
