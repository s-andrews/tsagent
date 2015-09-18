package uk.ac.babraham.tsagent.analysis;

public class IntensityDistancePoint {

	private int intensity;
	private double distance;
	
	public IntensityDistancePoint (int intensity, double distance) {
		this.intensity = intensity;
		this.distance = distance;
	}
	
	public int intensity () {
		return intensity;
	}
	
	public double distance () {
		return distance;
	}
	
}
