# tsagent
This is an ImageJ based analysis tool which takes in a 2-channel TIFF image of a single cell.  
The first channel should be a signal that defines the outer membrane of the cell, and the second
channel is the signal you wish to measure.

The program uses the first channel to identify the cell's border, and then takes the set of
points within this border and for each of these it measures the intensity of the point in the
second channel, and the distance to the nearest border point.  These paired measurements
are then written out to a tab delimited text file along with some QC images showin the
efficacy of the border detection.
