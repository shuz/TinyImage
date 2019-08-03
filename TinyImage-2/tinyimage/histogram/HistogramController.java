package tinyimage.histogram;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tinyimage.*;
import tinyimage.histogram.JHistogramPanel.BarMovedEvent;
import tinyimage.histogram.JHistogramPanel.BarMovedListener;

public final class HistogramController implements ImageController {

	private static final String HISTOGRAM_CARD = "Histogram";
	private static final String GRAY_LEVEL = "Gray Level";
	private static final String RED = "Red";
	private static final String GREEN = "Green";
	private static final String BLUE = "Blue";
	
	private JPanel jHistogramPanel;
	private JPanel jHistogramArea;
	private JPanel jChannelSelectPanel;
	private JPanel jAutoSelectButtonPanel;
	private JPanel jActiveChannelPanel;
	private JLabel jLabelActiveChannel;
	private JButton jOtsuSelect;
	private JButton jEntropySelect;
	private JComboBox jComboActiveChannel;
	
	private JTitledHistogram jHistogramChannel1;
	private JTitledHistogram jHistogramChannel2;
	private JTitledHistogram jHistogramChannel3;
	private JTitledHistogram jHistogramChannel4;
	
	public JPanel getPanel() {
		if (jHistogramPanel == null) {
			jHistogramPanel = new JPanel();
			java.awt.BorderLayout layBorderLayout_4 = new java.awt.BorderLayout();
			jHistogramPanel.setLayout(layBorderLayout_4);
			jHistogramPanel.setPreferredSize(getJHistogramArea().getPreferredSize());
			jHistogramPanel.add(getJChannelSelectPanel(), java.awt.BorderLayout.NORTH);
			jHistogramPanel.add(getJHistogramArea(), java.awt.BorderLayout.CENTER);
		}
		return jHistogramPanel;
	}

	private JPanel getJChannelSelectPanel() {
		if (jChannelSelectPanel == null) {
			jChannelSelectPanel = new JPanel();
			jChannelSelectPanel.setLayout(new BoxLayout(jChannelSelectPanel, BoxLayout.Y_AXIS));
			jChannelSelectPanel.add(getJAutoSelectButtonPanel(), null);
			jChannelSelectPanel.add(getJActiveChannelPanel(), null);
		}
		return jChannelSelectPanel;
	}
	
	private JPanel getJHistogramArea() {
		final int MARGIN = 5;
		if (jHistogramArea == null) {
			jHistogramArea = new JPanel();
			BoxLayout boxLayout = new BoxLayout(jHistogramArea, BoxLayout.Y_AXIS);
			jHistogramArea.setLayout(boxLayout);
			jHistogramArea.add(getJHistogramChannel1(), null);
			jHistogramArea.add(getJHistogramChannel2(), null);
			jHistogramArea.add(getJHistogramChannel3(), null);
			jHistogramArea.add(getJHistogramChannel4(), null);
			Dimension size = getJHistogramChannel1().getPreferredSize();
			size.width += MARGIN;
			size.height += MARGIN;
			jHistogramArea.setPreferredSize(size);
		}
		return jHistogramArea;
	}
	
	private JPanel getJAutoSelectButtonPanel() {
		if (jAutoSelectButtonPanel == null) {
			jAutoSelectButtonPanel = new JPanel();
			jAutoSelectButtonPanel.add(getJOtsuSelect(), null);
			jAutoSelectButtonPanel.add(getJEntropySelect(), null);
		}
		return jAutoSelectButtonPanel;
	}

	private JPanel getJActiveChannelPanel() {
		if (jActiveChannelPanel == null) {
			jLabelActiveChannel = new JLabel();
			jLabelActiveChannel.setText("Active Channel");
			jActiveChannelPanel = new JPanel();
			jActiveChannelPanel.add(jLabelActiveChannel, null);
			jActiveChannelPanel.add(getJComboActiveChannel(), null);
		}
		return jActiveChannelPanel;
	}

	private JComboBox getJComboActiveChannel() {
		if (jComboActiveChannel == null) {
			jComboActiveChannel = new JComboBox();
			jComboActiveChannel.addItem(getJHistogramChannel1());
			jComboActiveChannel.addItem(getJHistogramChannel2());
			jComboActiveChannel.addItem(getJHistogramChannel3());
			jComboActiveChannel.addItem(getJHistogramChannel4());
			jComboActiveChannel.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					doUpdate();
				}
			});
		}
		return jComboActiveChannel;
	}
	
	private JButton getJOtsuSelect() {
		if (jOtsuSelect == null) {
			jOtsuSelect = new JButton();
			jOtsuSelect.setText("OTSU");
			jOtsuSelect.setAlignmentX(0.5F);
			jOtsuSelect.setPreferredSize(new java.awt.Dimension(77,26));
			jOtsuSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JHistogramPanel panel = getActiveHistogramPanel();
					panel.setBarPosition(OtsuThresholder.threshold(panel.getHistogram()));
				}
			});
		}
		return jOtsuSelect;
	}

	private JButton getJEntropySelect() {
		if (jEntropySelect == null) {
			jEntropySelect = new JButton();
			jEntropySelect.setText("Entropy");
			jEntropySelect.setAlignmentY(0.5F);
			jEntropySelect.setAlignmentX(0.5F);
			jEntropySelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JHistogramPanel panel = getActiveHistogramPanel();
					panel.setBarPosition(EntropyThresholder.threshold(panel.getHistogram()));
				}
			});
		}
		return jEntropySelect;
	}
	
	private JTitledHistogram getJHistogramChannel1() {
		if (jHistogramChannel1 == null) {
			jHistogramChannel1 = new JTitledHistogram(GRAY_LEVEL, TinyImageUtils.NULL_IMAGE);
			jHistogramChannel1.getHistogram().addBarMovedListener(new BarMovedListener() {
				public void barMoved(BarMovedEvent e) {
					jComboActiveChannel.setSelectedIndex(0);
					doUpdate();
				}
			});
		}
		return jHistogramChannel1;
	}

	private JTitledHistogram getJHistogramChannel2() {
		if (jHistogramChannel2 == null) {
			jHistogramChannel2 = new JTitledHistogram(RED, TinyImageUtils.NULL_IMAGE);
			jHistogramChannel2.getHistogram().addBarMovedListener(new BarMovedListener() {
				public void barMoved(BarMovedEvent e) {
					jComboActiveChannel.setSelectedIndex(1);
					doUpdate();
				}
			});
		}
		return jHistogramChannel2;
	}

	private JTitledHistogram getJHistogramChannel3() {
		if (jHistogramChannel3 == null) {
			jHistogramChannel3 = new JTitledHistogram(GREEN, TinyImageUtils.NULL_IMAGE);
			jHistogramChannel3.getHistogram().addBarMovedListener(new BarMovedListener() {
				public void barMoved(BarMovedEvent e) {
					jComboActiveChannel.setSelectedIndex(2);
					doUpdate();
				}
			});
		}
		return jHistogramChannel3;
	}

	private JTitledHistogram getJHistogramChannel4() {
		if (jHistogramChannel4 == null) {
			jHistogramChannel4 = new JTitledHistogram(BLUE, TinyImageUtils.NULL_IMAGE);
			jHistogramChannel4.getHistogram().addBarMovedListener(new BarMovedListener() {
				public void barMoved(BarMovedEvent e) {
					jComboActiveChannel.setSelectedIndex(3);
					doUpdate();
				}
			});
		}
		return jHistogramChannel4;
	}

	private JHistogramPanel getActiveHistogramPanel() {
		return ((JTitledHistogram)jComboActiveChannel.getSelectedItem()).getHistogram(); 
	}
	
	public String getPanelName() {
		return HISTOGRAM_CARD;
	}

	public void setAppCallback(TinyImageCallback callback) {
		this.callback = callback;
	}
	
	public void onOriginalImageChange(BufferedImage image) {
		jHistogramChannel1.setImage(TinyImageUtils.getGraylevelImage(image));
		jHistogramChannel2.setImage(TinyImageUtils.getRedChannelImage(image));
		jHistogramChannel3.setImage(TinyImageUtils.getGreenChannelImage(image));
		jHistogramChannel4.setImage(TinyImageUtils.getBlueChannelImage(image));
		jComboActiveChannel.setSelectedIndex(0);
	}

	public void doUpdate() {
		JHistogramPanel histogram = getActiveHistogramPanel();
		if (histogram == null) return;
		GrayLevelImage image = histogram.getImage();
		callback.setTransformedImage(toBinaryImage(image, histogram.getBarPosition()));
	}
	
	private BufferedImage toBinaryImage(GrayLevelImage image, int val) {
		if (image == TinyImageUtils.NULL_IMAGE) return null;
		
		BufferedImage bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
				int b = image.getPixel(i, j) > val ? 0xFFFFFF : 0;
				bimage.setRGB(i, j, b);
			}
		}
		return bimage;
	}
	
	private TinyImageCallback callback;
}
