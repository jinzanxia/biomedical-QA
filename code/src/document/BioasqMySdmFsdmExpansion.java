package step2Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utlis.BioasqProcessUtil;


public class BioasqMySdmFsdmExpansion {
	
	public void bioasqSDMExpansion(String outqueriesFileName,String batchFile,String baseTxt,String expansionFile) throws Exception {
		 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outqueriesFileName));
		 bufferedWriter.write("{");
		 bufferedWriter.newLine();
		 String line="";
		 BufferedReader bufferedReaderbase = new BufferedReader(new FileReader(baseTxt));
		 while((line=bufferedReaderbase.readLine())!=null){
			 bufferedWriter.write(line);
			 bufferedWriter.newLine();
		 }
		 bufferedReaderbase.close();
		 bufferedWriter.write(" \"queries\" :[");
		 bufferedWriter.newLine();
		 File file=new File(batchFile);
		 String files[];
		 files=file.list();
		 int num = files.length;		
		 for(int i=1;i<=num;i++){
			 String question = "";
			 String expansion = "";
			 String str_question = "";
			 String str_expansion = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile+ i +".txt"));
			 BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader( expansionFile+ i +".txt"));
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();//可以修改停用词表
			 String str1 = bioasqProcessUtil.format(str_question.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 
			 while((expansion=bufferedReaderExpansion.readLine())!=null){
				 str_expansion = str_expansion + expansion + " ";
			 }
			 bufferedReader.close();
			 bufferedReaderExpansion.close();
			 String str7 = bioasqProcessUtil.format(str_expansion.trim());
			 String str8 = bioasqProcessUtil.filterSpace(str7.trim());
			 String str9 = bioasqProcessUtil.stopwords(str8.trim());
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
//			bufferedWriter.write("    \"text\" : \"#combine:0=0.67:1=0.27:2=0.06(#sdm("+str3.trim()+")#fieldedsdm("+str3.trim()+")#sdm("+str9.trim()+"))\"");
			 bufferedWriter.write("    \"text\" : \"#combine:0=0.60:1=0.25:2=0.15(#sdm("+str3.trim()+")#fieldedsdm("+str3.trim()+")#sdm("+str9.trim()+"))\"");
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
		String outqueriesFileName="bioasq5b_batch5/doc_queries/fsdm/bioasq5b_batch5_sdmfsdmw2_expansionw2.queries";
		String batchFile="bioasq5b_batch5/questions/";
		String baseTxt="lib/basefsdmw2.txt";
		String expansionFile="bioasq5b_batch5/doc_expansion/fsdmTitle/";
		BioasqMySdmFsdmExpansion bioasqSDMExpansion = new BioasqMySdmFsdmExpansion();
		bioasqSDMExpansion.bioasqSDMExpansion(outqueriesFileName,batchFile,baseTxt,expansionFile);
		System.out.println("end");
	}

}
