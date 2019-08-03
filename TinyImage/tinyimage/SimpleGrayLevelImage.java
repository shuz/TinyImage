package tinyimage;

import java.awt.image.BufferedImage;

public class SimpleGrayLevelImage implements GrayLevelImage {

	private int width, height;
	private int [] pixel;
	
	public SimpleGrayLevelImage(int w, int h) {
		width = w;
		height = h;
		pixel = new int[w*h];
	}
	
	public SimpleGrayLevelImage(GrayLevelImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixel = new int[width * height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				setPixel(i, j, image.getPixel(i, j));
			}
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPixel(int x, int y) {
		return pixel[y*width+x];
	}

	public void setPixel(int x, int y, int value) {
		pixel[y*width+x] = value;
	}
	
	public BufferedImage toBufferedImage() {
		return toBufferedImage(BufferedImage.TYPE_USHORT_GRAY);
	}

	public BufferedImage toBufferedImage(int type) {
		BufferedImage fimage = new BufferedImage(width, height, type);
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				int val = pixel[j*width+i];
				fimage.setRGB(i, j, (val << 16) | (val << 8) | val);
			}
		}
		return fimage;
	}
}
