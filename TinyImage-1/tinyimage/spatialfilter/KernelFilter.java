package tinyimage.spatialfilter;

import java.awt.image.BufferedImage;

import tinyimage.*;

public final class KernelFilter {
	
	public KernelFilter(int dim) {
		this.dimension = dim;
		this.kernel = new double[dim*dim];
	}
	
	public KernelFilter(int dim, BufferedImage image) {
		this(dim);
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
		double sumr = 0;
		double sumg = 0;
		double sumb = 0;
		for (int i = getKernelMin(); i < getKernelMax(); ++i) {
			for (int j = getKernelMin(); j < getKernelMax(); ++j) {
				int rgb = readImage(x - i, y - j);
				sumr += ((rgb >> 16) & 0xFF) * getKernel(i, j);
				sumg += ((rgb >>  8) & 0xFF) * getKernel(i, j);
				sumb += ((rgb >>  0) & 0xFF) * getKernel(i, j);
			}
		}

		return (clamp(sumr) << 16) | (clamp(sumg) << 8) | clamp(sumb);
	}
	
	private int clamp(double val) {
		if (val < 0) val = 0;
		else if (val > 0xFF) val = 0xFF;
		return (int)Math.round(val);
	}
	
	public void setKernel(int x, int y, double val) {
		kernel[(x - getKernelMin())*dimension + (y - getKernelMin())] = val;
	}
	
	public double getKernel(int x, int y) {
		return kernel[(x - getKernelMin())*dimension + (y - getKernelMin())];
	}
	
	/**
	 * get the minimum index of kernel, inclusive
	 * @return the minimum index of kernel
	 */
	public int getKernelMin() {
		return -dimension/2;
	}
	
	/**
	 * get the maximum index of kernel, exclusive
	 * @return the maximum index of kernel
	 */
	public int getKernelMax() {
		return dimension - dimension/2;
	}
	
	public int getKernelDimension() {
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
	private double [] kernel;
	private BufferedImage image;
}
