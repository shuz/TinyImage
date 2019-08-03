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
