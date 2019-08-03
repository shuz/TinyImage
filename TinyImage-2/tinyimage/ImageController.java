package tinyimage;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public interface ImageController {
	JPanel getPanel();
	String getPanelName();
	void setAppCallback(TinyImageCallback callback);
	void onOriginalImageChange(BufferedImage image);
}
