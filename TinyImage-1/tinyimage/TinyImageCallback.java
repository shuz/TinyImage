package tinyimage;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public interface TinyImageCallback {
	BufferedImage getOriginalImage();
	void setTransformedImage(BufferedImage image);
	JPanel getSelectedCard();
}
