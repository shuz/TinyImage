package tinyimage.morphology;

import tinyimage.*;

public final class KernelFilter {
	
	public KernelFilter(int xdim, int ydim) {
		this.xdim = xdim;
		this.ydim = ydim;
		this.kernel = new int[xdim*ydim];
		this.kernelMask = new boolean[xdim*ydim];
		xmin = -xdim/2;
		ymin = -ydim/2;
		for (int i = 0; i < xdim*ydim; ++i) {
			kernelMask[i] = true;
		}
	}
	
	public KernelFilter(int xdim, int ydim, GrayLevelImage image) {
		this(xdim, ydim);
	}
	
	public SimpleGrayLevelImage skeleton(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		boolean isEmpty;
		int xmax, xmin, ymax, ymin;
		xmin = ymin = 0;
		xmax = image.getWidth() - 1;
		ymax = image.getHeight() - 1;
		SimpleGrayLevelImage fimage = null;
		SimpleGrayLevelImage ffimage = erode(image);
		do {
			isEmpty = true;
			int xxmin, xxmax, yymin, yymax;
			xxmin = xmax;  xxmax = xmin;
			yymin = ymax;  yymax = ymin;
			
			fimage = ffimage;
			ffimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
			for (int i = xmin; i <= xmax; ++i) {
				for (int j = ymin; j <= ymax; ++j) {
					int rgb = erodePixel(i, j, fimage);
					if (rgb > 0) {
						ffimage.setPixel(i, j, 255);
						if (i > xxmax) xxmax = i;
						if (i < xxmin) xxmin = i;
						if (j < yymin) yymin = j;
						if (j > yymax) yymax = j;
						isEmpty = false;
					}
				}
			}
			
			for (int i = xmin; i <= xmax; ++i) {
				for (int j = ymin; j <= ymax; ++j) {
					int rgb = dilatePixel(i, j, ffimage) == 0 ? 0 : 255;
					rgb = (fimage.getPixel(i, j) == 0 ? 0 : 255) - rgb;
					if (rgb > 0) {
						result.setPixel(i, j, 255);
					}
				}
			}
			xmin = xxmin;  xmax = xxmax;
			ymin = yymin;  ymax = yymax;
		} while (!isEmpty);
		return result;
	}
	
	public SimpleGrayLevelImage distanceTransform(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		boolean isEmpty;
		int xmax, xmin, ymax, ymin;
		xmin = ymin = 0;
		xmax = image.getWidth() - 1;
		ymax = image.getHeight() - 1;
		int valmax = 0;
		do {
			SimpleGrayLevelImage fimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
			isEmpty = true;
			int xxmin, xxmax, yymin, yymax;
			xxmin = xmax;  xxmax = xmin;
			yymin = ymax;  yymax = ymin;
			for (int i = xmin; i <= xmax; ++i) {
				for (int j = ymin; j <= ymax; ++j) {
					int rgb = erodePixel(i, j, image);
					fimage.setPixel(i, j, rgb);
					if (rgb != 0) {
						if (i > xxmax) xxmax = i;
						if (i < xxmin) xxmin = i;
						if (j < yymin) yymin = j;
						if (j > yymax) yymax = j;
						isEmpty = false;
						int value = result.getPixel(i, j) + 1;
						if (value > valmax) valmax = value;
						result.setPixel(i, j, value);
					}
				}
			}
			xmin = xxmin;  xmax = xxmax;
			ymin = yymin;  ymax = yymax;
			image = fimage;
		} while (!isEmpty);
		
		return result;
	}
	
	public SimpleGrayLevelImage skeletonByDistanceTransform(GrayLevelImage image) {
		SimpleGrayLevelImage dtimage = distanceTransform(image);
		SimpleGrayLevelImage result = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				result.setPixel(i, j, skeletonPixelValue(i, j, dtimage));
			}
		}
		return result;
	}
	
	public int skeletonPixelValue(int x, int y, GrayLevelImage dtimage) {
		int val = dtimage.getPixel(x, y);
		boolean isMax = false;
		for (int i = getKernelXMin(); i < getKernelXMax(); ++i) {
			for (int j = getKernelYMin(); j < getKernelYMax(); ++j) {
				if (getKernelMask(i, j)) {
					int rgb = readImage(x + i, y + j, dtimage);
					if (val < rgb) return 0;
					if (val > rgb) isMax = true;
				}
			}
		}
		return isMax ? val : 0;
	}
	
	public SimpleGrayLevelImage skeletonRestoration(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage fimage = new SimpleGrayLevelImage(image);
		int valmax = 0;
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int val = image.getPixel(i, j);
				if (val > valmax) valmax = val;
			}
		}
		for (int k = valmax; k > 0; --k) {
			SimpleGrayLevelImage ffimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
			for (int i = 0; i < image.getWidth(); ++i) {
				for (int j = 0; j < image.getHeight(); ++j) {
					boolean found = false;
					kernelfor:
					for (int ki = getKernelXMin(); ki < getKernelXMax(); ++ki) {
						for (int kj = getKernelYMin(); kj < getKernelYMax(); ++kj) {
							if (getKernelMask(ki, kj)) {
								int rgb = readImage(i - ki, j - kj, fimage);
								if (rgb >= k) {
									found = true;
									break kernelfor;
								}
							}
						}
					}
					if (found) ffimage.setPixel(i, j, k);
					else ffimage.setPixel(i, j, fimage.getPixel(i, j));
				}
			}
			fimage = ffimage;
		}
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int val = fimage.getPixel(i, j);
				if (val > 0) fimage.setPixel(i, j, 255);
			}
		}
		return fimage;
	}
	
	public SimpleGrayLevelImage gradient(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage fimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int rgb1 = erodePixel(i, j, image);
				int rgb2 = dilatePixel(i, j, image);
				fimage.setPixel(i, j, clamp((rgb2 - rgb1)/2));
			}
		}
		return fimage;
	}
	
	public SimpleGrayLevelImage grayscaleReconstruct(GrayLevelImage g, GrayLevelImage f) {
		//		Step1:  perform grayscale dilation on marker g.
		//		Step2:  check every gray value in dilated result is not exceed the restriction of mask f.
		//		Step3:  repeat step1 and 2, until g getting stable.

		GrayLevelImage t;
		boolean changed;
		SimpleGrayLevelImage m = new SimpleGrayLevelImage(g);
		
		do {
			changed = false;
			t = m;
			m = new SimpleGrayLevelImage(m.getWidth(), m.getHeight());
			for (int i = 0; i < m.getWidth(); ++i) {
				for (int j = 0; j < m.getHeight(); ++j) {
					int rgb = dilatePixel(i, j, t);
					if (rgb != 0) {
						int rgbv = f.getPixel(i, j);
						if (rgb > rgbv) {
							rgb = rgbv;
						}
					}
					m.setPixel(i, j, rgb);
					int tval = t.getPixel(i, j);
					if (tval != rgb) {
						changed = true;
					}
				}
			}
		} while (changed);
		return m;
	}
	
	public SimpleGrayLevelImage opening(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
	    return dilate(erode(image));
	}

	public SimpleGrayLevelImage closing(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
	    return erode(dilate(image));
	}
	
	public SimpleGrayLevelImage dilate(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage fimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int rgb = dilatePixel(i, j, image);
				fimage.setPixel(i, j, rgb);
			}
		}
		return fimage;
	}
	
	public int dilatePixel(int x, int y, GrayLevelImage image) {
		int max = Integer.MIN_VALUE;
		for (int i = getKernelXMin(); i < getKernelXMax(); ++i) {
			for (int j = getKernelYMin(); j < getKernelYMax(); ++j) {
				if (getKernelMask(i, j)) {
					int rgb = readImage(x - i, y - j, image);
					max = Math.max(max, ((rgb >>  0) & 0xFF) + getKernel(i, j));
					if (max == 255) return 255;
				}
			}
		}
		return clamp(max);
	}
	

	public SimpleGrayLevelImage erode(GrayLevelImage image) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		SimpleGrayLevelImage fimage = new SimpleGrayLevelImage(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int rgb = erodePixel(i, j, image);
				fimage.setPixel(i, j, rgb);
			}
		}
		return fimage;
	}
	
	public int erodePixel(int x, int y, GrayLevelImage image) {
		int min = Integer.MAX_VALUE;
		for (int i = getKernelXMin(); i < getKernelXMax(); ++i) {
			for (int j = getKernelYMin(); j < getKernelYMax(); ++j) {
				if (getKernelMask(i, j)) {
					int rgb = readImage(x + i, y + j, image);
					min = Math.min(min, ((rgb >>  0) & 0xFF) - getKernel(i, j));
					if (min == 0) return 0;
				}
			}
		}
		return clamp(min);
	}
	
	private int clamp(double val) {
		if (val < 0) val = 0;
		else if (val > 0xFF) val = 0xFF;
		return (int)Math.round(val);
	}
	
	public void setKernel(int x, int y, int val) {
		kernel[(x - xmin) + (y - ymin)*xdim] = val;
	}
	
	public int getKernel(int x, int y) {
		return kernel[(x - xmin) + (y - ymin)*xdim];
	}
	
	public void setKernelMask(int x, int y, boolean val) {
		kernelMask[(x - xmin) + (y - ymin)*xdim] = val;
	}
	
	public boolean getKernelMask(int x, int y) {
		return kernelMask[(x - xmin) + (y - ymin)*xdim];
	}
	
	
	public void setKernelXOrg(int xorg) {
		if (xorg < 0 || xorg >= xdim) {
			throw new IllegalArgumentException();
		}
		xmin = -xorg;
	}
	
	public void setKernelYOrg(int yorg) {
		if (yorg < 0 || yorg >= ydim) {
			throw new IllegalArgumentException();
		}
		ymin = -yorg;
	}
	
	/**
	 * get the x minimum index of kernel, inclusive
	 * @return the minimum index of kernel
	 */
	public int getKernelXMin() {
		return xmin;
	}
	
	/**
	 * get the y minimum index of kernel, inclusive
	 * @return the minimum index of kernel
	 */
	public int getKernelYMin() {
		return ymin;
	}
	
	/**
	 * get the x maximum index of kernel, exclusive
	 * @return the maximum index of kernel
	 */
	public int getKernelXMax() {
		return xdim + xmin;
	}
	
	/**
	 * get the y maximum index of kernel, exclusive
	 * @return the maximum index of kernel
	 */
	public int getKernelYMax() {
		return ydim + ymin;
	}
	
	public int getKernelXDimension() {
		return xdim;
	}

	public int getKernelYDimension() {
		return ydim;
	}
	
	private static int readImage(int x, int y, GrayLevelImage image) {
		if (0 <= x && x < image.getWidth() && 0 <= y && y < image.getHeight()) {
			return image.getPixel(x, y);
		} else {
			//if (x < 0) x = 0;
			//else if (x >= image.getWidth()) x = image.getWidth() - 1;
			//if (y < 0) y = 0;
			//else if (y >= image.getHeight()) y = image.getHeight() - 1;
			// TODO:
			if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) return 0;
			return image.getPixel(x, y);
		}
	}

	private int xdim, ydim;
	private int xmin, ymin;
	private int [] kernel;
	private boolean [] kernelMask;
}
