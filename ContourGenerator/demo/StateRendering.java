import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import javax.imageio.ImageIO;

public class StateRendering
{

	public static void main(String[] args)
	{
		// 显示层
		System.out.println("counting...");
		try
		{
			LineNumberReader counter = new LineNumberReader(new FileReader(new File("D:/mesh.txt")));
			counter.skip(Long.MAX_VALUE);
			int lineCount = counter.getLineNumber();
			counter.close();
			System.out.println("counting finished");

			System.out.println("Rendering...");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("D:/mesh.txt"))));
			String txt = "";
			BufferedImage dest = new BufferedImage(617, 832, BufferedImage.TYPE_INT_RGB);
			ContourRender brush = new ContourRender(dest);
			int index = 0;
			while ((txt = reader.readLine()) != null)
			{
				System.out.println(index+"/"+lineCount);
				index++;
				String[] arg = txt.split(",");
				double x1 = Double.valueOf(arg[0]);
				double y1 = Double.valueOf(arg[1]);
				double x2 = Double.valueOf(arg[2]);
				double y2 = Double.valueOf(arg[3]);
				int rgb = Integer.valueOf(arg[4]);
				Line line = new Line(x1, y1, x2, y2, rgb);
				brush.drawLine(line);
			}
			System.out.println("Rendering finished");
			reader.close();
			ImageIO.write(dest, "JPG", new File("D:/123.jpg"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
