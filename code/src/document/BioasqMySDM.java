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


public class BioasqMySDM {
	/**
	 * galago SDM
	 * windows
	 * 生成sdm查询，输入为问题文件,输出为query文件
	 */
	public void sdm(String batch,String outqueriesFileName) throws Exception{
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
		 //bufferedWriter.write("    \"windowLimit\" :3,"); 
		 bufferedWriter.newLine();
		 bufferedWriter.write(" \"queries\" :[");
		 bufferedWriter.newLine();
		 String InbaseFile = batch;
		 File file=new File(InbaseFile);
		 String files[];
		 files=file.list();
		 int num = files.length;
		 for(int i=1;i<=num;i++){
			 String question = "";
			 String str_question = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(InbaseFile+ i +".txt"));
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 bufferedReader.close();
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();
			 String str1 = bioasqProcessUtil.format(str_question.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
			 bufferedWriter.write("    \"text\" : \"#sdm("+str3.trim()+")\"");					 
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
		BioasqMySDM retrievalModel = new BioasqMySDM();
		String batch="bioasq5b_batch1/questions/";
		String outqueriesFileName="bioasq5b_batch1/doc_queries/sdm/bioasq5b_batch1_sdmw3.queries";
		retrievalModel.sdm(batch,outqueriesFileName);
		System.out.println("end");
	}

}
