package uk.ac.babraham.tsagent.images;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;

public class ImageReader {

	public static TsagentImageSet readFile (File file) {
		
		ImagePlus wholeImage = IJ.openImage(file.getAbsolutePath());
		
		ImagePlus borderImage = new ImagePlus(wholeImage.getImageStack().getSliceLabel(1),wholeImage.getImageStack().getProcessor(1));
		borderImage.setCalibration(wholeImage.getCalibration());
		
		ImagePlus measureImage = new ImagePlus(wholeImage.getImageStack().getSliceLabel(2),wholeImage.getImageStack().getProcessor(2));
		borderImage.setCalibration(wholeImage.getCalibration());

		return new TsagentImageSet(borderImage, measureImage);
		
	}
	
	
}
