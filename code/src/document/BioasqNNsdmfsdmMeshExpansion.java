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


public class BioasqNNsdmfsdmMeshExpansion {
	
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
			 String str_tagger = "";
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(batchFile+ i +".txt"));
			 BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader( expansionFile+ i +".txt"));
			 while((question=bufferedReader.readLine())!=null){
				 str_question = str_question + question + " ";
			 }
			 MaxentTagger tagger = new MaxentTagger("stanford-postagger-full-2015-04-20/models/wsj-0-18-bidirectional-nodistsim.tagger");  
			 List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(str_question.trim()));  
			 for (List<HasWord> sentence : sentences) {  
				 ArrayList<TaggedWord> tSentence = (ArrayList<TaggedWord>) tagger.tagSentence(sentence);  
				 str_tagger = Sentence.listToString(tSentence, false);
			     System.out.println(str_tagger);
			 }  
			 String [] str_split = str_tagger.trim().split(" ");
			 String str_nn = "";
			 String str_other = "";
			 for(int j=0;j<str_split.length;j++){
				 if(str_split[j].endsWith("/NN")||str_split[j].endsWith("/NNS")||str_split[j].endsWith("/NNP")||str_split[j].endsWith("/NNPS")){
					 str_nn = str_nn + str_split[j].split("/")[0] + " ";
				 } else {
					 str_other = str_other + str_split[j].split("/")[0] + " ";
				 }
			 }
			 BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			 bioasqProcessUtil.storeStopWords();
			 String str4 = bioasqProcessUtil.format(str_nn.trim());
			 String str5 = bioasqProcessUtil.filterSpace(str4.trim());
			 String str6 = bioasqProcessUtil.stopwords(str5.trim());
			 
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
			 if(str6.trim().length()>0){
				 if(str9.trim().length()>0){
					 bufferedWriter.write("    \"text\" : \"#combine:0=0.60:1=0.25:2=0.15(#sdm("+str3.trim()+")#fieldedsdm("+str6.trim()+")#sdm("+str9.trim()+"))\"");						 
				 }else{
					 bufferedWriter.write("    \"text\" : \"#combine:0=0.85:1=0.15(#sdm("+str3.trim()+")#fieldedsdm("+str6.trim()+"))\"");						 						
				 }
			 }else{
				 if(str9.trim().length()>0){
					 bufferedWriter.write("    \"text\" : \"#combine:0=0.75:1=0.25(#sdm("+str3.trim()+")#sdm("+str9.trim()+"))\"");						 
				 }else{
					 bufferedWriter.write("    \"text\" : \"#sdm("+str3.trim()+")\"");						 						
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
		String outqueriesFileName="bioasq5b_batch5/doc_queries/nnMesh/bioasq5b_batch5_NNsdmfsdmw3Mesh_expansionw3mesh.queries";
		String batchFile="bioasq5b_batch5/questions/";
		String baseTxt="lib/basefsdmw3Mesh.txt";
		String expansionFile="bioasq5b_batch5/doc_expansion/nnMesh/";
		BioasqNNsdmfsdmMeshExpansion bioasqSDMExpansion = new BioasqNNsdmfsdmMeshExpansion();
		bioasqSDMExpansion.bioasqSDMExpansion(outqueriesFileName,batchFile,baseTxt,expansionFile);
		System.out.println("end");
	}

}
