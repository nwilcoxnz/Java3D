import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;

/**
 * The draw area.
 */
class Canvas extends JPanel {

	static int numPolygons = 0;
	static Polygon[] Polygons = new Polygon[4300];
    private static final long serialVersionUID = 1L;

    private Model m_model;

    public Canvas()
    {
        setOpaque(true);
    }

    public void setModel(final Model model)
    {
        m_model = model;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (m_model == null) return;

        // TODO render the model
        g.translate(100,100);
        for (int i = 0; i < numPolygons; i++) 
        	g.drawPolygon(Polygons[i]);
        
        System.out.println("drawing");
        System.out.println(numPolygons);

    }
}