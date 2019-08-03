package tinyimage.spatialfilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import tinyimage.*;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public final class SpatialFilterController implements ImageController {
	
	private static final String SPATIAL_FILTER_CARD = "Spatial Filter";
	
	private JPanel jSpatialFilterPanel;  //  @jve:decl-index=0:visual-constraint="5,202"
	private JPanel jPredefinedKernelPanel;
	private JKernelPanel jKernelPanel;

	private ActionListener filterPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (callback.getOriginalImage() == null) return;
			KernelFilter filter = getKernelPanel().getFilter();
			if (filter == null) return;
			filter.setImage(callback.getOriginalImage());
			int type = callback.getOriginalImage().getType();
			if (type == 0) type = BufferedImage.TYPE_INT_RGB;
			callback.setTransformedImage(filter.filter(type));
		}
	};
	
	public JPanel getPanel() {
		if (jSpatialFilterPanel == null) {
			jSpatialFilterPanel = new JPanel();
			jSpatialFilterPanel.setLayout(new BorderLayout());
			jSpatialFilterPanel.add(getKernelPanel(), java.awt.BorderLayout.NORTH);
			jSpatialFilterPanel.add(getPredefinedKernelPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jSpatialFilterPanel;
	}
	
	private JKernelPanel getKernelPanel() {
		if (jKernelPanel == null) {
			jKernelPanel = new JKernelPanel();
			jKernelPanel.addActionListener(filterPerformer);
		}
		return jKernelPanel;
	}
	
	private JPanel getPredefinedKernelPanel() {
		if (jPredefinedKernelPanel == null) {
			jPredefinedKernelPanel = new JPanel();
			GridLayout gridlayout = new GridLayout();
			gridlayout.setColumns(1);
			gridlayout.setRows(8);
			jPredefinedKernelPanel.setLayout(gridlayout);
			jPredefinedKernelPanel.add(getJIdentityKernelPanel(), null);
			jPredefinedKernelPanel.add(getJMeanKernelPanel(), null);
			jPredefinedKernelPanel.add(getJMedianKernelPanel(), null);
			jPredefinedKernelPanel.add(getJRobertKernelPanel(), null);
			jPredefinedKernelPanel.add(getJPrewittKernelPanel(), null);
			jPredefinedKernelPanel.add(getJSobelKernelPanel(), null);
			jPredefinedKernelPanel.add(getJGaussKernelPanel(), null);
			jGaussErrorInfoLabel = new JLabel();
			jGaussErrorInfoLabel.setForeground(java.awt.Color.red);
			jPredefinedKernelPanel.add(jGaussErrorInfoLabel, null);
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

	private JButton jIdentityKernel = null;

	private JButton jRobertKernelX = null;

	private JButton jPrewittKernelX = null;

	private JPanel jIdentityKernelPanel = null;

	private JPanel jPrewittKernelPanel = null;

	private JPanel jSobelKernelPanel = null;

	private JPanel jGaussKernelPanel = null;

	private JPanel jMeanKernelPanel = null;
	
	private JPanel jMedianKernelPanel = null;

	private JButton jSobelKernelX = null;

	private JButton jGaussKernel = null;

	private JButton jMeanKernel = null;
	
	private JButton jMedianKernel = null;

	private JPanel jRobertKernelPanel = null;

	private JPanel jSigmaPanel = null;

	private JLabel jSigmaLabel = null;

	private JTextField jSigmaTextField = null;

	private JLabel jGaussErrorInfoLabel = null;

	private JButton jRobertKernelY = null;

	private JButton jPrewittKernelY = null;

	private JButton jSobelKernelY = null;

	/**
	 * This method initializes jIdentityKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJIdentityKernel() {
		if (jIdentityKernel == null) {
			jIdentityKernel = new JButton();
			jIdentityKernel.setText("Identity Kernel");
			jIdentityKernel.setPreferredSize(new java.awt.Dimension(132,26));
			jIdentityKernel.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int n = getKernelPanel().getKernelDimension();
					KernelFilter filter = new KernelFilter(n);
					
				    filter.setKernel(0, 0, 1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			
			});
		}
		return jIdentityKernel;
	}

	/**
	 * This method initializes jRobertKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRobertKernelX() {
		if (jRobertKernelX == null) {
			jRobertKernelX = new JButton();
			jRobertKernelX.setText("Robert (X)");
			jRobertKernelX.setPreferredSize(new java.awt.Dimension(100,26));
			jRobertKernelX.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1,  0);
					filter.setKernel( 0, -1,  0);
					filter.setKernel(+1, -1,  0);
					
					filter.setKernel(-1,  0,  0);
					filter.setKernel( 0,  0, -1);
					filter.setKernel(+1,  0,  0);
					
					filter.setKernel(-1, +1,  0);
					filter.setKernel( 0, +1,  0);
					filter.setKernel(+1, +1,  1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
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
			jPrewittKernelX.setText("Prewitt (X)");
			jPrewittKernelX.setPreferredSize(new java.awt.Dimension(100,26));
			jPrewittKernelX.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1, -1);
					filter.setKernel( 0, -1,  0);
					filter.setKernel(+1, -1,  1);
					
					filter.setKernel(-1,  0, -1);
					filter.setKernel( 0,  0,  0);
					filter.setKernel(+1,  0,  1);
					
					filter.setKernel(-1, +1, -1);
					filter.setKernel( 0, +1,  0);
					filter.setKernel(+1, +1,  1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
		}
		return jPrewittKernelX;
	}

	/**
	 * This method initializes jIdentityKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJIdentityKernelPanel() {
		if (jIdentityKernelPanel == null) {
			jIdentityKernelPanel = new JPanel();
			jIdentityKernelPanel.setLayout(new FlowLayout());
			jIdentityKernelPanel.setAlignmentY(0.5f);
			jIdentityKernelPanel.add(getJIdentityKernel(), null);
		}
		return jIdentityKernelPanel;
	}

	/**
	 * This method initializes jRobertKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPrewittKernelPanel() {
		if (jPrewittKernelPanel == null) {
			jPrewittKernelPanel = new JPanel();
			jPrewittKernelPanel.add(getJPrewittKernelX(), null);
			jPrewittKernelPanel.add(getJPrewittKernelY(), null);
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
			jSobelKernelPanel.add(getJSobelKernelX(), null);
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
			jGaussKernelPanel.add(getJGaussKernel(), null);
			jGaussKernelPanel.add(getJSigmaPanel(), null);
		}
		return jGaussKernelPanel;
	}

	/**
	 * This method initializes jMeanKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJMeanKernelPanel() {
		if (jMeanKernelPanel == null) {
			jMeanKernelPanel = new JPanel();
			jMeanKernelPanel.add(getJMeanKernel(), null);
		}
		return jMeanKernelPanel;
	}
	
	private JPanel getJMedianKernelPanel() {
		if (jMedianKernelPanel == null) {
			jMedianKernelPanel = new JPanel();
			jMedianKernelPanel.add(getJMedianKernel(), null);
		}
		return jMedianKernelPanel;
	}

	/**
	 * This method initializes jSobelKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSobelKernelX() {
		if (jSobelKernelX == null) {
			jSobelKernelX = new JButton();
			jSobelKernelX.setText("Sobel (X)");
			jSobelKernelX.setPreferredSize(new java.awt.Dimension(100,26));
			jSobelKernelX.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1, -1);
					filter.setKernel( 0, -1,  0);
					filter.setKernel(+1, -1,  1);
					
					filter.setKernel(-1,  0, -2);
					filter.setKernel( 0,  0,  0);
					filter.setKernel(+1,  0,  2);
					
					filter.setKernel(-1, +1, -1);
					filter.setKernel( 0, +1,  0);
					filter.setKernel(+1, +1,  1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
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
			jGaussKernel.setText("Gaussian Filter");
			jGaussKernel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						KernelFilter filter = new KernelFilter(getKernelPanel().getKernelDimension());
						double std = Double.parseDouble(jSigmaTextField.getText());
						if (std <= 0) {
							throw new NumberFormatException();
						}
						double sum = 0;
					    for (int i = filter.getKernelMin(); i < filter.getKernelMax(); ++i) {
					    	for (int j = filter.getKernelMin(); j < filter.getKernelMax(); ++j) {
					    		double val = gauss(i,std)*gauss(j,std);
					    		filter.setKernel(i, j, val);
					    		sum += val;
					    	}
					    }
					    // make the sum of kernel identical to 1.0
					    for (int i = filter.getKernelMin(); i < filter.getKernelMax(); ++i) {
					    	for (int j = filter.getKernelMin(); j < filter.getKernelMax(); ++j) {
					    		double val = filter.getKernel(i, j) / sum;
					    		filter.setKernel(i, j, val);
					    	}
					    }
						jKernelPanel.setKernel(filter);
						jGaussErrorInfoLabel.setText("");
						filterPerformer.actionPerformed(null);
					} catch (NumberFormatException err) {
						jGaussErrorInfoLabel.setText("sigma number format error");
						jSigmaTextField.selectAll();
						jSigmaTextField.requestFocus();
					}
				}
				
				private double gauss(double x, double std) {
					return Math.exp(-x*x/(2*std*std)) / (std*Math.sqrt(2*Math.PI));
				}
			});
		}
		return jGaussKernel;
	}

	/**
	 * This method initializes jMedianKernel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJMeanKernel() {
		if (jMeanKernel == null) {
			jMeanKernel = new JButton();
			jMeanKernel.setText("Mean Filter");
			jMeanKernel.setPreferredSize(new java.awt.Dimension(132,26));
			jMeanKernel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int n = getKernelPanel().getKernelDimension();
					KernelFilter filter = new KernelFilter(n);
					double avg = 1.0 / (n*n);
					
				    for (int i = filter.getKernelMin(); i < filter.getKernelMax(); ++i) {
				    	for (int j = filter.getKernelMin(); j < filter.getKernelMax(); ++j) {
				    		filter.setKernel(i, j, avg);
				    	}
				    }

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
		}
		return jMeanKernel;
	}
	
	private JButton getJMedianKernel() {
		if (jMedianKernel == null) {
			jMedianKernel = new JButton();
			jMedianKernel.setText("Median Filter");
			jMedianKernel.setPreferredSize(new java.awt.Dimension(132,26));
			jMedianKernel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int n = getKernelPanel().getKernelDimension();
					MedianFilter filter = new MedianFilter(n, callback.getOriginalImage());

					int type = callback.getOriginalImage().getType();
					if (type == 0) type = BufferedImage.TYPE_INT_RGB;
					callback.setTransformedImage(filter.filter(type));
				}
			});
		}
		return jMedianKernel;
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
		}
		return jRobertKernelPanel;
	}

	/**
	 * This method initializes jSigmaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSigmaPanel() {
		if (jSigmaPanel == null) {
			jSigmaLabel = new JLabel();
			jSigmaLabel.setText("sigma");
			jSigmaLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			jSigmaPanel = new JPanel();
			jSigmaPanel.setLayout(new FlowLayout());
			jSigmaPanel.add(jSigmaLabel, null);
			jSigmaPanel.add(getJSigmaTextField(), null);
		}
		return jSigmaPanel;
	}

	/**
	 * This method initializes jSigmaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJSigmaTextField() {
		if (jSigmaTextField == null) {
			jSigmaTextField = new JTextField();
			jSigmaTextField.setPreferredSize(new java.awt.Dimension(80,20));
			jSigmaTextField.setText("1.0");
			jSigmaTextField.addFocusListener(TinyImageUtils.SEL_ALL_LISTENER);
		}
		return jSigmaTextField;
	}

	/**
	 * This method initializes jRobertKernelY	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRobertKernelY() {
		if (jRobertKernelY == null) {
			jRobertKernelY = new JButton();
			jRobertKernelY.setText("Robert (Y)");
			jRobertKernelY.setPreferredSize(new java.awt.Dimension(100,26));
			jRobertKernelY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1,  0);
					filter.setKernel( 0, -1,  0);
					filter.setKernel(+1, -1,  0);
					
					filter.setKernel(-1,  0,  0);
					filter.setKernel( 0,  0,  0);
					filter.setKernel(+1,  0,  1);
					
					filter.setKernel(-1, +1,  0);
					filter.setKernel( 0, +1, -1);
					filter.setKernel(+1, +1,  0);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
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
			jPrewittKernelY.setText("Prewitt (Y)");
			jPrewittKernelY.setPreferredSize(new java.awt.Dimension(100,26));
			jPrewittKernelY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1, -1);
					filter.setKernel( 0, -1, -1);
					filter.setKernel(+1, -1, -1);
					
					filter.setKernel(-1,  0,  0);
					filter.setKernel( 0,  0,  0);
					filter.setKernel(+1,  0,  0);
					
					filter.setKernel(-1, +1,  1);
					filter.setKernel( 0, +1,  1);
					filter.setKernel(+1, +1,  1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
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
			jSobelKernelY.setText("Sobel (Y)");
			jSobelKernelY.setPreferredSize(new java.awt.Dimension(100,26));
			jSobelKernelY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KernelFilter filter = new KernelFilter(3);
					
					filter.setKernel(-1, -1, -1);
					filter.setKernel( 0, -1, -2);
					filter.setKernel(+1, -1, -1);
					
					filter.setKernel(-1,  0,  0);
					filter.setKernel( 0,  0,  0);
					filter.setKernel(+1,  0,  0);
					
					filter.setKernel(-1, +1,  1);
					filter.setKernel( 0, +1,  2);
					filter.setKernel(+1, +1,  1);

					jKernelPanel.setKernel(filter);
					filterPerformer.actionPerformed(null);
				}
			});
		}
		return jSobelKernelY;
	}
}
