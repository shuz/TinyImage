package tinyimage.spatialfilter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import tinyimage.TinyImageUtils;

public class MedianFilter {

	public MedianFilter(int dim, BufferedImage image) {
		this.dimension = dim;
		this.image = image;
	}
	
	public BufferedImage filter(int imageType) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		BufferedImage fimage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int rgb = filterPixel(i, j);
				fimage.setRGB(i, j, rgb);
			}
		}
		return fimage;
	}
	
	public int filterPixel(int x, int y) {
		double [] r = new double[dimension * dimension];
		double [] g = new double[dimension * dimension];
		double [] b = new double[dimension * dimension];
		int idx = 0;
		for (int i = getMin(); i < getMax(); ++i) {
			for (int j = getMin(); j < getMax(); ++j, ++idx) {
				int rgb = readImage(x - i, y - j);
				r[idx] = (rgb >> 16) & 0xFF;
				g[idx] = (rgb >>  8) & 0xFF;
				b[idx] = (rgb >>  0) & 0xFF;
			}
		}
		Arrays.sort(r);
		Arrays.sort(g);
		Arrays.sort(b);

		int mid = dimension*dimension / 2;
		return ((int)r[mid] << 16) | ((int)g[mid] << 8) | ((int)b[mid]);
	}
	
	private int getMin() {
		return -dimension/2;
	}
	
	private int getMax() {
		return dimension - dimension/2;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	private int readImage(int x, int y) {
		if (0 <= x && x < image.getWidth() && 0 <= y && y < image.getHeight()) {
			return image.getRGB(x, y);
		} else {
			if (x < 0) x = 0;
			else if (x >= image.getWidth()) x = image.getWidth() - 1;
			if (y < 0) y = 0;
			else if (y >= image.getHeight()) y = image.getHeight() - 1;
			return image.getRGB(x, y);
		}
	}

	private int dimension;
	private BufferedImage image;
}
