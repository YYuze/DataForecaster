import java.awt.Point;
import java.awt.geom.Line2D;

/*
 * line in grid initiate with start point\end point\value
 */
public class Line
{
	private Line2D.Double line;
	private int rgb;

	public Line(double x1,double y1,double x2,double y2, double value)
	{
		line = new Line2D.Double(x1,y1,x2,y2);
		this.rgb = this.convertValueToRGB(value);
	}
	
	public Line(double x1,double y1,double x2,double y2,int rgb)
	{
		line = new Line2D.Double(x1,y1,x2,y2);
		this.rgb = rgb;
	}

	private int convertValueToRGB(double value)
	{
		double h = value / 100 * 360;
		double s = 1;
		double l = 0.5;
		int rgb = this.convertHSLToRGB(h, s, l);
		return rgb;
	}

	private int convertHSLToRGB(double h, double s, double l)
	{
		double q = 0.0;
		if (l < 0.5)
		{
			q = l * (1 + s);
		} else
		{
			q = l + s - l * s;
		}
		double p = 2 * l - q;
		double hk = h / 360.0;
		double[] t = { hk + 1.0 / 3.0, hk, hk - 1.0 / 3.0 };
		for (int i = 0; i < t.length; i++)
		{
			if (t[i] > 1)
			{
				t[i] -= 1;
			}
			if (t[i] < 0)
			{
				t[i] += 1;
			}
			if (t[i] < 1.0 / 6.0)
			{
				t[i] = p + (q - p) * 6 * t[i];
			} else if (t[i] < 0.5)
			{
				t[i] = q;
			} else if (t[i] < 2.0 / 3.0)
			{
				t[i] = p + (q - p) * 6 * (2.0 / 3.0 - t[i]);
			} else
			{
				t[i] = p;
			}
			t[i] *= 255;
		}
		int r = (int) t[0];
		int g = (int) t[1];
		int b = (int) t[2];
		int rgb = (r << 16) | (g << 8) | (b << 0);
		return rgb;
	}

	public Line2D.Double getLine()
	{
		return this.line;
	}

	public int getLineRGB()
	{
		return this.rgb;
	}
}
