import java.awt.Graphics;

public class Triangle {
	double[] x = new double[3];
	double[] y = new double[3];
	
	Triangle(double[] x, double[] y, double[] z){
		this.x = x;
		this.y = y;
		Canvas.numPolygons++;
	}

	public void draw(Graphics g){
		for(int i =0; i<2;i++){
			System.out.println(x[i]);
		}
		System.out.println(y[0]);
		g.drawLine(-2000*(int)x[0], -2000*(int)y[0], -2000*(int)x[1], -2000*(int)y[1]);
		g.drawLine(-2000*(int)x[1], -2000*(int)y[1], -2000*(int)x[2], -2000*(int)y[2]);
		g.drawLine(-2000*(int)x[0], -2000*(int)y[0], -2000*(int)x[2], -2000*(int)y[2]);
	}

}
