package stock;

/* Author: Zule Li
 * Email:zule.li@hotmail.com
 * Last Modified Date:Mar.7,2015
 * */
import javax.swing.*;
import java.awt.*;

public class Canvas extends JLabel {
	public double[] data0 = null;

	public Canvas() {
	}

	public void paint(Graphics g) {
		draw1((Graphics2D) g);
	}

	private void draw1(Graphics2D g) {
		if (data0 == null)
			return;
		double high = 0;
		int h = 200;
		int w = 600;
		int righte = 1;
		float upe = 0.85f;
		for (int i = 0; i < data0.length; i++) {
			double high0 = data0[i];
			if (high0 > high)
				high = high0;
		}
		double hr = h / high;
		int wr = (int) w / data0.length;
		for (int i = 0; i < data0.length; i++) {
			g.drawLine((righte + i) * wr, h, (righte + i) * wr, (int) (h - upe
					* hr * (data0[i])));
		}

		g.drawLine(righte * wr, 0, righte * wr, h);
		g.drawLine(righte * wr, h, data0.length * wr, h);
	}
}