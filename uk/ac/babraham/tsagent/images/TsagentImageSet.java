package uk.ac.babraham.tsagent.images;

import ij.ImagePlus;

public class TsagentImageSet {
	
	private ImagePlus borderImage;
	private ImagePlus measureImage;

	public TsagentImageSet (ImagePlus borderImage, ImagePlus measureImage) {
		this.borderImage = borderImage;
		this.measureImage = measureImage;
	}
	
	public ImagePlus borderImage () {
		return borderImage;
	}
	
	public ImagePlus measureImage () {
		return measureImage;
	}
	
}
