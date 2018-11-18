package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class BioasqDLWordToNum {
	static Map<String,Integer> maps = new HashMap<String,Integer>();;
	public void setMaps() throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(""));
		String word = "";
		int index = 0;
		while((word=bufferedReader.readLine())!=null){
			maps.put(word, index);
			index++;
		}
		bufferedReader.close();
	}
	
	public void wordProcess() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(""));
		BufferedWriter bw = new BufferedWriter(new FileWriter("",true));
		String snippet = "";
		while((snippet=br.readLine())!=null){
			String word[] = snippet.split(" ");
			String new_snippet = "";
			for(int i=0;i<word.length;i++){
				if(maps.containsKey(word[i].trim())){
					new_snippet = new_snippet + word[i] + " ";
				} else {
					new_snippet = new_snippet + "*unknow*" + " ";
				}
			}
			bw.write(new_snippet.trim());
			bw.newLine();
		}
		br.close();
		bw.close();
	}
	
	public void wordToNum() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(""));
		BufferedWriter bw = new BufferedWriter(new FileWriter("",true));
		String snippet = "";
		while((snippet=br.readLine())!=null){
			String word[] = snippet.split(" ");
			String snippet_num = "";
			for(int i=0;i<word.length;i++){
				snippet_num = snippet_num + maps.get(word[i].trim()) + " ";
			}
			bw.write(snippet_num.trim());
			bw.newLine();
		}
		br.close();
		bw.close();
	}
	
	
	
	public static void main(String[] args) {
		
	}	

}
