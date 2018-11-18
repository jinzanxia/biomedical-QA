package document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import utlis.BioasqProcessUtil;


public class BioasqTfidfExpansion {
	 HashMap<String , Integer> map = new HashMap<String , Integer>(); 
	/**
	 * bioasq 通过统计历年来query中的词频，将词频倒数作为该词权重，进行查询
	 * windows
	 */
	public void bioasqSDM(String outqueriesFileName,String batchFile,String expansionFile) throws Exception {
		 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outqueriesFileName));
		 bufferedWriter.write("{");
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"verbose\" :true,");
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"casefold\" :true,");
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"requested\" :100,");
		 bufferedWriter.newLine();
		 bufferedWriter.write("    \"uniw\" :0.85,");
		 bufferedWriter.newLine();
		 bufferedWriter.write("    \"odw\" :0.10,");
		 bufferedWriter.newLine();
		 bufferedWriter.write("    \"uww\" :0.05,");
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"queries\" :[");
		 bufferedWriter.newLine();
		 File file=new File(batchFile);
		 String files[];
		 files=file.list();
		 int num = files.length;
		 for(int i=1;i<=num;i++){
			 String question = "";
			 String str_question = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile + i +".txt"));
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 bufferedReader.close();
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();
			 String str1 = bioasqProcessUtil.format(str_question.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 /**expansion*/
			 BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader(expansionFile+ i +".txt"));
			 String expansion = "";
			 String str_expansion = "";
			 while((expansion=bufferedReaderExpansion.readLine())!=null){
				 str_expansion = str_expansion + expansion + " ";
			 }
			 bufferedReaderExpansion.close();
			 String str8 = bioasqProcessUtil.format(str_expansion.trim());
			 String str9 = bioasqProcessUtil.filterSpace(str8.trim());
			 String str10 = bioasqProcessUtil.stopwords(str9.trim());
			 
			 String []str7= str3.trim().split(" ");
			 String info="";
			 String weight="";
			 int n=0;
			 for(int k=0;k<str7.length;k++){
				 if(!map.containsKey(str7[k])){
					 map.put(str7[k], 1);
				 }
				 info=info+" "+str7[k];
				 float floatnum=map.get(str7[k]);
				 float thisweight=100/floatnum;
				 weight=weight+":"+n+"="+thisweight;
				 n++;
			 }
			 //组合sdm模型
			 String str4 = "";
			 String str5 = "";
			 String []str6 = str3.trim().split(" ");
			 for(int k=0;k<str6.length-1;k++){
				 str4 = str4 + "#od:1(" +  str6[k] + " " + str6[k+1] + ")";
				 str5 = str5 + "#uw:8(" +  str6[k] + " " + str6[k+1] + ")";
			 }
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
			 if(str4!=""){
				 bufferedWriter.write("    \"text\" : \"#combine:0=0.85:1=0.08:2=0.04:3=0.03(#combine:0=2:1=1(#combine("+str3.trim()+")#combine"+weight+"("+info+"))#combine("+str4.trim()+")#combine("+str5.trim()+")#combine("+str10.trim()+"))\"");
			 }else{
				 bufferedWriter.write("    \"text\" : \"#combine:0=0.63:1=0.33:2=0.04(#combine("+str3.trim()+")#combine"+weight+"("+info+")#combine("+str10.trim()+"))\"");
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
	public void storequeryTerm(){
		String str = "";
		try {
//			BufferedReader inputStopWord = new BufferedReader(new FileReader("lib/TitAbstwordFreq.txt"));
			BufferedReader inputStopWord = new BufferedReader(new FileReader("lib/queryWordFreq.txt"));
			while((str = inputStopWord.readLine())!=null){
				String[] terms=str.split("\t");
				map.put(terms[0], Integer.parseInt(terms[1]));
			}
			inputStopWord.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		/**ustb_prir5*/
		String batchFile="bioasq4b_batch3/questions/";
		String outqueriesFileName="bioasq4b_batch3/doc_queries/tfidf/bioasq4b_batch3_tfidf_expansion.queries";	
		String expansionFile="bioasq4b_batch3/doc_expansion/nn/";
		BioasqTfidfExpansion bioasqSDM = new BioasqTfidfExpansion();
		bioasqSDM.storequeryTerm();
		bioasqSDM.bioasqSDM(outqueriesFileName, batchFile,expansionFile);
		System.out.print("end");
	}

}
