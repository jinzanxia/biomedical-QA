package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Time {
public void getFilePath1(File dir) {
		
		File[] tempList = dir.listFiles();
		if(tempList==null){
			return;
		}
		for(int i=0;i<tempList.length;i++){
			if(tempList[i].isFile()&&tempList[i].getAbsolutePath().endsWith("xml")){
				String pathName = tempList[i].getName().substring(0, tempList[i].getName().length()-4);
				String absolutePath = tempList[i].getAbsolutePath();
				try {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:/test/path2.txt",true));
					bufferedWriter.write(pathName + "##" + absolutePath);
					bufferedWriter.newLine();
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(tempList[i].isDirectory()){
				getFilePath1(tempList[i]);
			}
		}
	}
	public static void main(String[] args) throws Exception {
		BufferedReader bufferedReader1 = new BufferedReader(new FileReader(""));
		BufferedReader bufferedReader2 = new BufferedReader(new FileReader(""));
		
	}

}
