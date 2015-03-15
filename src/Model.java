import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * This class can load model data from files and manage it.
 */
class Model {

    private int m_numVertices;
    private static int m_numTriangles;
    
    public static int getNumTriangles(){
    	return m_numTriangles;
    }
    
    // the largest absolute coordinate value of the untransformed model data
    private float m_maxSize;

    private Model()
    { }

    /**
     * Creates a {@link Model} instance for the data in the specified file.
     * 
     * @param file
     *            The file to load.
     * @return The {@link Model}, or null if an error occurred.
     */
    public static Model loadModel(final File file)
    {
        final Model model = new Model();

        // read the data from the file
        if (!model.loadModelFromFile(file))
        {
            return null;
        }

        return model;
    }

    /**
     * Reads model data from the specified file.
     * 
     * @param file
     *            The file to load.
     * @return True on success, false otherwise.
     */
    protected boolean loadModelFromFile(final File file)
    {
        m_maxSize = 0.f;

        try (final Scanner scanner = new Scanner(file)) {
            // the first line specifies the vertex count
            m_numVertices = scanner.nextInt();

            // read all vertex coordinates
            float x, y, z;
            // all vertices to be stored in an array
            float[][] vertices = new float[m_numVertices][3];
            for (int i = 0; i < m_numVertices; ++i) {
                // advance the position to the beginning of the next line
                scanner.nextLine();

                // read the vertex coordinates
                x = scanner.nextFloat();
                y = scanner.nextFloat();
                z = scanner.nextFloat();

                // store the vertex data
                vertices[i][0] = x;
                vertices[i][1] = y;
                vertices[i][2] = z;
                
//                System.out.println("X original : " + vertices[i][0]);


                // determine the max value in any dimension in the model file
                m_maxSize = Math.max(m_maxSize, Math.abs(x));
                m_maxSize = Math.max(m_maxSize, Math.abs(y));
                m_maxSize = Math.max(m_maxSize, Math.abs(z));
            }
            
//          System.out.println("X original : " + vertices[0][0]);


            // the next line specifies the number of triangles
            scanner.nextLine();
            m_numTriangles = scanner.nextInt();
            
            System.out.println("hi" + m_numTriangles);

            // read all polygon data (assume triangles); these are indices into
            // the vertex array
            int v1, v2, v3;
            double[] xCoords = new double[3];
            double[] yCoords = new double[3];
            double[] zCoords = new double[3];
            for (int i = 0; i < m_numTriangles; ++i) {
                scanner.nextLine();

                // the model files start with index 1, we start with 0
                v1 = scanner.nextInt() - 1;
                v2 = scanner.nextInt() - 1;
                v3 = scanner.nextInt() - 1;
                
//                System.out.println(" vertices : " + v1 + v2 + v3);

                // TODO store the triangle data in a suitable way
                xCoords[0] = vertices[v1][0];
                xCoords[1] = vertices[v2][0];
                xCoords[2] = vertices[v3][0];
                
//                System.out.println("original: " + xCoords[0]);
                
                yCoords[0] = vertices[v1][1];
                yCoords[1] = vertices[v2][1];
                yCoords[2] = vertices[v3][1];
                
                zCoords[0] = vertices[v1][2];
                zCoords[1] = vertices[v2][2];
                zCoords[2] = vertices[v3][2];
                
                
//        		g.drawLine((int)xCoords[0], (int)yCoords[0], (int)xCoords[1], (int)yCoords[1]);
//        		g.drawLine((int)xCoords[1], (int)yCoords[1], (int)xCoords[2], (int)yCoords[2]);
//        		g.drawLine((int)xCoords[0], (int)yCoords[0], (int)xCoords[2], (int)yCoords[2]);
                
            	Canvas.Polygons[Canvas.numPolygons] = new Polygon();
            	Canvas.Polygons[Canvas.numPolygons].npoints = 3;
            	Canvas.Polygons[Canvas.numPolygons].addPoint((int)xCoords[0], (int)yCoords[0]);
            	Canvas.Polygons[Canvas.numPolygons].addPoint((int)xCoords[1], (int)yCoords[1]);
            	Canvas.Polygons[Canvas.numPolygons].addPoint((int)xCoords[2], (int)yCoords[2]);

            	Canvas.numPolygons++;
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("No such file " + file.toString() + ": "
                    + e.getMessage());
            return false;
        } catch (NoSuchElementException e) {
            System.err.println("Invalid file format: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Something went wrong while reading the"
                    + " model file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        System.out.println("Number of vertices in model: " + m_numVertices);
        System.out.println("Number of triangles in model: " + m_numTriangles);

        return true;
        
    }
    


    /**
     * Returns the largest absolute coordinate value of the original,
     * untransformed model data.
     */
    public float getMaxSize()
    {
        return m_maxSize;
    }
}