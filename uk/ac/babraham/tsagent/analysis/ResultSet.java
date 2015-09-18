package uk.ac.babraham.tsagent.analysis;

import java.util.ArrayList;

public class ResultSet {

	private ArrayList<IntensityDistancePoint> dataPoints = new ArrayList<IntensityDistancePoint>();
	
	
	public void addDataPoint (IntensityDistancePoint p) {
		dataPoints.add(p);
	}
	
	public IntensityDistancePoint [] getDataPoints () {
		return dataPoints.toArray(new IntensityDistancePoint[0]);
	}
	
	
}
