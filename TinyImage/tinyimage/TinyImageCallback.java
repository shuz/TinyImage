package tinyimage;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public interface TinyImageCallback {
	BufferedImage getOriginalImage();
	BufferedImage getTransformedImage();
	void setOriginalImage(BufferedImage image);
	void setTransformedImage(BufferedImage image);
	JPanel getSelectedCard();
	JImagePanel getOriginalImagePanel();
	JImagePanel getTransformedImagePanel();
}
