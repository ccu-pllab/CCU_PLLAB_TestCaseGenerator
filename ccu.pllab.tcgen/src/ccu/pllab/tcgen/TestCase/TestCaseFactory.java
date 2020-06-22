package ccu.pllab.tcgen.TestCase;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class TestCaseFactory {
	String prePath = "C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG\\";
	public TestCaseFactory() {

	}
  
	public void createTestCase(String fileName) throws FileNotFoundException {
		try {
			File file = new File(prePath+fileName + ".txt");
			
			if (file.isFile() && file.exists())
            { 			
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String content="";
                while ((content = bufferedReader.readLine()) != null)
                {
                    System.out.println("file content: "+ content);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("File not found.");
            }
		} catch (Exception e) {

		} finally {

		}
	}

}
