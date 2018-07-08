package demo;
import java.io.IOException;
import Layer.DataSpreader;
/*
 * 由于数据计算密度较高，采用多线程并发计算对cpu要求很高
 * cpu计算速度不够时就会出现GC线程锁死的情况
 */
public class StateSpreading {
	public static void main(String[] args) {
		// 数据层
		System.out.println("Spreading...");
		Thread t1 = new Thread() {
			public void run() {
				for (int i = 4; i <= 31; i += 3) {
					System.out.println(i + ".csv start");
					String targetFilePath = "D:/Zdata/" + i + ".csv";
					String destFilePath = "D:/Zspread/" + i + ".sprd";
					DataSpreader dataMap = new DataSpreader(targetFilePath, 617, 832);
					try {
						dataMap.generateDataFile(destFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(i + ".csv finished");
				}
			}
		};
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Spreading finished");
	}
}
