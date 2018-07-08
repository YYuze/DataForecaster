package demo;

import Layer.ContourRender;
import Layer.Line;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class StateRendering {
	public static void main(String[] args) {
		// 显示层
		Thread t1 = new Thread() {
			public void run() {
				for (int z = 4; z <= 16; z += 3) {
					System.out.println(z + "start");
					String dataFilePath = "D:/Zmesh/" + z + ".msh";
					String contourFilePath = "D:/Zcontour/" + z + ".jpg";
					try {
						System.out.println("Rendering...");
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File(dataFilePath))));
						String txt = "";
						BufferedImage dest = new BufferedImage(617, 832, BufferedImage.TYPE_INT_RGB);
						ContourRender brush = new ContourRender(dest);
						while ((txt = reader.readLine()) != null) {
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
						ImageIO.write(dest, "JPG", new File(contourFilePath));
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(z + "done");
				}
			}
		};

		Thread t2 = new Thread() {
			public void run() {
				for (int z = 19; z <= 31; z += 3) {
					System.out.println(z + "start");
					String dataFilePath = "D:/Zmesh/" + z + ".msh";
					String contourFilePath = "D:/Zcontour/" + z + ".jpg";
					try {
						System.out.println("Rendering...");
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File(dataFilePath))));
						String txt = "";
						BufferedImage dest = new BufferedImage(617, 832, BufferedImage.TYPE_INT_RGB);
						ContourRender brush = new ContourRender(dest);
						while ((txt = reader.readLine()) != null) {
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
						ImageIO.write(dest, "JPG", new File(contourFilePath));
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(z + "done");
				}
			}
		};
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
