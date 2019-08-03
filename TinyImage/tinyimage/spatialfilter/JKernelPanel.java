package tinyimage.spatialfilter;

import java.awt.GridLayout;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import tinyimage.TinyImageUtils;

public class JKernelPanel extends JPanel {

	private static final long serialVersionUID = -5611408371678200967L;
	private static final int INIT_KERNEL_SIZE = 3;
	
	private JPanel jKernelSizePanel = null;
	private JLabel jKernelSizeLabel = null;
	private JComboBox jKernelSizeComboBox = null;
	private JPanel jKernelPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jAcceptButton = null;
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
		this.setSize(300, 250);
		this.add(getJKernelSizePanel(), java.awt.BorderLayout.NORTH);
		this.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
		this.setKernelDimension(INIT_KERNEL_SIZE);
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
			jKernelSizeLabel.setText("Kernel Size");
			jKernelSizePanel = new JPanel();
			jKernelSizePanel.add(jKernelSizeLabel, null);
			jKernelSizePanel.add(getJKernelSizeComboBox(), null);
		}
		return jKernelSizePanel;
	}

	/**
	 * This method initializes jKernelSizeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJKernelSizeComboBox() {
		if (jKernelSizeComboBox == null) {
			jKernelSizeComboBox = new JComboBox();
			jKernelSizeComboBox.setPreferredSize(new java.awt.Dimension(100,25));
			for (int i = 1; i < 10; i+=2) {
				jKernelSizeComboBox.addItem(new Integer(i));
			}
			jKernelSizeComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					Integer size = (Integer)jKernelSizeComboBox.getSelectedItem();
					if (size != null) {
						updateKernelPanel(size.intValue());
					}
				}
			});
			
		}
		return jKernelSizeComboBox;
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
			jErrorInfoLabel = new JLabel();
			jErrorInfoLabel.setForeground(java.awt.Color.red);
			jButtonPanel = new JPanel();
			jButtonPanel.setLayout(new BorderLayout());
			jButtonPanel.add(getJAcceptButton(), java.awt.BorderLayout.EAST);
			jButtonPanel.add(jErrorInfoLabel, java.awt.BorderLayout.CENTER);
		}
		return jButtonPanel;
	}

	/**
	 * This method initializes jAcceptButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAcceptButton() {
		if (jAcceptButton == null) {
			jAcceptButton = new JButton();
			jAcceptButton.setText("Filter");
			jAcceptButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		}
		return jAcceptButton;
	}

	public void setKernelDimension(int n) {
		getJKernelSizeComboBox().setSelectedIndex(n/2);
		
		updateKernelPanel(n);
		jKernelElements[n*n/2].setText("1");
	}
	
	private void updateKernelPanel(int n) {
		kernelDimension = n;
		
		getJKernelPanel().removeAll();
		GridLayout gridLayout = new GridLayout(n, n);
		jKernelPanel.setLayout(gridLayout);
		
		jKernelElements = new JTextField[n*n];
		for (int i = 0; i < n*n; ++i) {
			jKernelElements[i] = new JTextField();
			jKernelElements[i].setText("0");
			jKernelElements[i].addFocusListener(TinyImageUtils.SEL_ALL_LISTENER);
			jKernelPanel.add(jKernelElements[i]);
		}
		this.updateUI();
	}
	
	public KernelFilter getFilter() {
		KernelFilter filter = new KernelFilter(kernelDimension);
		int k = 0;
		try {
			for (int i = filter.getKernelMin(); i < filter.getKernelMax(); ++i) {
				for (int j = filter.getKernelMin(); j < filter.getKernelMax(); ++j, ++k) {
					double value = Double.parseDouble(jKernelElements[k].getText());
					filter.setKernel(i, j, value);
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
	
	public void addActionListener(ActionListener action) {
		this.jAcceptButton.addActionListener(action);
	}
	
	public int getKernelDimension() {
		return kernelDimension;
	}
	
	public void setKernel(KernelFilter kernel) {
		setKernelDimension(kernel.getKernelDimension());
		
		DecimalFormat nf = new DecimalFormat("#0.######");
		int k = 0;
		for (int i = kernel.getKernelMin(); i < kernel.getKernelMax(); ++i) {
			for (int j = kernel.getKernelMin(); j < kernel.getKernelMax(); ++j, ++k) {
				double value = kernel.getKernel(i, j);
				
				jKernelElements[k].setText(nf.format(value));
				jKernelElements[k].setCaretPosition(0);
			}
		}
		jErrorInfoLabel.setText("");
	}
	
	private int kernelDimension = INIT_KERNEL_SIZE;
}
