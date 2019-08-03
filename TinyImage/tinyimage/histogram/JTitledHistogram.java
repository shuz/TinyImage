package tinyimage.histogram;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tinyimage.GrayLevelImage;

public class JTitledHistogram extends JPanel {
	private static final long serialVersionUID = 2662301167758514107L;
	private JLabel jChannelTitle;
	private JHistogramPanel jHistogram;
	
	public JTitledHistogram(String name, GrayLevelImage image) {
		jChannelTitle = new JLabel();
		jChannelTitle.setText(name);
		jChannelTitle.setAlignmentX(0.5f);
		jHistogram = new JHistogramPanel(image);
		jHistogram.setAlignmentX(0.5f);
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(boxLayout);
		this.add(jChannelTitle, null);
		this.add(jHistogram, null);
	}
	
	public String getHistogramName() {
		return jChannelTitle.getText();
	}
	
	public void setHistogramTitle(String name) {
		jChannelTitle.setText(name);
	}
	
	public void setImage(GrayLevelImage image) {
		jHistogram.computeHistogram(image);
	}
	
	public JHistogramPanel getHistogram() {
		return jHistogram;
	}
	
	public String toString() {
		return getHistogramName();
	}
}
