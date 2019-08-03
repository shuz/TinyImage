package tinyimage.morphology;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import tinyimage.*;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public final class MorphologyController implements ImageController {
	
	private static final String SPATIAL_FILTER_CARD = "Morphology";
	
	private JPanel jSpatialFilterPanel;  //  @jve:decl-index=0:visual-constraint="5,202"
	private JPanel jPredefinedKernelPanel;
	private JKernelPanel jKernelPanel;

	private SimpleGrayLevelImage mask;
	private BufferedImage imageBeforeMasking;
	
	private MouseMotionListener maskingMouseListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
			Point p = e.getPoint();
			if (p.x >= callback.getOriginalImage().getWidth() || 
				p.y >= callback.getOriginalImage().getHeight()) return;
			mask.setPixel(p.x, p.y, 255);
			callback.getOriginalImage().setRGB(p.x, p.y, 0xFF0000);	// red
			callback.getOriginalImagePanel().updateUI();
		}
	};
	
	private ActionListener dilatePerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.dilate(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}
	};
	
	private ActionListener erodePerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.erode(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}
	};
	
	private ActionListener openingPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.opening(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}
	};
	
	private ActionListener closingPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.closing(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}
	};
	
	private ActionListener skeletonPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			if (getKernelPanel().getKernelXDimension() == 1 ||
				getKernelPanel().getKernelYDimension() == 1) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.skeletonByDistanceTransform(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}		
	};

	private ActionListener skeletonByErosionPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			if (getKernelPanel().getKernelXDimension() == 1 ||
				getKernelPanel().getKernelYDimension() == 1) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.skeleton(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}		
	};
	
	private ActionListener distanceTransformPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getOriginalImage() == null) return;
			if (getKernelPanel().getKernelXDimension() == 1 ||
				getKernelPanel().getKernelYDimension() == 1) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.distanceTransform(TinyImageUtils.getGraylevelImage(callback.getOriginalImage())).toBufferedImage());
		}
	};

	private ActionListener skeletonRestorationPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getTransformedImage() == null) return;
			if (getKernelPanel().getKernelXDimension() == 1 ||
				getKernelPanel().getKernelYDimension() == 1) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(filter.skeletonRestoration(TinyImageUtils.getGraylevelImage(callback.getTransformedImage())).toBufferedImage());
		}
	};
	
	private ActionListener substractPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getOriginalImage() == null) return;
			if (callback.getTransformedImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			callback.setTransformedImage(TinyImageUtils.substract(TinyImageUtils.getGraylevelImage(callback.getOriginalImage()), TinyImageUtils.getGraylevelImage(callback.getTransformedImage())).toBufferedImage());
		}
	};

	private ActionListener negatePerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getTransformedImage() == null) return;
			callback.setTransformedImage(TinyImageUtils.negate(TinyImageUtils.getGraylevelImage(callback.getTransformedImage())).toBufferedImage());
		}
	};

	private ActionListener rescalePerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getTransformedImage() == null) return;
			callback.setTransformedImage(TinyImageUtils.rescale(TinyImageUtils.getGraylevelImage(callback.getTransformedImage())).toBufferedImage());
		}
	};

	private ActionListener toBinaryPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getTransformedImage() == null) return;
			callback.setTransformedImage(TinyImageUtils.toBinary(TinyImageUtils.getGraylevelImage(callback.getTransformedImage())).toBufferedImage());
		}
	};
	
	private ActionListener edgeDetectPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			GrayLevelImage image = TinyImageUtils.getGraylevelImage(callback.getOriginalImage());
			callback.setTransformedImage(TinyImageUtils.substract(filter.dilate(image), filter.erode(image)).toBufferedImage());
		}
	};

	private ActionListener grayscaleReconstructPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getOriginalImage() == null) return;
			if (callback.getTransformedImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			GrayLevelImage m = TinyImageUtils.getGraylevelImage(callback.getTransformedImage());
			GrayLevelImage v = TinyImageUtils.getGraylevelImage(callback.getOriginalImage());
			
			callback.setTransformedImage(filter.grayscaleReconstruct(m, v).toBufferedImage());
		}
	};
	
	private ActionListener gradientPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			GrayLevelImage image = TinyImageUtils.getGraylevelImage(callback.getOriginalImage());
			callback.setTransformedImage(filter.gradient(image).toBufferedImage());
		}
	};

	
	public JPanel getPanel() {
		if (jSpatialFilterPanel == null) {
			jSpatialFilterPanel = new JPanel();
			jSpatialFilterPanel.setLayout(new BorderLayout());
			jSpatialFilterPanel.add(getJScrollPane(), java.awt.BorderLayout.EAST);
			jSpatialFilterPanel.add(getPredefinedKernelPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jSpatialFilterPanel;
	}
	
	private JKernelPanel getKernelPanel() {
		if (jKernelPanel == null) {
			jKernelPanel = new JKernelPanel();
		}
		return jKernelPanel;
	}
	
	private JPanel getPredefinedKernelPanel() {
		if (jPredefinedKernelPanel == null) {
			jPredefinedKernelPanel = new JPanel();
			GridLayout gridlayout = new GridLayout();
			gridlayout.setColumns(1);
			gridlayout.setRows(7);
			jPredefinedKernelPanel.setLayout(gridlayout);
			jPredefinedKernelPanel.add(getJSubstractPanel(), null);
			jPredefinedKernelPanel.add(getJRobertKernelPanel(), null);
			jPredefinedKernelPanel.add(getJPrewittKernelPanel(), null);
			jPredefinedKernelPanel.add(getJSobelKernelPanel(), null);
			jPredefinedKernelPanel.add(getJNegatePanel(), null);
			jPredefinedKernelPanel.add(getJGaussKernelPanel(), null);
			jPredefinedKernelPanel.add(getJScalePanel(), null);
		}
		return jPredefinedKernelPanel;
	}

	public String getPanelName() {
		return SPATIAL_FILTER_CARD;
	}

	public void setAppCallback(TinyImageCallback callback) {
		this.callback = callback;
	}

	public void onOriginalImageChange(BufferedImage image) {
		// nothing to do
	}

	private TinyImageCallback callback;

	private JButton jRobertKernelX = null;

	private JButton jPrewittKernelX = null;

	private JPanel jPrewittKernelPanel = null;

	private JPanel jSobelKernelPanel = null;

	private JPanel jGaussKernelPanel = null;

	private JButton jSobelKernelX = null;

	private JButton jGaussKernel = null;

	private JPanel jRobertKernelPanel = null;

	private JButton jRobertKernelY = null;

	private JButton jPrewittKernelY = null;

	private JButton jSobelKernelY = null;

	private JPanel jNegatePanel = null;

	private JPanel jSubstractPanel = null;

	private JButton jSubstractButton = null;

	private JButton jNegateButton = null;

	private JButton jSkeletonByErosionButton = null;

	private JPanel jScalePanel = null;

	private JButton jRescaleButton = null;

	private JButton jEdgeDetectButton = null;

	private JButton jToBinaryButton = null;

	private JButton jGrayScaleReconstructionButton = null;

	private JButton jGradientButton = null;

	private JScrollPane jScrollPane = null;

	private JButton jConditionalDilateStartButton = null;

	private JButton jConditionalDilateEndButton = null;

	private JLabel jConditionalDilateLabel = null;

	/**
	 * This method initializes jRobertKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRobertKernelX() {
		if (jRobertKernelX == null) {
			jRobertKernelX = new JButton();
			jRobertKernelX.setText("Dilate");
			jRobertKernelX.addActionListener(dilatePerformer);
		}
		return jRobertKernelX;
	}

	/**
	 * This method initializes jPrewittKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJPrewittKernelX() {
		if (jPrewittKernelX == null) {
			jPrewittKernelX = new JButton();
			jPrewittKernelX.setText("Open");
			jPrewittKernelX.addActionListener(openingPerformer);
		}
		return jPrewittKernelX;
	}

	/**
	 * This method initializes jRobertKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPrewittKernelPanel() {
		if (jPrewittKernelPanel == null) {
			jPrewittKernelPanel = new JPanel();
			jPrewittKernelPanel.add(getJSobelKernelX(), null);
			jPrewittKernelPanel.add(getJSkeletonByErosionButton(), null);
		}
		return jPrewittKernelPanel;
	}

	/**
	 * This method initializes jPrewittKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSobelKernelPanel() {
		if (jSobelKernelPanel == null) {
			jSobelKernelPanel = new JPanel();
			jSobelKernelPanel.add(getJSobelKernelY(), null);
		}
		return jSobelKernelPanel;
	}

	/**
	 * This method initializes jGaussKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJGaussKernelPanel() {
		if (jGaussKernelPanel == null) {
			jGaussKernelPanel = new JPanel();
			jGaussKernelPanel.add(getJEdgeDetectButton(), null);
			jGaussKernelPanel.add(getJGradientButton(), null);
		}
		return jGaussKernelPanel;
	}

	/**
	 * This method initializes jSobelKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSobelKernelX() {
		if (jSobelKernelX == null) {
			jSobelKernelX = new JButton();
			jSobelKernelX.setText("Skeleton");
			jSobelKernelX.setPreferredSize(new java.awt.Dimension(100,26));
			jSobelKernelX.addActionListener(skeletonPerformer);
		}
		return jSobelKernelX;
	}

	/**
	 * This method initializes jGaussKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJGaussKernel() {
		if (jGaussKernel == null) {
			jGaussKernel = new JButton();
			jGaussKernel.setText("Distance Transform");
			jGaussKernel.addActionListener(distanceTransformPerformer);
		}
		return jGaussKernel;
	}

	/**
	 * This method initializes jBlankPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJRobertKernelPanel() {
		if (jRobertKernelPanel == null) {
			jRobertKernelPanel = new JPanel();
			jRobertKernelPanel.add(getJRobertKernelX(), null);
			jRobertKernelPanel.add(getJRobertKernelY(), null);
			jRobertKernelPanel.add(getJPrewittKernelX(), null);
			jRobertKernelPanel.add(getJPrewittKernelY(), null);
		}
		return jRobertKernelPanel;
	}

	/**
	 * This method initializes jRobertKernelY	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRobertKernelY() {
		if (jRobertKernelY == null) {
			jRobertKernelY = new JButton();
			jRobertKernelY.setText("Erode");
			jRobertKernelY.addActionListener(erodePerformer);
		}
		return jRobertKernelY;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJPrewittKernelY() {
		if (jPrewittKernelY == null) {
			jPrewittKernelY = new JButton();
			jPrewittKernelY.setText("Close");
			jPrewittKernelY.addActionListener(closingPerformer);
		}
		return jPrewittKernelY;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSobelKernelY() {
		if (jSobelKernelY == null) {
			jSobelKernelY = new JButton();
			jSobelKernelY.setText("Skelton Restoration");
			jSobelKernelY.setPreferredSize(new java.awt.Dimension(150,26));
			jSobelKernelY.addActionListener(skeletonRestorationPerformer);
		}
		return jSobelKernelY;
	}

	/**
	 * This method initializes jNegatePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNegatePanel() {
		if (jNegatePanel == null) {
			jNegatePanel = new JPanel();
			jNegatePanel.add(getJGaussKernel(), null);
			jNegatePanel.add(getJGrayScaleReconstructionButton(), null);
		}
		return jNegatePanel;
	}

	/**
	 * This method initializes jSubstractPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSubstractPanel() {
		if (jSubstractPanel == null) {
			jSubstractPanel = new JPanel();
			jSubstractPanel.add(getJSubstractButton(), null);
			jSubstractPanel.add(getJNegateButton(), null);
			jSubstractPanel.add(getJToBinaryButton(), null);
			jSubstractPanel.add(getJRescaleButton(), null);
		}
		return jSubstractPanel;
	}

	/**
	 * This method initializes jSubstractButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSubstractButton() {
		if (jSubstractButton == null) {
			jSubstractButton = new JButton();
			jSubstractButton.setText("Sub");
			jSubstractButton.addActionListener(substractPerformer);
		}
		return jSubstractButton;
	}

	/**
	 * This method initializes jNegateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJNegateButton() {
		if (jNegateButton == null) {
			jNegateButton = new JButton();
			jNegateButton.setText("Neg");
			jNegateButton.addActionListener(negatePerformer);
		}
		return jNegateButton;
	}

	/**
	 * This method initializes jSkeletonByDistanceButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSkeletonByErosionButton() {
		if (jSkeletonByErosionButton == null) {
			jSkeletonByErosionButton = new JButton();
			jSkeletonByErosionButton.setText("Skeleton by Subset");
			jSkeletonByErosionButton.addActionListener(skeletonByErosionPerformer);
		}
		return jSkeletonByErosionButton;
	}

	/**
	 * This method initializes jScalePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJScalePanel() {
		if (jScalePanel == null) {
			jConditionalDilateLabel = new JLabel();
			jConditionalDilateLabel.setText("Conditional Dilate");
			jScalePanel = new JPanel();
			jScalePanel.add(jConditionalDilateLabel, null);
			jScalePanel.add(getJConditionalDilateStartButton(), null);
			jScalePanel.add(getJConditionalDilateEndButton(), null);
		}
		return jScalePanel;
	}

	/**
	 * This method initializes jRescaleButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRescaleButton() {
		if (jRescaleButton == null) {
			jRescaleButton = new JButton();
			jRescaleButton.setText("Rescale");
			jRescaleButton.addActionListener(rescalePerformer);
		}
		return jRescaleButton;
	}

	/**
	 * This method initializes jEdgeDetectButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJEdgeDetectButton() {
		if (jEdgeDetectButton == null) {
			jEdgeDetectButton = new JButton();
			jEdgeDetectButton.setText("Edge Detect");
			jEdgeDetectButton.addActionListener(edgeDetectPerformer);
		}
		return jEdgeDetectButton;
	}

	/**
	 * This method initializes jToBinaryButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJToBinaryButton() {
		if (jToBinaryButton == null) {
			jToBinaryButton = new JButton();
			jToBinaryButton.setText("Binary");
			jToBinaryButton.addActionListener(toBinaryPerformer);
		}
		return jToBinaryButton;
	}

	/**
	 * This method initializes jGrayScaleReconstructionButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJGrayScaleReconstructionButton() {
		if (jGrayScaleReconstructionButton == null) {
			jGrayScaleReconstructionButton = new JButton();
			jGrayScaleReconstructionButton.setText("Reconstruct");
			jGrayScaleReconstructionButton.addActionListener(grayscaleReconstructPerformer);
		}
		return jGrayScaleReconstructionButton;
	}

	/**
	 * This method initializes jGradientButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJGradientButton() {
		if (jGradientButton == null) {
			jGradientButton = new JButton();
			jGradientButton.setText("Gradient");
			jGradientButton.addActionListener(gradientPerformer);
		}
		return jGradientButton;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setPreferredSize(new java.awt.Dimension(300,250));
			jScrollPane.setViewportView(getKernelPanel());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jConditionalDilateStartButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJConditionalDilateStartButton() {
		if (jConditionalDilateStartButton == null) {
			jConditionalDilateStartButton = new JButton();
			jConditionalDilateStartButton.setText("Select Mask");
			jConditionalDilateStartButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (jConditionalDilateStartButton.getText().equals("Select Mask")) {
								jConditionalDilateStartButton.setText("Cancel Mask");
								mask = new SimpleGrayLevelImage(callback.getOriginalImage().getWidth(), callback.getOriginalImage().getHeight());
								imageBeforeMasking = callback.getOriginalImage();
								callback.setOriginalImage(new SimpleGrayLevelImage(TinyImageUtils.getGraylevelImage(imageBeforeMasking)).toBufferedImage(BufferedImage.TYPE_INT_RGB));
								callback.getOriginalImagePanel().addMouseMotionListener(maskingMouseListener);
							} else {
								jConditionalDilateStartButton.setText("Select Mask");
								mask = null;
								callback.getOriginalImagePanel().removeMouseMotionListener(maskingMouseListener);
								callback.setOriginalImage(imageBeforeMasking);
							}
						
						}
					});
		}
		return jConditionalDilateStartButton;
	}

	/**
	 * This method initializes jConditionalDilateEndButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJConditionalDilateEndButton() {
		if (jConditionalDilateEndButton == null) {
			jConditionalDilateEndButton = new JButton();
			jConditionalDilateEndButton.setText("Perform");
			jConditionalDilateEndButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (mask == null) return;
							if (callback.getOriginalImage() == null) return;
							KernelFilter filter = getKernelPanel().getFilter();
							if (filter == null) return;
							callback.setTransformedImage(filter.grayscaleReconstruct(mask, TinyImageUtils.getGraylevelImage(imageBeforeMasking)).toBufferedImage());
							jConditionalDilateStartButton.setText("Select Mask");
							mask = null;
							callback.getOriginalImagePanel().removeMouseMotionListener(maskingMouseListener);
							callback.setOriginalImage(imageBeforeMasking);
						}
					});
		}
		return jConditionalDilateEndButton;
	}
}
