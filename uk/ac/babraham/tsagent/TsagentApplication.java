package uk.ac.babraham.tsagent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.tsagent.analysis.ImageAnalyser;
import uk.ac.babraham.tsagent.analysis.IntensityDistancePoint;
import uk.ac.babraham.tsagent.analysis.ResultSet;
import uk.ac.babraham.tsagent.gui.ImageSetPanel;
import uk.ac.babraham.tsagent.images.ImageReader;
import uk.ac.babraham.tsagent.images.TsagentImageSet;

public class TsagentApplication extends JFrame {

	private TsagentImageSet imageSet;
	private ResultSet results;
	private File [] files;

	public TsagentApplication () {

		super("TSAgent");
		setJMenuBar(new TsagentMenu());
		getContentPane().setLayout(new BorderLayout());
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}


	public void processFiles (File [] files) {
		
		this.files = files;
		
		Thread t = new Thread (new Runnable() {
			
			public void run() {
				for (int f=0;f<TsagentApplication.this.files.length;f++) {
					openFile(TsagentApplication.this.files[f]);
					analyse();
					try {
						
						File resultsFile = new File(TsagentApplication.this.files[f].getAbsolutePath()+"_tsa.txt");
						
						writeResults(resultsFile);
					}
					catch (IOException ioe) {
						JOptionPane.showMessageDialog(TsagentApplication.this, "Failed to process "+TsagentApplication.this.files[f]+" "+ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}				
			}
		});
		
		t.start();
	}
	
	public void openFile (File file) {
		imageSet = ImageReader.readFile(file);
		getContentPane().add(new ImageSetPanel(imageSet),BorderLayout.CENTER);
		validate();
	}

	public void analyse () {
		ImageAnalyser analyser = new ImageAnalyser(imageSet);

		results = analyser.runAnalysis();
		getContentPane().removeAll();

		getContentPane().add(new ImageSetPanel(imageSet),BorderLayout.CENTER);
		validate();
		repaint();
	}

	public void writeResults (File file) throws IOException {
		if (results == null) return;

		PrintWriter pr = new PrintWriter(new FileWriter(file));

		IntensityDistancePoint [] id = results.getDataPoints();

		for (int i=0;i<id.length;i++) {
			pr.println(""+id[i].distance()+"\t"+id[i].intensity());
		}

		pr.close();

	}




	public static void main (String [] args) {
		new TsagentApplication();
	}

	private class TsagentMenu extends JMenuBar {
		public TsagentMenu () {
			
			JMenu fileMenu = new JMenu("File");
			
			JMenuItem fileOpen = new JMenuItem("Analyse");
			fileOpen.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					JFileChooser chooser = new JFileChooser();
					chooser.setMultiSelectionEnabled(true);
					chooser.setFileFilter(new FileFilter() {
						
						public String getDescription() {
							return "TIFF files";
						}
						
						public boolean accept(File f) {
							if (f.isDirectory() || f.getName().toLowerCase().endsWith(".tif") || f.getName().toLowerCase().endsWith(".tiff")) {
								return true;
							}
							
							return false;
						}
					});
				
					int result = chooser.showOpenDialog(TsagentApplication.this);
					if (result == JFileChooser.CANCEL_OPTION) return;

					File [] files = chooser.getSelectedFiles();

					TsagentApplication.this.processFiles(files);
				}
			});
			
			fileMenu.add(fileOpen);
			
			add(fileMenu);
		}
	}

}


