package uk.ac.babraham.tsagent.analysis;

import java.util.ArrayList;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import uk.ac.babraham.tsagent.images.TsagentImageSet;

public class ImageAnalyser {


	private TsagentImageSet images;
	
	public ImageAnalyser (TsagentImageSet images) {
		this.images = images;
	}
	
	public ResultSet runAnalysis () {
		
		// First we need to find a centre point in the cell.  To do this we'll threshold the image
		// and then take the average position from the pixels which are left.  Hopefully this should
		// fall somewhere within the cell.
		
		int threshold = calculateThreshold(images.borderImage());
		
		System.err.println("Auto threshold is "+threshold);
		
		// Now we get the centre of the points above this threshold
		xyPoint xyCentre = getCentre(images.borderImage(),threshold);
		
		System.err.println("Centre point is "+xyCentre.x+","+xyCentre.y);
		
		
		// Now we set of 360 probes to spread out from the centre point to the
		// edge of the image.
		
		BorderProbe [] probes = new BorderProbe[360];
		
		for (int i=0;i<probes.length;i++) {
			probes[i] = new BorderProbe(images.borderImage(), xyCentre, i);
		}
	
		// We can now highlight the border on the border image
		ImageProcessor p = images.borderImage().getProcessor();
		
		for (int i=0;i<probes.length;i++) {
			p.set(probes[i].borderPoint.x, probes[i].borderPoint.y,5000);
		}
		
		images.borderImage().setProcessor("Border", p);
			
		
		// We should now check for the circularity of the points.  We can take the average position (centre)
		// and then measure the distance from that to each point.  Any points whose distance changes too much
		// could be kicked.  If the proportion of non-circular points is too high we could kick the whole cell
		
		
		// Now we can use the measure data to see what the relationship between the intensities and
		// the distance from the edge is
		
		ResultSet results = getIntensityDistanceRelationship(probes,images.measureImage());
		
		return results;
	}
	
	
	private int calculateThreshold (ImagePlus image) {
		ImageProcessor p = image.getProcessor();
		
		return(p.getAutoThreshold());
	}
	
	private xyPoint getCentre (ImagePlus image, int threshold) {
		
		double xSum = 0;
		double ySum = 0;
		int count = 0;
		
		ImageProcessor p = image.getProcessor();
		for (int x=0;x<p.getWidth();x++) {
			for (int y=0;y<p.getHeight();y++) {
				if (p.get(x,y) >= threshold) {
					xSum += x;
					ySum += y;
					count++;
				}
			}
		}
		
		System.err.println("Found "+count+" pixels above threshold");
		
		int xReturn = (int) (xSum/count);
		int yReturn = (int) (ySum/count);
		
		return(new xyPoint(xReturn,yReturn));
		
	}
	
	private ResultSet getIntensityDistanceRelationship(BorderProbe [] probes,ImagePlus image) {
		
		ResultSet results = new ResultSet();
		
		ImageProcessor proc = image.getProcessor();
		
		for (int line=0;line<image.getHeight();line++) {
			
			// See what points we have on this line
			ArrayList<BorderProbe> probesOnThisLine = new ArrayList<BorderProbe>();
			
			for (int p=0;p<probes.length;p++) {
				if (probes[p].borderPoint.y == line) {
					probesOnThisLine.add(probes[p]);
				}
			}
			
			// See if we have probes at each end
			if (probesOnThisLine.size() < 2) {
				//TODO: See if we can extrapolate from the lines above/below
				continue;
			}
			
			int leftmostX = image.getWidth();
			int rightmostX = -1;
			
			for (BorderProbe p : probesOnThisLine) {
				if (p.borderPoint.x < leftmostX) leftmostX = p.borderPoint.x;
				if (p.borderPoint.x > rightmostX) rightmostX = p.borderPoint.x;
			}
			
			if (rightmostX - leftmostX < 10) continue; // Don't worry about points too close to the edge
			
			for (int x=leftmostX;x<=rightmostX;x++) {
				
				double closestDistance = 10000000;
				for (int p=0;p<probes.length;p++) {
					double thisDistance = Math.sqrt(Math.pow(x-probes[p].borderPoint.x,2)+Math.pow(line-probes[p].borderPoint.y,2));
					if (thisDistance < closestDistance) closestDistance = thisDistance;
				}
				
//				System.err.println("Added data intensity="+proc.get(x,line)+" distance="+closestDistance);
				results.addDataPoint(new IntensityDistancePoint(proc.get(x,line), closestDistance));
				proc.set(x, line,5000);
				
			}
			
			image.setProcessor("measure", proc);
			
			
			
		}
		
		
		
		return results;
	}
	
	
}
