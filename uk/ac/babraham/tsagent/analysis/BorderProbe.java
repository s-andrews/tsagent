package uk.ac.babraham.tsagent.analysis;

import java.util.Vector;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class BorderProbe {

	private xyPoint [] pointsOnPath;
	xyPoint borderPoint;
	
	public BorderProbe (ImagePlus image,xyPoint centre, double angle) {
		
		// First we need to plot a path from the centre to the edge of the
		// image at the given angle
		
		Vector<xyPoint> tempPoints = new Vector<xyPoint>();
		
		int distance = 0;
		
		while (true) {
			distance++;
			
			// Get the x and y points for something which is 'distance'
			// away from the start at the given angle.
			
			double xDiff = Math.sin(Math.toRadians(angle))*distance;
			double yDiff = Math.cos(Math.toRadians(angle))*distance;
			
			int x = centre.x+(int)xDiff;
			int y = centre.y+(int)yDiff;
			
			// Check whether this fell off the end of the image
			if (x <= 0) break;
			if (y <= 0) break;
			if (x >= image.getWidth()) break;
			if (y >= image.getHeight()) break;
			
			// If there are existing points then check that this is different
			// to the last one we calculated
			if (tempPoints.size() > 0 && tempPoints.get(tempPoints.size()-1).x == x && tempPoints.get(tempPoints.size()-1).y == y) continue;
			
			if (angle == 45) {
				System.err.println("New point at "+x+","+y);
			}
			
			tempPoints.add(new xyPoint(x, y));			
			
		}
		
		pointsOnPath = tempPoints.toArray(new xyPoint[0]);
		
		
		// Now we work our way along the path finding the brightest point in a 5x5 grid
		
		int maxIndex = 0;
		double maxIntensity = 0;
		
		ImageProcessor p = image.getProcessor();
		
		for (int i=0;i<pointsOnPath.length;i++) {
			
			double intensity = 0;
			int count = 0;
			
			for (int x=-3;x<=3;x++) {
				for (int y=-3;y<=3;y++) {
					
					int x1 = pointsOnPath[i].x+x;
					int y1 = pointsOnPath[i].y+y;

					if (x1<0 || x1>=p.getWidth()) continue;
					if (y1<0 || y1>=p.getHeight()) continue;
					
					count++;
					intensity += p.get(x1,y1);
				}
			}
			
			if (count == 0) {
				System.err.println("No valid points - shouldn't happen.  Started at "+pointsOnPath[i].x+","+pointsOnPath[i].y);
			}
			else {
				
				intensity /= count;
				
				if (intensity > maxIntensity) {
					maxIndex = i;
					maxIntensity = intensity;
				}
			}
			
		}

		borderPoint = pointsOnPath[maxIndex];
		
		System.err.println("At "+angle+" degrees the shell is at "+borderPoint.x+","+borderPoint.y+" with intensity "+maxIntensity);
		
		
	}
	
	
}
