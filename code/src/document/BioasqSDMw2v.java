package step2Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import utlis.BioasqProcessUtil;


public class BioasqSDMw2v {
	static Map<Integer,String> map = new HashMap<Integer,String>();
	static Map<String,String> mapTypes = new HashMap<String,String>();
	/**读相似单词的文件*/
	public void setDataToMap(String similar,String type) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(similar));
		BufferedReader bufferedReaderTypes = new BufferedReader(new FileReader(type));
		String word = "";
		String types = "";
		int count = 1;
		while((word=bufferedReader.readLine())!=null){
			map.put(count, word.trim());
			count++;
		}
		while((types=bufferedReaderTypes.readLine())!=null){
			mapTypes.put(types.trim(), "");
		}
		bufferedReader.close();
		bufferedReaderTypes.close();
	}
	
	public void bioasqBaselinew2v(String outqueriesFileName,String batchFile) throws Exception {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outqueriesFileName));
		 bufferedWriter.write("{");
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"queries\" :[");
		 bufferedWriter.newLine();
		 int count_index = 1;
		 File file=new File(batchFile);
		 String files[];
		 files=file.list();
		 int num = files.length;
		 for(int i=1;i<=num;i++){
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile+ i +".txt"));
			 String question = "";
			 String str_question = "";
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 bufferedReader.close();
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();
			 String str1 = bioasqProcessUtil.format(str_question.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 /**w2v*/
			 System.out.println(str3);
			 String str4[] = str3.trim().split(" ");
			 String str5 = "";
			 for(int j=0;j<str4.length;j++){
				 /**拼近义词*/
				 if(mapTypes.containsKey(str4[j].trim())){
					 System.out.println(count_index);
					 String[] strWord = map.get(count_index).trim().split(" ");
					 if(strWord.length>4){
						 str5 = str5 + "#syn(" + strWord[0] + " " + strWord[1] + " " + strWord[2] + " " + strWord[3] + " )";
					 }else{
						 String tmp="";
						 for(int t=0;t<strWord.length;t++){
							 tmp=tmp + " " +strWord[t];
							 
						 }
						 str5 = str5 + "#syn(" +tmp+ " )";
					 }
					 
					 count_index++;
				 } else {
					 str5 = str5 + "#syn("+str4[j].trim()+")";
				 }
				
				 
			 }
			 
			 /**sdm*/			 
			 String str6 = "";
			 String str7 = "";
			 for(int k=0;k<str4.length-1;k++){
				 str6 = str6 + "#od:1(" +  str4[k] + " " + str4[k+1] + ")";
				 str7 = str7 + "#uw:8(" +  str4[k] + " " + str4[k+1] + ")";
			 }
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
			 if(str5.trim().length()>0){
				 if(str6.trim().length()>0){
					 bufferedWriter.write("    \"text\" : \"#combine:0=0.85:1=0.1:2=0.05(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+"))#combine("+str6+")#combine("+str7+"))\"");
				 }else{
					 bufferedWriter.write("    \"text\" : \"#combine:0=1(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+")))\"");
				 }
			 }else{				 
				 if(str6.trim().length()>0){
					 bufferedWriter.write("    \"text\" : \"#combine:0=0.85:1=0.1:2=0.05(#combine("+str3+")#combine("+str6+")#combine("+str7+"))\"");
				}else{
					 bufferedWriter.write("    \"text\" : \"#combine("+str3+")\""); 
				}
			 }
			  bufferedWriter.newLine();
			  if(i==num){
				  bufferedWriter.write("  }");
				  bufferedWriter.newLine();
			  } else {
				  bufferedWriter.write("  },");
				  bufferedWriter.newLine();
			  }
		 }
		bufferedWriter.write("]}");
	    bufferedWriter.newLine();
		bufferedWriter.close();
	}
	public static void main(String[] args) throws Exception {
		String type="lib/types.txt";
		String similar="bioasq5b_batch1/alluse/bioasq5b_batch1_similar.txt";
		String batchFile="bioasq5b_batch1/questions/";
		String outqueriesFileName="bioasq5b_batch1/doc_queries/w2v/bioasq5b_batch1_w2v.queries";		
		BioasqSDMw2v bioasqSDMw2v = new BioasqSDMw2v();
		bioasqSDMw2v.setDataToMap(similar,type);
		bioasqSDMw2v.bioasqBaselinew2v(outqueriesFileName,batchFile);
		System.out.println("end");
	}

}
