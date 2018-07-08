package demo;

import part.ConrecMesher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StateMeshing {
	public static void main(String[] args) {
		// 处理层
		Thread t1 = new Thread() {
			public void run() {
				for (int ii = 4; ii <= 16; ii += 3) {
					double[][] data = new double[617][832];
					String dataFilePath = "D:/Zspread/" + ii + ".sprd";
					String destFilePath = "D:/Zmesh/" + ii + ".msh";
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File(dataFilePath))));
						for (int i = 0; i < data.length; i++) {
							for (int u = 0; u < data[i].length; u++) {
								data[i][u] = Double.valueOf(reader.readLine());
							}
						}
						reader.close();
						System.out.println("Meshing " + ii + " start");
						ConrecMesher processor = new ConrecMesher(data);
						processor.generateMeshingFile(destFilePath);
						System.out.println("Meshing " + ii + " finished");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				for (int ii = 19; ii <= 31; ii += 3) {
					double[][] data = new double[617][832];
					String dataFilePath = "D:/Zspread/" + ii + ".sprd";
					String destFilePath = "D:/Zmesh/" + ii + ".msh";
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(new File(dataFilePath))));
						for (int i = 0; i < data.length; i++) {
							for (int u = 0; u < data[i].length; u++) {
								data[i][u] = Double.valueOf(reader.readLine());
							}
						}
						reader.close();
						System.out.println("Meshing " + ii + " start");
						ConrecMesher processor = new ConrecMesher(data);
						processor.generateMeshingFile(destFilePath);
						System.out.println("Meshing " + ii + " finished");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
