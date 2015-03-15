//package nz.ac.massey.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ModelViewer {

    private static final int INIT_WIDTH = 1024;
    private static final int INIT_HEIGHT = 768;

    private JFrame m_frame;

    private Canvas m_canvas;

    private JSlider m_sliderRotateX;
    private JSlider m_sliderRotateY;
    private JSlider m_sliderRotateZ;

    private JButton m_btnScaleUp;
    private JButton m_btnScaleDown;
    private JButton m_btnIncrX;
    private JButton m_btnDecrX;
    private JButton m_btnIncrY;
    private JButton m_btnDecrY;
    private JButton m_btnIncrZ;
    private JButton m_btnDecrZ;

    private JCheckBox m_chkRenderWireframe;
    private JCheckBox m_chkRenderSolid;
    private JCheckBox m_chkCullBackFaces;

    private JMenuItem m_menuOpenModelFile;

    private Model m_currentModel;

    // ////////////////////////////////////////////////////////////////////
    private ChangeListener m_sliderChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e)
        {
            final JSlider source = (JSlider) e.getSource();
            if (m_currentModel != null) {
                if (source == m_sliderRotateX) {
                    // TODO
                } else if (source == m_sliderRotateY) {
                    // TODO
                } else if (source == m_sliderRotateZ) {
                    // TODO
                }
            }
        }
    };

    private ActionListener m_btnActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            final Object source = e.getSource();
            if (m_currentModel != null) {
                // scale changes
                if (source == m_btnScaleUp) {
                    // TODO
                } else if (source == m_btnScaleDown) {
                    // TODO
                }
                // translation changes
                else if (source == m_btnIncrX) {
                    // TODO
                } else if (source == m_btnDecrX) {
                    // TODO
                } else if (source == m_btnIncrY) {
                    // TODO
                } else if (source == m_btnDecrY) {
                    // TODO
                } else if (source == m_btnIncrZ) {
                    // TODO
                } else if (source == m_btnDecrZ) {
                    // TODO
                }
            }
        }
    };

    private ActionListener m_chkActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            final Object source = e.getSource();
            if (source == m_chkRenderWireframe) {
                // TODO
            } else if (source == m_chkRenderSolid) {
                // TODO
            } else if (source == m_chkCullBackFaces) {
                // TODO
            }
        }
    };

    private ActionListener m_menuActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == m_menuOpenModelFile) {
                Model model = loadModelFile();
                if (model != null) m_currentModel = model;
            }
        }
    };

    // ////////////////////////////////////////////////////////////////////
    private ModelViewer()
    {}

    /**
     * Factory method for {@link ModelViewer}.
     */
    public static ModelViewer create()
    {
        final ModelViewer viewer = new ModelViewer();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run()
            {
                viewer.createGui();
            }
        });

        return viewer;
    }

    /**
     * Creates the GUI. Must be called from the EDT.
     */
    private void createGui()
    {
        m_frame = new JFrame("Model Viewer");
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setup the content pane
        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        m_frame.setContentPane(contentPane);

        final Border border =
                BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        // setup the canvas and control panel
        m_canvas = new Canvas();
        m_canvas.setBorder(border);
        contentPane.add(m_canvas, BorderLayout.CENTER);
        final JComponent controlPanel = createControlPanel();
        controlPanel.setBorder(border);
        contentPane.add(controlPanel, BorderLayout.LINE_START);

        // add the menu
        final JMenuBar menuBar = new JMenuBar();
        m_frame.setJMenuBar(menuBar);
        final JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        m_menuOpenModelFile = new JMenuItem("Open");
        m_menuOpenModelFile.addActionListener(m_menuActionListener);
        fileMenu.add(m_menuOpenModelFile);

        // register a key event dispatcher to get a turn in handling all
        // key events, independent of which component currently has the focus
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e)
                    {
                        switch (e.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            System.exit(0);
                            return true; // consume the event
                        default:
                            return false; // do not consume the event
                        }
                    }
                });

        m_frame.setSize(new Dimension(INIT_WIDTH, INIT_HEIGHT));
        m_frame.setVisible(true);
    }

    /**
     * Creates and returns the control panel. Must be called from the EDT.
     */
    private JComponent createControlPanel()
    {
        final JPanel toolbar = new JPanel(new GridBagLayout());

        final GridBagConstraints gbcDefault = new GridBagConstraints();
        gbcDefault.gridx = 0;
        gbcDefault.gridy = GridBagConstraints.RELATIVE;
        gbcDefault.gridwidth = 2;
        gbcDefault.fill = GridBagConstraints.HORIZONTAL;
        gbcDefault.insets = new Insets(5, 10, 5, 10);
        gbcDefault.anchor = GridBagConstraints.FIRST_LINE_START;
        gbcDefault.weightx = 0.5;

        final GridBagConstraints gbcLabels =
                (GridBagConstraints) gbcDefault.clone();
        gbcLabels.insets = new Insets(5, 10, 0, 10);

        final GridBagConstraints gbcTwoCol =
                (GridBagConstraints) gbcDefault.clone();
        gbcTwoCol.gridwidth = 1;
        gbcTwoCol.gridx = 0;
        gbcTwoCol.insets.right = 5;

        GridBagConstraints gbc = null;

        // setup the rotation sliders
        m_sliderRotateX = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        m_sliderRotateY = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        m_sliderRotateZ = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        m_sliderRotateX.setPaintLabels(true);
        m_sliderRotateY.setPaintLabels(true);
        m_sliderRotateZ.setPaintLabels(true);
        m_sliderRotateX.setPaintTicks(true);
        m_sliderRotateY.setPaintTicks(true);
        m_sliderRotateZ.setPaintTicks(true);
        m_sliderRotateX.setMajorTickSpacing(90);
        m_sliderRotateY.setMajorTickSpacing(90);
        m_sliderRotateZ.setMajorTickSpacing(90);
        m_sliderRotateX.addChangeListener(m_sliderChangeListener);
        m_sliderRotateY.addChangeListener(m_sliderChangeListener);
        m_sliderRotateZ.addChangeListener(m_sliderChangeListener);
        gbc = gbcDefault;
        toolbar.add(new JLabel("Rotation X:"), gbcLabels);
        toolbar.add(m_sliderRotateX, gbc);
        toolbar.add(new JLabel("Rotation Y:"), gbcLabels);
        toolbar.add(m_sliderRotateY, gbc);
        toolbar.add(new JLabel("Rotation Z:"), gbcLabels);
        toolbar.add(m_sliderRotateZ, gbc);

        m_btnScaleDown = new JButton("- size");
        m_btnScaleUp = new JButton("+ size");
        m_btnScaleDown.addActionListener(m_btnActionListener);
        m_btnScaleUp.addActionListener(m_btnActionListener);
        gbc = (GridBagConstraints) gbcTwoCol.clone();
        toolbar.add(m_btnScaleDown, gbc);
        gbc.gridx = 1;
        gbc.insets.left = gbc.insets.right;
        gbc.insets.right = gbcDefault.insets.right;
        toolbar.add(m_btnScaleUp, gbc);

        m_btnIncrX = new JButton("+ x");
        m_btnDecrX = new JButton("- x");
        m_btnIncrY = new JButton("+ y");
        m_btnDecrY = new JButton("- y");
        m_btnIncrZ = new JButton("+ z");
        m_btnDecrZ = new JButton("- z");
        m_btnIncrX.addActionListener(m_btnActionListener);
        m_btnDecrX.addActionListener(m_btnActionListener);
        m_btnIncrY.addActionListener(m_btnActionListener);
        m_btnDecrY.addActionListener(m_btnActionListener);
        m_btnIncrZ.addActionListener(m_btnActionListener);
        m_btnDecrZ.addActionListener(m_btnActionListener);

        gbc = (GridBagConstraints) gbcTwoCol.clone();
        toolbar.add(m_btnDecrX, gbc);
        gbc.gridx = 1;
        gbc.insets.left = gbc.insets.right;
        gbc.insets.right = gbcDefault.insets.right;
        toolbar.add(m_btnIncrX, gbc);

        gbc = (GridBagConstraints) gbcTwoCol.clone();
        toolbar.add(m_btnDecrY, gbc);
        gbc.gridx = 1;
        gbc.insets.left = gbc.insets.right;
        gbc.insets.right = gbcDefault.insets.right;
        toolbar.add(m_btnIncrY, gbc);

        gbc = (GridBagConstraints) gbcTwoCol.clone();
        toolbar.add(m_btnDecrZ, gbc);
        gbc.gridx = 1;
        gbc.insets.left = gbc.insets.right;
        gbc.insets.right = gbcDefault.insets.right;
        toolbar.add(m_btnIncrZ, gbc);

        // add check boxes
        gbc = gbcDefault;

        m_chkRenderWireframe = new JCheckBox("Render Wireframe");
        m_chkRenderWireframe.setSelected(true);
        m_chkRenderWireframe.addActionListener(m_chkActionListener);
        toolbar.add(m_chkRenderWireframe, gbc);

        m_chkRenderSolid = new JCheckBox("Render Solid");
        m_chkRenderSolid.setSelected(true);
        m_chkRenderSolid.addActionListener(m_chkActionListener);
        toolbar.add(m_chkRenderSolid, gbc);

        m_chkCullBackFaces = new JCheckBox("Cull Back Faces");
        m_chkCullBackFaces.setSelected(true);
        m_chkCullBackFaces.addActionListener(m_chkActionListener);
        gbc = (GridBagConstraints) gbcDefault.clone();
        gbc.weighty = 1.;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        toolbar.add(m_chkCullBackFaces, gbc);

        return toolbar;
    }

    /**
     * Displays a chooser dialog and loads the selected model.
     * 
     * @return The model, or null if the user cancels the action or something
     *         goes wrong.
     */
    private Model loadModelFile()
    {
        // show a file chooser for model files
        JFileChooser chooser = new JFileChooser("./");
        chooser.setFileFilter(new FileNameExtensionFilter(
                ".dat model files", "dat"));
        int retVal = chooser.showOpenDialog(m_frame);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            // try to load the model from the selected file
            Model model = Model.loadModel(file);

            if (model != null) {
                // initialise the scale value so that the model fits into the
                // render window
                // TODO

                // update the canvas on success
                m_canvas.setModel(model);
            }

            return model;
        }

        return null;
    }

    public static void main(String[] args)
    {
        ModelViewer.create();
    }
}
