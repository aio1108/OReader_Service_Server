package com.hyweb.mybatis.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Properties;

public class i18nGenTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			Properties p = new Properties();
			File file = new File("c:/NiiDataDictionary.properties");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String _outputPath = "c:/NiiDataDictionary_i18n_zh_TW.properties";
			BufferedWriter outfile = new BufferedWriter(new FileWriter(_outputPath));			
			
			String str;
			while ((str = br.readLine()) != null) {
				String[] temp = str.split("=");
				if(temp.length<2) continue;
				String outputStr = temp[0].trim()+" = "+toHexString(temp[1].trim());
				System.out.println(outputStr);
				outfile.write(outputStr+"\n");
			}
			outfile.close();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static String toHexString(String str) 
	{
	   String strResult = "";
	   for(int i=0; i<str.length(); i++)
	   {
		   char c = str.charAt(i);
		   String s2 = "0000"+Integer.toHexString(c);
		   strResult += "\\u"+ s2.substring(s2.length()-4);
	   }
	   return strResult;
	}
	
}
