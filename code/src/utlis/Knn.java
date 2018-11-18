package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Knn {
	static int flag = 1;
	public static void  main(String[] args) throws IOException{
		/**读单词到map中*/
		HashMap<String,String> map = new HashMap<String,String>();
		FileReader fr = new FileReader("d:/fangfan/word");
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter("D:/fangfan/1.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		/**输出单词到txt中*/
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("d:/space/words.txt",true));
		String line =  "";
		String line1 = "";
		int count =0;
		while(((line = br.readLine())!=null)){
			count++;
			map.put(line.trim(),count+"");
			System.out.println("count="+count);
		}
		System.out.println(map.size() + "====");
		count = 0;
		br.close();
		fr.close();
		for(int i=1;i<=100;i++){
			FileReader fr2 = new FileReader("D:/fangfan/questions_3/" + i +".txt");
			BufferedReader br2 = new BufferedReader(fr2);
			String lines = "";
			String str = "";
			while((str = br2.readLine())!=null){
				lines = lines + str + " ";
			}
			br2.close();
			fr2.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(lines.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());
			String [] arr = str3.trim().split(" ");
			for(int j=0;j<arr.length;j++){
				if(map.containsKey(arr[j].trim())){
					System.out.println(flag++);
					bufferedWriter.write(arr[j]);
					bufferedWriter.newLine();
					bw.write(map.get(arr[j]));
					bw.newLine();
				}
			}
		}
		bw.close();	
		fw.close();
		bufferedWriter.close();
	}
}
