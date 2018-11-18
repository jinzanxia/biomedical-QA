package step2Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import utlis.BioasqProcessUtil;


public class BioasqMyPdfrExpansion {
	/**
	 * galago PDFR
	 * windows
	 * 
	 */
	public void bioasqBaseLine(String outqueriesFileName,String batchFile,String baseTxt,String expansionFile) throws Exception{
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
			 String str_question = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile+ i +".txt"));
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
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
//			 bufferedWriter.write("    \"text\" : \"#pdfr("+str3.trim()+")\"");					 
		     bufferedWriter.write("    \"text\" : \"#combine:0=0.7:1=0.25:2=0.05(#sdm("+str3.trim()+")#pdfr("+str3.trim()+")#sdm("+str10.trim()+"))\",");
//			 bufferedWriter.write("    \"text\" : \"#combine:0=0.7:1=0.25:2=0.05(#sdm("+str3.trim()+")#pdfr("+str3.trim()+")#pdfr("+str10.trim()+"))\",");
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
		String batchFile="bioasq4b_batch1/questions/";
		String outqueriesFileName="bioasq4b_batch1/doc_queries/pdfr/bioasq4b_batch1_pdfr_expansion.queries";		
		String baseTxt="bioasq4b_batch1/alluse/basepdfr.txt";
		String expansionFile="bioasq4b_batch1/doc_expansion/pdfr/";
		BioasqMyPdfrExpansion bioasqBaseline = new BioasqMyPdfrExpansion();
		bioasqBaseline.bioasqBaseLine(outqueriesFileName,batchFile,baseTxt,expansionFile);
		System.out.println("end");
	}

}
