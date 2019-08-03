package tinyimage.histogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import tinyimage.GrayLevelImage;

public class JHistogramPanel extends JPanel {
	private static final long serialVersionUID = 3657479078497481618L;
	private static final int MARGIN = 10;
	private static final int BARPOS_MARGIN = 20;
	private static final int HEIGHT = 100;
	private static final int LEVELS = 0x100;
	
	private int height = HEIGHT;
	
	public JHistogramPanel() {}
	
	public JHistogramPanel(GrayLevelImage image, int height) {
		this(image);
		this.height = height;
	}
	
	public JHistogramPanel(GrayLevelImage image) {
		this.image = image;
		
		computeHistogram(image);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				handleMouseEvent(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				handleMouseEvent(e);
			}
		});
		this.setBackground(Color.WHITE);
	}

	public static interface BarMovedListener {
		void barMoved(BarMovedEvent e);
	}
	
	public static class BarMovedEvent {
		public BarMovedEvent(int pos) {
			barPosition = pos;
		}
		
		private int barPosition;
		
		public int getBarPosition() {
			return barPosition;
		}
	}
	
	public void addBarMovedListener(BarMovedListener listener) {
		barMovedListeners.addLast(listener);
	}
	
	public int getBarPosition() {
		return barPosition;
	}
	
	public void setBarPosition(int pos) {
		barPosition = pos;
		raiseBarMovedEvent();
		repaint();	
	}
	
	public GrayLevelImage getImage() {
		return image;
	}
	
	public void computeHistogram(GrayLevelImage image) {
		this.image = image;
		
		maxHeight = 0;
		histogram = new int[LEVELS];
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int grayLevel = image.getPixel(i, j);
				int count = ++histogram[grayLevel];
				if (count > maxHeight) maxHeight = count;
			}
		}
		
		setPreferredSize(new Dimension(histogram.length + 2*MARGIN, height + 2*MARGIN + BARPOS_MARGIN));
		
		revalidate();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle rect = getBounds();
		
		// Draw the histogram
		g.setColor(Color.BLUE);
		for (int i = 0; i < histogram.length; ++i) {
			g.drawLine(MARGIN + i, rect.height - MARGIN, MARGIN + i, rect.height - MARGIN - round(histogram[i] / (double)maxHeight * height));
		}
		
		// Draw the split bar
		g.setColor(Color.YELLOW);
		g.drawLine(MARGIN + barPosition, rect.height - MARGIN, MARGIN + barPosition, MARGIN + BARPOS_MARGIN);
		
		Rectangle2D bounds = getBarStringBounds();
		g.setColor(Color.BLUE);
		g.drawString(String.valueOf(barPosition), MARGIN + barPosition - round(bounds.getWidth() / 2), round(MARGIN + BARPOS_MARGIN + bounds.getHeight())/2);
	}
	
	public int[] getHistogram() {
		return histogram;
	}
	
	private Rectangle2D getBarStringBounds() {
		return getFontMetrics(getFont()).getStringBounds(String.valueOf(barPosition), getGraphics());
	}
	
	private int round(double d) {
		return (int)(d + 0.5);
	}
	
	private void handleMouseEvent(MouseEvent e) {
		int pos = e.getX() - MARGIN;
		
		if (pos >= histogram.length) pos = histogram.length - 1;
		else if (pos < 0) pos = 0;
		
		setBarPosition(pos);
	}
	
	private void raiseBarMovedEvent() {
		BarMovedEvent event = new BarMovedEvent(barPosition);
		Iterator itor = barMovedListeners.iterator();
		while (itor.hasNext()) {
			BarMovedListener listener = (BarMovedListener)itor.next();
			listener.barMoved(event);
		}
	}
	
	private GrayLevelImage image;
	private int[] histogram;
	private int maxHeight;
	private int barPosition;
	private LinkedList barMovedListeners = new LinkedList();
}
