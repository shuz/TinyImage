package tinyimage;

import javax.swing.JFrame;

import tinyimage.histogram.HistogramController;
import tinyimage.spatialfilter.SpatialFilterController;
import tinyimage.morphology.MorphologyController;

public class TinyImageApp {

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		TinyImage frame = new TinyImage();
		frame.addController(new HistogramController());
		frame.addController(new SpatialFilterController());
		frame.addController(new MorphologyController());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
