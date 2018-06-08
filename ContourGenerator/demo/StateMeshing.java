import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StateMeshing
{

	public static void main(String[] args)
	{
		// 处理层
		double[][] data = new double[617][832];
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data.txt"))));
			for (int i = 0; i < data.length; i++)
			{
				for (int u = 0; u < data[i].length; u++)
				{
					data[i][u] = Double.valueOf(reader.readLine());
				}
			}
			reader.close();
			System.out.println("Meshing...");
			ConrecMesher processor = new ConrecMesher(data);
			processor.generateMeshingFile("D:/mesh.txt");
			System.out.println("Meshing finished");
		}catch (NumberFormatException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

}
