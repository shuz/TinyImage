package tinyimage;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;

public final class TinyImageUtils {

	public static GrayLevelImage getGraylevelImage(final BufferedImage image) {
		if (image == null) return NULL_IMAGE;
		
		return new GrayLevelImage() {
			public int getWidth() {
				return image.getWidth();
			}
	
			public int getHeight() {
				return image.getHeight();
			}
	
			public int getPixel(int x, int y) {
				int rgb = image.getRGB(x, y) & 0x00FFFFFF;
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >>  8) & 0xFF;
				int b = (rgb >>  0) & 0xFF;
				
				return (int)(r * 0.3 + g * 0.59 + b * 0.11 + 0.5);
			}
		};
	};

	public static GrayLevelImage getRedChannelImage(final BufferedImage image) {
		if (image == null) return NULL_IMAGE;
		
		return new GrayLevelImage() {
			public int getWidth() {
				return image.getWidth();
			}
	
			public int getHeight() {
				return image.getHeight();
			}
	
			public int getPixel(int x, int y) {
				int rgb = image.getRGB(x, y) & 0x00FFFFFF;
				int r = (rgb >> 16) & 0xFF;
				return r;
			}
		};
	};

	public static GrayLevelImage getGreenChannelImage(final BufferedImage image) {
		if (image == null) return NULL_IMAGE;
		
		return new GrayLevelImage() {
			public int getWidth() {
				return image.getWidth();
			}
	
			public int getHeight() {
				return image.getHeight();
			}
	
			public int getPixel(int x, int y) {
				int rgb = image.getRGB(x, y) & 0x00FFFFFF;
				int r = (rgb >> 8) & 0xFF;
				return r;
			}
		};
	};

	public static GrayLevelImage getBlueChannelImage(final BufferedImage image) {
		if (image == null) return NULL_IMAGE;
		
		return new GrayLevelImage() {
			public int getWidth() {
				return image.getWidth();
			}
	
			public int getHeight() {
				return image.getHeight();
			}
	
			public int getPixel(int x, int y) {
				int rgb = image.getRGB(x, y) & 0x00FFFFFF;
				int r = (rgb >> 0) & 0xFF;
				return r;
			}
		};
	};
	
	public static SimpleGrayLevelImage negate(GrayLevelImage image) {
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				result.setPixel(i, j, 255 - image.getPixel(i, j));
			}
		}
		return result;
	}
	
	public static SimpleGrayLevelImage substract(GrayLevelImage image1, GrayLevelImage image2) {
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image1.getWidth(), image1.getHeight());
		for (int i = 0; i < image1.getWidth(); ++i) {
			for (int j = 0; j < image1.getHeight(); ++j) {
				int val = image1.getPixel(i, j) - image2.getPixel(i, j);
				if (val < 0) val = 0;
				if (val > 255) val = 255;
				result.setPixel(i, j, val);
			}
		}
		return result;
	}
	
	public static SimpleGrayLevelImage rescale(GrayLevelImage image) {
		int valmax = 0;
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int val = image.getPixel(i, j); 
				if (val > valmax) valmax = val;
			}
		}
		if (valmax == 0) return result;
		double scale = 255.0 / valmax;
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				double newval = image.getPixel(i, j) * scale;
				result.setPixel(i, j, (int)(newval + 0.5));
			}
		}
		return result;
	}
	
	public static SimpleGrayLevelImage toBinary(GrayLevelImage image) {
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int val = image.getPixel(i, j); 
				result.setPixel(i, j, val == 0 ? 0 : 255);
			}
		}
		return result;
	}
	
	public static final GrayLevelImage NULL_IMAGE = new GrayLevelImage() {
		public int getWidth() { return 0; }
		public int getHeight() { return 0; }
		public int getPixel(int x, int y) { throw new IndexOutOfBoundsException(); }
	};
	

	public static FocusListener SEL_ALL_LISTENER = new FocusAdapter() {
		public void focusGained(FocusEvent arg0) {
			JTextField text = (JTextField)arg0.getComponent();
			text.selectAll();
		}
	};
}
