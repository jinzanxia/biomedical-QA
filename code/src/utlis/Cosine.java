package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cosine {
	
	static Map<String,String> map = new HashMap<String,String>();
	static Map<String,Double> mod = new HashMap<String,Double>();
	/**读到内存中*/
	public void readData() throws Exception {
		BufferedReader bufferedReaderVec = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/vectors.txt"));
		BufferedReader bufferedReaderTypes = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/types.txt"));
		BufferedReader bufferedReaderMod = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/mod.txt"));
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String str4 = "";
		while((str1=bufferedReaderVec.readLine())!=null&&(str2=bufferedReaderTypes.readLine())!=null){
			map.put(str2,str1);
		}
		while((str3=bufferedReaderMod.readLine())!=null&&(str4=bufferedReaderTypes.readLine())!=null){
			mod.put(str4,Double.parseDouble(str3));
		}
		bufferedReaderVec.close();
		bufferedReaderTypes.close();
		bufferedReaderMod.close();
	} 
	public void readData2() throws Exception {
		BufferedReader bufferedReaderTypes = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/types.txt"));
		BufferedReader bufferedReaderMod = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/mod.txt"));
		String str3 = "";
		String str4 = "";
		while((str3=bufferedReaderMod.readLine())!=null&&(str4=bufferedReaderTypes.readLine())!=null){
			mod.put(str4,Double.parseDouble(str3));
		}
		bufferedReaderTypes.close();
		bufferedReaderMod.close();
	} 
	/**计算相似度*/
	public void cosSimilar(String str) throws Exception {
		Map<String,Double> map_cosine = new HashMap<String,Double>();
		if(map.containsKey(str)){
			for(Map.Entry<String,String> entry : map.entrySet()){
				double data = cosine(map.get(str).toString(),entry.getValue().toString(),mod.get(str),mod.get(entry.getKey()));
				System.out.println(data);
				map_cosine.put(entry.getKey(), data);
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/data/EveryOne/zll/bioasq4/w2v_syn/data.txt",true));
			for(Map.Entry<String, Double> map_data :map_cosine.entrySet()){
				bufferedWriter.write(map_data.getKey() + "\\t"+ map_data.getValue());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		
	}
	/**调用linux命令排序，并返回结果*/
	public void sortByLinux() throws Exception {
		String commands = "sort -rn -k2 /data/EveryOne/zll/bioasq4/w2v_syn/data.txt";
		Process process = Runtime.getRuntime().exec(commands);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		BufferedReader input = new BufferedReader (ir);
		int count = 1;
		String line;
		while ((line = input.readLine ()) != null){
			if(count<5){
				System.out.println(line);
			}
			count++;
		}
	}
	
	
	/**读取单词向量，并计算相似度
	public List<Vec> cosSimilarity(String str,int index) throws Exception {
		List<Vec> list = new ArrayList<Vec>();
		
		if(map.containsKey(str)){
			for(Map.Entry<String,String> entry : map.entrySet()){
				System.out.println(11);
				double data = cosine(map.get(str).toString(),entry.getValue().toString(),mod.get(str),mod.get(entry.getKey()));
				System.out.println(22);
				Vec vec = new Vec();
				vec.setTypes(entry.getKey());
				vec.setVec(data);
				if(list.size()<5){
					list.add(vec);
					//这里将map.entrySet()转换成list
			        //List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map_double.entrySet());
			        //然后通过比较器来实现排序
			        Collections.sort(list,new Comparator<Vec>() {
			            public int compare(Vec o1,Vec o2) {
			                return o2.getVec().compareTo(o1.getVec());
			            }
			        });
				} else {
					if(list.get(4).getVec()<vec.getVec()){
						list.remove(4);
						list.add(vec);
						Collections.sort(list,new Comparator<Vec>() {
				            public int compare(Vec o1,Vec o2) {
				                return o2.getVec().compareTo(o1.getVec());
				            }
				        });
					}
				}
				System.out.println(33);
			}
		}
		return list;
	}
	*/
	
	
	public double cosine(String str1, String str2, Double mod1, Double mod2) {
		double addData = 0; 
		String[] d1 = str1.trim().split(" ");
		String[] d2 = str2.trim().split(" ");
		for (int i=0;i<d1.length;i++) {
			for (int j=0;j<d2.length;j++){
				if (i==j)
					addData += (Double.parseDouble(d1[i]) * Double.parseDouble(d2[j]));
			}
		}
		return addData/(mod1*mod2);
	}
	
	
	public static void main(String[] args) throws Exception {
		Cosine cosine = new Cosine();
		cosine.readData();
		cosine.readData2();
		cosine.cosSimilar("##");
		cosine.sortByLinux();
		//System.out.println(mod.size());
		//cosine.cosSimilarity("##", 0);
		String question = "";
		String str_question = "";
		
//		for(int i=0;i<=1306;i++){
//			BufferedReader bufferedReader = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/split/" + i +".txt"));
//			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/data/EveryOne/zll/bioasq4/w2v_syn/" + (i+1) + ".txt",true));
//			while((question=bufferedReader.readLine())!=null){
//				 str_question = question + " ";
//			 }
//			bufferedReader.close();
//			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
//			 bioasqProcessUtil.storeStopWords();
//			 String str1 = bioasqProcessUtil.format(str_question.trim());
//			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
//			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
//			 
//			 String str4[] = str3.trim().split(" ");
//			 for(int j=0;j<str4.length;j++){
//				 List<Vec> list = cosine.cosSimilarity(str4[j],(i+1));
//				 String syn = "";
//				   for(Vec vec :list){ 
//					   System.out.println(vec.getTypes() + vec.getVec());
//					   syn = syn + vec.getTypes() + " ";
//			       } 
//				   bufferedWriter.write(syn);
//				   bufferedWriter.newLine();
//			 }
//			 bufferedWriter.close();
////			  // BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("d:/space/syn.txt"));
//		}
	}

}
