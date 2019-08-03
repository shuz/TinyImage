package tinyimage;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenu;

public class TinyImage extends javax.swing.JFrame {
	private static final long serialVersionUID = 2298665270584514327L;
	
	private javax.swing.JPanel ivjJFrameContentPane = null;

	private javax.swing.JToolBar ivjJToolBar = null;

	private javax.swing.JMenuBar ivjJMenuBar = null;

	private javax.swing.JMenu ivjJMenuFile = null;

	private JSplitPane jSplitPane = null;

	private JTabbedPane jLeftPanel = null;

	private JSplitPane jSplitPane1 = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JMenuItem jMenuItemFileOpen = null;

	private JMenuItem jMenuItemFileSaveAs = null;

	private JMenuItem jMenuItemFileClose = null;
	
	private JFileChooser jFileChooser = null;
	
	private BufferedImage originalImage = null;
	
	private BufferedImage transformedImage = null;

	private JImagePanel jOriginalImagePanel = null;

	private JImagePanel jTransformedImagePanel = null;
	
	private LinkedList imageControllers = new LinkedList();

	public TinyImage() {
		super();
		initialize();
	}

	public void addController(ImageController ctrl) {
		imageControllers.addLast(ctrl);
		JPanel panel = ctrl.getPanel();
		getJLeftPanel().addTab(ctrl.getPanelName(), panel);
		ctrl.setAppCallback(callback);
	}
	
	/**
	 * Return the JFrameContentPane property value.
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			ivjJFrameContentPane = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout_3 = new java.awt.BorderLayout();
			ivjJFrameContentPane.setLayout(layBorderLayout_3);
			ivjJFrameContentPane.add(getIvjJToolBar(),
					java.awt.BorderLayout.NORTH);
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {
		this.setContentPane(getJFrameContentPane());
		this.setJMenuBar(getIvjJMenuBar());
		this.setName("TinyImage");
		this.setTitle("TinyImage");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(0, 0, 1024, 768);
	}

	/**
	 * This method initializes ivjJToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private javax.swing.JToolBar getIvjJToolBar() {
		if (ivjJToolBar == null) {
			ivjJToolBar = new javax.swing.JToolBar();
		}
		return ivjJToolBar;
	}

	/**
	 * This method initializes ivjJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getIvjJMenuBar() {
		if (ivjJMenuBar == null) {
			ivjJMenuBar = new javax.swing.JMenuBar();
			ivjJMenuBar.add(getIvjJMenuFile());
			ivjJMenuBar.add(getJImageMenu());
		}
		return ivjJMenuBar;
	}

	/**
	 * This method initializes ivjJMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getIvjJMenuFile() {
		if (ivjJMenuFile == null) {
			ivjJMenuFile = new javax.swing.JMenu();
			ivjJMenuFile.setText("File");
			ivjJMenuFile.add(getJMenuItemFileOpen());
			ivjJMenuFile.add(getJMenuItemFileSaveAs());
			ivjJMenuFile.add(getJMenuItemFileClose());
			ivjJMenuFile.addMenuListener(new javax.swing.event.MenuListener() {
				public void menuSelected(javax.swing.event.MenuEvent e) {
					jMenuItemFileSaveAs.setEnabled(transformedImage != null);
					jMenuItemFileClose.setEnabled(originalImage != null);
				}
				public void menuDeselected(javax.swing.event.MenuEvent e) {}
				public void menuCanceled(javax.swing.event.MenuEvent e) {}
			});
		}
		return ivjJMenuFile;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJLeftPanel());
			jSplitPane.setRightComponent(getJSplitPane1());
		}
		return jSplitPane;
	}

	private JTabbedPane getJLeftPanel() {
		if (jLeftPanel == null) {
			jLeftPanel = new JTabbedPane();
		}
		return jLeftPanel;
	}
	
	/**
	 * This method initializes jSplitPane1	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane1() {
		if (jSplitPane1 == null) {
			jSplitPane1 = new JSplitPane();
			jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPane1.setTopComponent(getJScrollPane());
			jSplitPane1.setBottomComponent(getJScrollPane1());
			jSplitPane1.setDividerLocation(320);
		}
		return jSplitPane1;
	}

	private static class DragMouseListener extends MouseMotionAdapter {
		private JPanel pane;
		
		public DragMouseListener(JPanel pane) {
			this.pane = pane;
			pane.setAutoscrolls(true);
			pane.addMouseMotionListener(this);
		}
		
	    public void mouseDragged(MouseEvent e) {
            Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
            pane.scrollRectToVisible(r);
	    }
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJOriginalImagePanel());
			new DragMouseListener(getJOriginalImagePanel());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTransformedImagePanel());
			new DragMouseListener(getJTransformedImagePanel());
		}
		return jScrollPane1;
	}
	
    /*
     * Get the extension of a file.
     */
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	/**
	 * This method initializes jFileChooser	
	 * 	
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
			jFileChooser.setFileFilter(new FileFilter() {
				
			    //Accept all directories and all gif, jpg, tiff, or png files.
			    public boolean accept(File f) {
			        if (f.isDirectory()) {
			            return true;
			        }

			        String extension = getExtension(f);
			        if (extension != null) {
			            if (extension.equals("bmp") ||
			            	extension.equals("tiff") ||
			                extension.equals("tif")  ||
			                extension.equals("gif")  ||
			                extension.equals("jpeg") ||
			                extension.equals("jpg")  ||
			                extension.equals("png")) {
			                    return true;
			            } else {
			                return false;
			            }
			        }

			        return false;
			    }

			    //The description of this filter
			    public String getDescription() {
			        return "Images";
			    }
			});
		}
		return jFileChooser;
	}
	
	/**
	 * This method initializes jMenuItemFileOpen	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemFileOpen() {
		if (jMenuItemFileOpen == null) {
			jMenuItemFileOpen = new JMenuItem();
			jMenuItemFileOpen.setText("Open...");
			jMenuItemFileOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					closeImage();
					
			        int returnVal = getJFileChooser().showOpenDialog(TinyImage.this);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = getJFileChooser().getSelectedFile();
			            try {
							setOriginalImage(ImageIO.read(file));
							setTransformedImage(ImageIO.read(file));
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Failed to load image.\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
			        }
				}
			});
		}
		return jMenuItemFileOpen;
	}

	/**
	 * This method initializes jMenuItemFileSaveAs	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemFileSaveAs() {
		if (jMenuItemFileSaveAs == null) {
			jMenuItemFileSaveAs = new JMenuItem();
			jMenuItemFileSaveAs.setText("Save As...");
			jMenuItemFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (transformedImage == null) return;
					
			        int returnVal = getJFileChooser().showSaveDialog(TinyImage.this);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = getJFileChooser().getSelectedFile();
			            try {
							boolean succeed = ImageIO.write(transformedImage, getExtension(file), file);
							if (!succeed)
								throw new IOException("No appropriate writer is found.");
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Failed to save image.\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
			        }
				}
			});
		}
		return jMenuItemFileSaveAs;
	}

	private void closeImage() {
		setTransformedImage(null);
		setOriginalImage(null);
	}
	
	/**
	 * This method initializes jMenuItemFileClose	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemFileClose() {
		if (jMenuItemFileClose == null) {
			jMenuItemFileClose = new JMenuItem();
			jMenuItemFileClose.setText("Close");
			jMenuItemFileClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					closeImage();
				}
			});
		}
		return jMenuItemFileClose;
	}

	/**
	 * This method initializes jOriginalImagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JImagePanel getJOriginalImagePanel() {
		if (jOriginalImagePanel == null) {
			jOriginalImagePanel = new JImagePanel();
		}
		return jOriginalImagePanel;
	}
	
	private void setOriginalImage(BufferedImage image) {
		originalImage = image;
		getJOriginalImagePanel().setImage(image);
		Iterator itor = imageControllers.iterator();
		while (itor.hasNext()) {
			ImageController controller = (ImageController)itor.next();
			controller.onOriginalImageChange(image);
		}
		getJOriginalImagePanel().repaint();
	}
	
	private void setTransformedImage(BufferedImage image) {
		transformedImage = image;
		getJTransformedImagePanel().setImage(image);
		getJTransformedImagePanel().repaint();
	}

	/**
	 * This method initializes jTransformedImagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JImagePanel getJTransformedImagePanel() {
		if (jTransformedImagePanel == null) {
			jTransformedImagePanel = new JImagePanel();
		}
		return jTransformedImagePanel;
	}
	
	private TinyImageCallback callback = new TinyImageCallback() {

		public void setOriginalImage(BufferedImage image) {
			TinyImage.this.setOriginalImage(image);
		}
		
		public BufferedImage getOriginalImage() {
			return originalImage;
		}

		public void setTransformedImage(BufferedImage image) {
			TinyImage.this.setTransformedImage(image);
		}
		
		public JPanel getSelectedCard() {
			return (JPanel)getJLeftPanel().getSelectedComponent();
		}

		public BufferedImage getTransformedImage() {
			return transformedImage;
		}

		public JImagePanel getOriginalImagePanel() {
			return getJOriginalImagePanel();
		}

		public JImagePanel getTransformedImagePanel() {
			return getJTransformedImagePanel();
		}
		
	};

	private JMenu jImageMenu = null;

	private JMenuItem jSwapImageMenuItem = null;

	/**
	 * This method initializes jSwapImagesMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJImageMenu() {
		if (jImageMenu == null) {
			jImageMenu = new JMenu();
			jImageMenu.setText("Image");
			jImageMenu.add(getJSwapImageMenuItem());
		}
		return jImageMenu;
	}

	/**
	 * This method initializes jSwapImageMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJSwapImageMenuItem() {
		if (jSwapImageMenuItem == null) {
			jSwapImageMenuItem = new JMenuItem();
			jSwapImageMenuItem.setText("Swap Images");
			jSwapImageMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (transformedImage == null) return;
					BufferedImage image = originalImage;
					originalImage = transformedImage;
					transformedImage = image;
					setOriginalImage(originalImage);
					setTransformedImage(transformedImage);
				}
			});
		}
		return jSwapImageMenuItem;
	}
	
}  //  @jve:decl-index=0:visual-constraint="4,2"
