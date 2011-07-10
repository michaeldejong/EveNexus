package nl.minicom.evenexus.gui.utils.dialogs;


import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;


public abstract class DatabaseFileChooser extends JDialog {

	private static final long serialVersionUID = -1581636709182360671L;

	private static int width = 550;
	private static int height = 400;
	
	public void initialize() {
		setResizable(false);
		setTitle("Select file");
		setIconImage(Icon.getImage("img/other/logo.png"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPosition = (int) (env.getMaximumWindowBounds().getWidth() - width) / 2;
		int yPosition = (int) (env.getMaximumWindowBounds().getHeight() - height) / 2;
		setBounds(xPosition, yPosition, width, height);
		Gui.setLookAndFeel();
		
		createGui();
		setVisible(true);
	}

	private void createGui() {
		
		final JFileChooser chooser = new JFileChooser();
		
		chooser.addChoosableFileFilter(new FileFilter() {			
			@Override
			public String getDescription() {
				return getClass().getPackage().getSpecificationTitle() + " database backup (.zip)";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".zip") || f.isDirectory();
			}
		});
				
		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						onFileSelect(chooser.getSelectedFile());
					}
				}.start();
				dispose();
			}
		});
		
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setMinimumSize(new Dimension(1, 1));
		chooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		setAdditionalParameters(chooser);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		setLayout(layout);   
		layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addGap(7)
    		.addComponent(chooser)
    		.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)    		
					.addGap(7)
					.addComponent(chooser)
		    		.addGap(7)
		        )
        	)
    	);		
	}
	
	public abstract void setAdditionalParameters(JFileChooser chooser);

	public abstract void onFileSelect(File file);
	
}
