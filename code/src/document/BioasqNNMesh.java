package document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import utlis.BioasqProcessUtil;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class BioasqNNMesh {
	/**
	 * bioasqNN名词
	 * windows
	 * 需要分词工具把名词提取出来，读取两个文件（原查询和名词文件），并一一对应好
	 */
	public void bioasqNN(String outqueriesFileName,String batchFile,String baseTxt,String combine1,String combine2) throws Exception {
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
			 String str_tagger = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile + i +".txt"));
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 bufferedReader.close();
			 MaxentTagger tagger = new MaxentTagger("stanford-postagger-full-2015-04-20/models/wsj-0-18-bidirectional-nodistsim.tagger");  
			 List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(str_question.trim()));  
			 for (List<HasWord> sentence : sentences) {  
				 ArrayList<TaggedWord> tSentence = (ArrayList<TaggedWord>) tagger.tagSentence(sentence);  
				 str_tagger = Sentence.listToString(tSentence, false);
			     System.out.println(str_tagger);
			 }  
			 String [] str_split = str_tagger.trim().split(" ");
			 String str_nn = "";
			 String str_other = "";//不是名词的字符串
			 for(int j=0;j<str_split.length;j++){
				 if(str_split[j].endsWith("/NN")||str_split[j].endsWith("/NNS")||str_split[j].endsWith("/NNP")||str_split[j].endsWith("/NNPS")){
					 str_nn = str_nn + str_split[j].split("/")[0] + " ";
				 } else {
					 str_other = str_other + str_split[j].split("/")[0] + " ";
				 }
			 }
			 /**名词的预处理*/
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();
			 String str1 = bioasqProcessUtil.format(str_nn.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			 /**原查询处理*/
			 String str4 = bioasqProcessUtil.format(str_question.trim());
			 String str5 = bioasqProcessUtil.filterSpace(str4.trim());
			 String str6 = bioasqProcessUtil.stopwords(str5.trim());
			 /**非名词查询处理*/
			 String str7 = bioasqProcessUtil.format(str_other.trim());
			 String str8 = bioasqProcessUtil.filterSpace(str7.trim());
			 String str9 = bioasqProcessUtil.stopwords(str8.trim());
			 bufferedWriter.write("  {");
			 bufferedWriter.newLine();
			 bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			 bufferedWriter.newLine();  
			 if(str3.trim().length()==0){
				 bufferedWriter.write("    \"text\" : \"#sdm("+str6.trim()+")\"");
			 } else {
				 bufferedWriter.write("    \"text\" : \"#combine:0="+combine1+":1="+combine2+"(#sdm("+str6.trim()+")#fieldedsdm("+str3.trim()+"))\"");
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
		String batchFile="bioasq5b_batch5/questions/";
		String outqueriesFileName="bioasq5b_batch5/doc_queries/nnMesh/bioasq5b_batch5_NNsdmfsdmw3Mesh.queries";
		String baseTxt="lib/basefsdmw3Mesh.txt";
		BioasqNNMesh bioasqNN = new BioasqNNMesh();
		bioasqNN.bioasqNN(outqueriesFileName,batchFile,baseTxt,"0.85","0.15");
	
		System.out.println("end");
	}

}
