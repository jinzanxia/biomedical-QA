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


public class BioasqMyFSDM {
	/**
	 * galago FSDM
	 * windows
	 * ��FSDM�Ĳ�ѯ��ֻ��Ҫ��ȡ��ѯ�������ļ��� �Ѿ�������
	 */
	public void bioasqBaseLine(String outqueriesFileName,String batchFile,String baseTxt) throws Exception{
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
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
//			 bufferedWriter.write("    \"text\" : \"#fieldedsdm:uw.attributes.width=8:uw.width=4("+str3.trim()+")\"");					 
			 bufferedWriter.write("    \"text\" : \"#fieldedsdm("+str3.trim()+")\"");					 
//			 bufferedWriter.write("    \"text\" : \"#combine:0=0.6:1=0.4(#sdm("+str3.trim()+")#fieldedsdm("+str3.trim()+")\""); 
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
		String batchFile="questions/batch1/";
		String outqueriesFileName="queries/myquery/query_myFSDM/bioasq4b_batch1_sdmfsdm.queries";	
		String baseTxt="basefsdmw2.txt";
		BioasqMyFSDM bioasqBaseline = new BioasqMyFSDM();
		bioasqBaseline.bioasqBaseLine(outqueriesFileName,batchFile,baseTxt);
	}

}
