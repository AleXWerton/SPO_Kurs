package kurs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class UtilHelper
{
	
	public void printFile(String filename) throws IOException
	{
		File file = new File(filename);
		Reader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		int lineNumber=0;
		while((line = bufferedReader.readLine()) != null)
		{
			System.out.println(lineNumber+": "+line);
			lineNumber++;
			bufferedReader.close();
		}
	}

}
