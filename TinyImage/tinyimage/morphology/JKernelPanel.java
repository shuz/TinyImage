package tinyimage.morphology;

import java.awt.GridLayout;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import tinyimage.TinyImageUtils;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class JKernelPanel extends JPanel {

	private static final long serialVersionUID = -5098501127982743913L;

	private static final int INIT_KERNEL_SIZE = 3;
	private static final int MAX_KERNEL_SIZE = 40;
	
	private JPanel jKernelSizePanel = null;
	private JLabel jKernelSizeLabel = null;
	private JComboBox jKernelSizeComboBoxX = null;
	private JPanel jKernelPanel = null;
	private JPanel jButtonPanel = null;
	private JLabel jErrorInfoLabel = null;
	private JTextField [] jKernelElements = null;
	
	/**
	 * This is the default constructor
	 */
	public JKernelPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(290, 260);
		this.add(getJKernelPropertyPanel(), java.awt.BorderLayout.NORTH);
		this.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
		this.setKernelDimension(INIT_KERNEL_SIZE, INIT_KERNEL_SIZE);
		this.add(getJKernelPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes jKernelSizePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJKernelSizePanel() {
		if (jKernelSizePanel == null) {
			jKernelSizeLabel = new JLabel();
			jKernelSizeLabel.setText("Size");
			jKernelSizePanel = new JPanel();
			jKernelSizePanel.add(jKernelSizeLabel, null);
			jKernelSizePanel.add(getJKernelSizeComboBoxX(), null);
			jKernelSizePanel.add(getJKernelSizeComboBoxY(), null);
		}
		return jKernelSizePanel;
	}

	/**
	 * This method initializes jKernelSizeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJKernelSizeComboBoxX() {
		if (jKernelSizeComboBoxX == null) {
			initJKernelSizeComboBox();
		}
		return jKernelSizeComboBoxX;
	}

	/**
	 * This method initializes jKernelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJKernelPanel() {
		if (jKernelPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jKernelPanel = new JPanel();
			jKernelPanel.setLayout(gridLayout);
		}
		return jKernelPanel;
	}

	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.setLayout(new BoxLayout(getJButtonPanel(), BoxLayout.Y_AXIS));
			jButtonPanel.setPreferredSize(new java.awt.Dimension(222,80));
			jButtonPanel.add(getJShapePanel(), null);
			jButtonPanel.add(getJInfoPanel(), null);
		}
		return jButtonPanel;
	}

	public void setKernelDimension(int x, int y) {
		getJKernelSizeComboBoxX().setSelectedIndex(x-1);
		getJKernelSizeComboBoxY().setSelectedIndex(y-1);
		
		updateKernelPanel(x, y);
	}
	
	private void updateKernelPanel(int x, int y) {
		xdim = x;
		ydim = y;
		xorg = x/2;
		yorg = y/2;
		
		getJKernelPanel().removeAll();
		GridLayout gridLayout = new GridLayout(y, x);
		jKernelPanel.setLayout(gridLayout);
		
		jKernelElements = new JTextField[x*y];
		for (int i = 0; i < x*y; ++i) {
			jKernelElements[i] = new JTextField();
			jKernelElements[i].setText("0");
			jKernelElements[i].addFocusListener(TinyImageUtils.SEL_ALL_LISTENER);
			jKernelPanel.add(jKernelElements[i]);
		}
		jKernelOrgComboBoxX.removeAllItems();
		jKernelOrgComboBoxY.removeAllItems();
		for (int i = 0; i < xdim; ++i) {
			jKernelOrgComboBoxX.addItem(new Integer(i));
		}
		for (int j = 0; j < ydim; ++j) {
			jKernelOrgComboBoxY.addItem(new Integer(j));
		}
		jKernelOrgComboBoxX.setSelectedIndex(xorg);
		jKernelOrgComboBoxY.setSelectedIndex(yorg);
		
		this.updateUI();
	}

	public KernelFilter getFilter() {
		KernelFilter filter = new KernelFilter(xdim, ydim);
		filter.setKernelXOrg(xorg);
		filter.setKernelYOrg(yorg);
		int k = 0;
		try {
			for (int i = filter.getKernelXMin(); i < filter.getKernelXMax(); ++i) {
				for (int j = filter.getKernelYMin(); j < filter.getKernelYMax(); ++j, ++k) {
					String text = jKernelElements[k].getText(); 
					if (text.equals("") || text.equals("*") || text.equals(" ")) {
						filter.setKernelMask(i, j, false);
					} else {
						int value = Integer.parseInt(text);
						filter.setKernel(i, j, value);
					}
				}
			}
			jErrorInfoLabel.setText("");
		} catch (NumberFormatException err) {
			jErrorInfoLabel.setText("Number format error.");
			jKernelElements[k].selectAll();
			jKernelElements[k].requestFocus();
			return null;
		}
		
		return filter;
	}
	
	public int getKernelXDimension() {
		return xdim;
	}

	public int getKernelYDimension() {
		return ydim;
	}
	
	public void setKernel(KernelFilter kernel) {
		setKernelDimension(kernel.getKernelXDimension(), kernel.getKernelYDimension());
		
		DecimalFormat nf = new DecimalFormat("#0");
		int k = 0;
		for (int i = kernel.getKernelXMin(); i < kernel.getKernelYMax(); ++i) {
			for (int j = kernel.getKernelYMin(); j < kernel.getKernelYMax(); ++j, ++k) {
				double value = kernel.getKernel(i, j);
				
				jKernelElements[k].setText(nf.format(value));
				jKernelElements[k].setCaretPosition(0);
			}
		}
		jErrorInfoLabel.setText("");
		xorg = -kernel.getKernelXMin();
		yorg = -kernel.getKernelYMin();
	}
	
	private int xdim = INIT_KERNEL_SIZE;
	private int ydim = INIT_KERNEL_SIZE;
	private int xorg, yorg;

	private JComboBox jKernelSizeComboBoxY = null;

	private JPanel jKernelPropertyPanel = null;

	private JPanel jKernelOrgPanel = null;

	private JLabel jLabel = null;

	private JComboBox jKernelOrgComboBoxX = null;

	private JComboBox jKernelOrgComboBoxY = null;

	private JPanel jShapePanel = null;

	private JButton jSquareButton = null;

	private JButton jCrossButton = null;

	private JButton jDiscButton = null;

	private JPanel jInfoPanel = null;


	private void initJKernelOrgComboBox() {
		jKernelOrgComboBoxX = new JComboBox();
		jKernelOrgComboBoxY = new JComboBox();
		jKernelOrgComboBoxX.setPreferredSize(new java.awt.Dimension(100,25));
		jKernelOrgComboBoxY.setPreferredSize(new java.awt.Dimension(100,25));
		
		ItemListener listener = new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				Integer orgX = (Integer)jKernelOrgComboBoxX.getSelectedItem();
				Integer orgY = (Integer)jKernelOrgComboBoxY.getSelectedItem();
				if (orgX != null && orgY != null) {
					xorg = orgX.intValue();
					yorg = orgY.intValue();
				}
			}
		};
		jKernelSizeComboBoxX.addItemListener(listener);
		jKernelSizeComboBoxY.addItemListener(listener);
	}
	
	private void initJKernelSizeComboBox() {
		jKernelSizeComboBoxX = new JComboBox();
		jKernelSizeComboBoxY = new JComboBox();
		jKernelSizeComboBoxX.setPreferredSize(new java.awt.Dimension(100,25));
		jKernelSizeComboBoxY.setPreferredSize(new java.awt.Dimension(100,25));
		for (int i = 1; i < MAX_KERNEL_SIZE; i++) {
			jKernelSizeComboBoxX.addItem(new Integer(i));
			jKernelSizeComboBoxY.addItem(new Integer(i));
		}
		ItemListener listener = new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				Integer sizeX = (Integer)jKernelSizeComboBoxX.getSelectedItem();
				Integer sizeY = (Integer)jKernelSizeComboBoxY.getSelectedItem();
				if (sizeX != null && sizeY != null) {
					updateKernelPanel(sizeX.intValue(), sizeY.intValue());
				}
			}
		};
		jKernelSizeComboBoxX.addItemListener(listener);
		jKernelSizeComboBoxY.addItemListener(listener);
	}
	
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJKernelSizeComboBoxY() {
		if (jKernelSizeComboBoxY == null) {
			initJKernelSizeComboBox();
		}
		return jKernelSizeComboBoxY;
	}

	/**
	 * This method initializes jKernelPropertyPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJKernelPropertyPanel() {
		if (jKernelPropertyPanel == null) {
			jKernelPropertyPanel = new JPanel();
			jKernelPropertyPanel.setLayout(new BoxLayout(getJKernelPropertyPanel(), BoxLayout.Y_AXIS));
			jKernelPropertyPanel.add(getJKernelSizePanel(), null);
			jKernelPropertyPanel.add(getJKernelOrgPanel(), null);
		}
		return jKernelPropertyPanel;
	}

	/**
	 * This method initializes jKernelOrgPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJKernelOrgPanel() {
		if (jKernelOrgPanel == null) {
			jLabel = new JLabel();
			jLabel.setText("Origin");
			jKernelOrgPanel = new JPanel();
			jKernelOrgPanel.add(jLabel, null);
			jKernelOrgPanel.add(getJKernelOrgComboBoxX(), null);
			jKernelOrgPanel.add(getJKernelOrgComboBoxY(), null);
		}
		return jKernelOrgPanel;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJKernelOrgComboBoxX() {
		if (jKernelOrgComboBoxX == null) {
			initJKernelOrgComboBox();
		}
		return jKernelOrgComboBoxX;
	}

	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJKernelOrgComboBoxY() {
		if (jKernelOrgComboBoxY == null) {
			initJKernelOrgComboBox();
		}
		return jKernelOrgComboBoxY;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJShapePanel() {
		if (jShapePanel == null) {
			jShapePanel = new JPanel();
			jShapePanel.add(getJSquareButton(), null);
			jShapePanel.add(getJCrossButton(), null);
			jShapePanel.add(getJDiscButton(), null);
		}
		return jShapePanel;
	}

	public void setKernelToSquare() {
		squarePerformer.actionPerformed(null);
	}
	
	public void setKernelToCross() {
		crossPerformer.actionPerformed(null);
	}
	
	public void setKernelToDisc() {
		discPerformer.actionPerformed(null);
	}
	
	private ActionListener squarePerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (int k = 0; k < xdim*ydim; ++k) {
				jKernelElements[k].setText("0");
			}
		}
	};

	private ActionListener crossPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (int k = 0; k < xdim*ydim; ++k) {
				jKernelElements[k].setText("");
			}
			for (int i = 0; i < xdim; ++i) {
				jKernelElements[yorg*xdim + i].setText("0");
			}
			for (int i = 0; i < ydim; ++i) {
				jKernelElements[xorg + i*xdim].setText("0");
			}
		}
	};

	private ActionListener discPerformer = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			double radius = Math.max(xdim, ydim) / 2.0;
			for (int i = 0; i < xdim; ++i) {
				for (int j = 0; j < ydim; ++j) {
					boolean inDisc = (i-xorg) * (i-xorg) + (j-yorg) * (j-yorg) <= radius * radius;
					jKernelElements[j*xdim + i].setText(inDisc ? "0" : "");
				}
			}
		}
	};

	private JButton jResetButton = null;
	
	/**
	 * This method initializes jSquareButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSquareButton() {
		if (jSquareButton == null) {
			jSquareButton = new JButton();
			jSquareButton.setText("Square");
			jSquareButton.addActionListener(squarePerformer);
		}
		return jSquareButton;
	}

	/**
	 * This method initializes jCrossButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCrossButton() {
		if (jCrossButton == null) {
			jCrossButton = new JButton();
			jCrossButton.setText("Cross");
			jCrossButton.addActionListener(crossPerformer);
		}
		return jCrossButton;
	}

	/**
	 * This method initializes jDiscButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJDiscButton() {
		if (jDiscButton == null) {
			jDiscButton = new JButton();
			jDiscButton.setText("Disc");
			jDiscButton.addActionListener(discPerformer);
		}
		return jDiscButton;
	}

	/**
	 * This method initializes jInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJInfoPanel() {
		if (jInfoPanel == null) {
			jErrorInfoLabel = new JLabel();
			jErrorInfoLabel.setForeground(java.awt.Color.red);
			jErrorInfoLabel.setPreferredSize(new java.awt.Dimension(100,30));
			jInfoPanel = new JPanel();
			jInfoPanel.setPreferredSize(new java.awt.Dimension(10,30));
			jInfoPanel.add(jErrorInfoLabel, null);
			jInfoPanel.add(getJResetButton(), null);
		}
		return jInfoPanel;
	}

	/**
	 * This method initializes jResetButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJResetButton() {
		if (jResetButton == null) {
			jResetButton = new JButton();
			jResetButton.setText("Reset");
			jResetButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setKernelDimension(INIT_KERNEL_SIZE, INIT_KERNEL_SIZE);
				}
			});
		}
		return jResetButton;
	}
}
