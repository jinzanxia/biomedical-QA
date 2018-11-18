package document;
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


public class BioasqMySdmFsdm {
	/**
	 * galago FSDM+SDM
	 * windows
	 * 做FSDM的查询，只需要读取查询（问题文件） 
	 */
	public void bioasqBaseLine(String outqueriesFileName,String batchFile,String baseTxt,String sdm,String fsdm) throws Exception{
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
			 bioasqProcessUtil.storeStopWords();//可以修改停用词表
			 String str1 = bioasqProcessUtil.format(str_question.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
			 //在全局进行sdm查询+在规定的域里进行fsdm查询
			 bufferedWriter.write("    \"text\" : \"#combine:0="+sdm+":1="+fsdm+"(#sdm("+str3.trim()+")#fieldedsdm("+str3.trim()+"))\""); 
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
		String batchFile="bioasq5b_batch5/questions/";
		String outqueriesFileName="bioasq5b_batch5/doc_queries/fsdm/bioasq5b_batch5_sdmfsdmw2.queries";	
		String baseTxt="lib/basefsdmw2.txt";
		BioasqMySdmFsdm bioasqBaseline = new BioasqMySdmFsdm();
		bioasqBaseline.bioasqBaseLine(outqueriesFileName,batchFile,baseTxt,"0.7","0.3");
		System.out.println("end");
	}

}
