import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StateSpreading
{

	public static void main(String[] args)
	{
		// 数据层
		System.out.println("Spreading...");
		DataSpreader dataMap = new DataSpreader("D:/data.csv", 617, 832);
		try
		{
			dataMap.generateDataFile("D:/data.txt");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Spreading finished");
	}
}
