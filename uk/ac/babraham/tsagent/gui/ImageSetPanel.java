package uk.ac.babraham.tsagent.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import uk.ac.babraham.tsagent.images.TsagentImageSet;

public class ImageSetPanel extends JPanel {

	public ImageSetPanel (TsagentImageSet images) {
		
		setLayout(new GridLayout(1, 2));
		add(new JImagePanel(images.borderImage()));
		add(new JImagePanel(images.measureImage()));
		
	}
	
	
}
