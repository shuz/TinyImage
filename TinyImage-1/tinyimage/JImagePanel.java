package tinyimage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class JImagePanel extends JPanel {
	private static final long serialVersionUID = 5101443867386397388L;
	private BufferedImage image;
	
	public JImagePanel() {}
	
	public void setImage(BufferedImage image) {
	  this.image = image;
	  if (image != null) {
		  setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	  } else {
		  setPreferredSize(new Dimension(0, 0));
	  }
	  revalidate();
	}
	
	public void paintComponent(Graphics g) {
	  super.paintComponent(g);
	  if (image != null) {
	    g.drawImage(image, 0, 0, this);
	  }
	}
}
