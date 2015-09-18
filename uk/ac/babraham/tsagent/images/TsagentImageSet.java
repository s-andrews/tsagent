package uk.ac.babraham.tsagent.images;

import java.io.File;

import ij.ImagePlus;

public class TsagentImageSet {
	
	private ImagePlus borderImage;
	private ImagePlus measureImage;
	private File file;

	public TsagentImageSet (File file, ImagePlus borderImage, ImagePlus measureImage) {
		this.borderImage = borderImage;
		this.measureImage = measureImage;
		this.file = file;
	}
	
	public ImagePlus borderImage () {
		return borderImage;
	}
	
	public ImagePlus measureImage () {
		return measureImage;
	}
	
	public File file () {
		return file;
	}
	
}
