package snippet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import utlis.BioasqProcessUtil;

public class BioasqSnippetRetrievalModel {
	static Map<Integer,String> map = new HashMap<Integer,String>();
	static Map<String,String> mapTypes = new HashMap<String,String>();
	/*读取相似的单词*/
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
	/**snippet的w2v拼sdm检索模型*/
	public void w2v(String similar,String type,int questionNum,String batch,String out) throws Exception {
		setDataToMap(similar, type);
		int count_index = 1;
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			System.out.println(str3);
			 String str4[] = str3.trim().split(" ");
			 String str5 = "";
			 for(int j=0;j<str4.length;j++){
				 if(mapTypes.containsKey(str4[j].trim())){
					 System.out.println(count_index);
					 String[] strWord = map.get(count_index).trim().split(" ");
					 str5 = str5 + "#syn(" + strWord[0] + " " + strWord[1] + " " + strWord[2] + " " + strWord[3] + ")";
					 count_index++;
				 }
			 }			 
			String str6 = "";
			String str7 = "";
			for(int k=0;k<str4.length-1;k++){
				str6 = str6 + "#od:1(" +  str4[k] + " " + str4[k+1] + ")";
				str7 = str7 + "#uw:8(" +  str4[k] + " " + str4[k+1] + ")";
			}
			BufferedWriter bufferedWriterSdm = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriterSdm.write("{");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write(" \"queries\" :[");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  {");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriterSdm.newLine();  
			 if(str5.length()>0){
				 if(str6.length()>0){
					 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.85:1=0.1:2=0.05(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+"))#combine("+str6+")#combine("+str7+"))\"");
				 }else{
					 bufferedWriterSdm.write("    \"text\" : \"#combine:0=1(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+")))\"");
				 }
			 }else{
				 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.85:1=0.1:2=0.05(#combine("+str3+")#combine("+str6+")#combine("+str7+"))\"");
			 }bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  }");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("]}");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.close();
		}	
	}
	/**snippet的baseline检索模型*/
	public void baseline(int questionNum,String batch,String out) throws Exception {
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			bufferedWriter.write("    \"text\" : \"#combine("+str3.trim()+")\"");
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	/**snippet的sdm检索模型，只有sdm，可添加widowLimit:3*/
	public void sdm(int questionNum,String batch,String out) throws Exception {
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"uniw\" : 0.85,\n \"odw\" : 0.15,\n \"uww\" : 0.05,");
			bufferedWriter.newLine();
//			bufferedWriter.write(" \"windowLimit\" : 3,");
//			bufferedWriter.newLine();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			bufferedWriter.write("    \"text\" : \"#sdm("+str3.trim()+")\"");
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	/**snippet的sdm+expansion检索模型*/
	public void sdmExpansion(int questionNum,String batch,String out,String expansionfile) throws Exception {
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
				
			String expansion = "";
			String str_expansion = "";
			BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader( expansionfile+ i +".txt"));
			while((expansion=bufferedReaderExpansion.readLine())!=null){
				str_expansion = str_expansion + expansion + " ";
			}
			bufferedReaderExpansion.close();
			String str7 = bioasqProcessUtil.format(str_expansion.trim());
			String str8 = bioasqProcessUtil.filterSpace(str7.trim());
			String str9 = bioasqProcessUtil.stopwords(str8.trim());
			/*
			BufferedWriter bufferedWriterSdm = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriterSdm.write("{");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write(" \"uniw\" : 0.85,\n \"odw\" : 0.10,\n \"uww\" : 0.05,");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write(" \"queries\" :[");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  {");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriterSdm.newLine();  
			bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.95:1=0.05(#sdm("+str3.trim()+")#sdm("+str9.trim()+"))\"");	
			bufferedWriterSdm.newLine();
			*/	
			/* */
			String str4 = "";
			String str5 = "";
			String []str6 = str3.trim().split(" ");
			for(int k=0;k<str6.length-1;k++){
				str4 = str4 + "#od:1(" +  str6[k] + " " + str6[k+1] + ")";
				str5 = str5 + "#uw:8(" +  str6[k] + " " + str6[k+1] + ")";
			}
			BufferedWriter bufferedWriterSdm = new BufferedWriter(new FileWriter(out+ i + ".queries"));
			bufferedWriterSdm.write("{");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write(" \"queries\" :[");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  {");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriterSdm.newLine();  
			 if(str4!=""){
				 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.85:1=0.08:2=0.04:3=0.03(#combine("+str3.trim()+")#combine("+str4.trim()+")#combine("+str5.trim()+")#combine("+str9.trim()+"))\"");
			 }else{
				 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.95:1=0.05(#combine("+str3.trim()+")#combine("+str9.trim()+"))\"");
			 }
			 bufferedWriterSdm.newLine(); 
			
			bufferedWriterSdm.write("  }");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("]}");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.close();
		}
	}
	/**snippet的sdm+NN检索模型*/
	public void sdmNN(int questionNum,String batch,String out) throws Exception {
		for(int i=1;i<=questionNum;i++){
			String str_tagger = "";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			 MaxentTagger tagger = new MaxentTagger("/media/prir1005/data/jzx/stanford-postagger-full-2015-04-20/models/wsj-0-18-bidirectional-nodistsim.tagger");  
			 List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(str_snippet.trim()));  
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
			 
			 String str1 = bioasqProcessUtil.format(str_snippet.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"uniw\" : 0.85,\n \"odw\" : 0.15,\n \"uww\" : 0.05,");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			 if(str6.trim().length()==0){
				 bufferedWriter.write("    \"text\" : \"#sdm("+str3.trim()+")\"");
			 } else {
				 bufferedWriter.write("    \"text\" : \"#combine:0=0.8:1=0.2(#sdm("+str3.trim()+")#sdm("+str6.trim()+"))\"");
			 }
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	/**snippet的w2v+expansion检索模型*/
	public void w2vExpansion(String similar,String type,int questionNum,String batch,String out,String expansionfile) throws Exception {
		setDataToMap(similar, type);
		int count_index = 1;
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			System.out.println(str3);
			
			String expansion = "";
			String str_expansion = "";
			BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader( expansionfile+ i +".txt"));
			while((expansion=bufferedReaderExpansion.readLine())!=null){
				str_expansion = str_expansion + expansion + " ";
			}
			bufferedReaderExpansion.close();
			String str10 = bioasqProcessUtil.format(str_expansion.trim());
			String str11 = bioasqProcessUtil.filterSpace(str10.trim());
			String str9 = bioasqProcessUtil.stopwords(str11.trim());

			
			 String str4[] = str3.trim().split(" ");
			 String str5 = "";
			 for(int j=0;j<str4.length;j++){
				 if(mapTypes.containsKey(str4[j].trim())){
					 System.out.println(count_index);
					 String[] strWord = map.get(count_index).trim().split(" ");
					 str5 = str5 + "#syn(" + strWord[0] + " " + strWord[1] + " " + strWord[2] + " " + strWord[3] + ")";
					 count_index++;
				 }
			 }			 
			String str6 = "";
			String str7 = "";
			for(int k=0;k<str4.length-1;k++){
				str6 = str6 + "#od:1(" +  str4[k] + " " + str4[k+1] + ")";
				str7 = str7 + "#uw:8(" +  str4[k] + " " + str4[k+1] + ")";
			}
			BufferedWriter bufferedWriterSdm = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriterSdm.write("{");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write(" \"queries\" :[");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  {");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriterSdm.newLine();  
			 if(str5.length()>0){
				 if(str6.length()>0){
					 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.8:1=0.2(#combine:0=0.85:1=0.1:2=0.05(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+"))#combine("+str6+")#combine("+str7+"))#combine("+str9+"))\"");
				 }else{
					 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.8:1=0.2(#combine:0=1(#combine:0=2:1=1(#combine("+str3+")#combine("+str5+")))#combine("+str9+"))\"");
				 }
			 }else{
				 bufferedWriterSdm.write("    \"text\" : \"#combine:0=0.8:1=0.2(#combine:0=0.85:1=0.1:2=0.05(#combine("+str3+")#combine("+str6+")#combine("+str7+"))#combine("+str9+"))\"");
			 }bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("  }");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.write("]}");
			bufferedWriterSdm.newLine();
			bufferedWriterSdm.close();
		}	
	}
	/**snippet的sdm+NN+expansion检索模型*/
	public void sdmNNExpansion(int questionNum,String batch,String out,String expansionfile) throws Exception {
		for(int i=1;i<=questionNum;i++){
			String str_tagger = "";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			 MaxentTagger tagger = new MaxentTagger("/media/prir1005/data/jzx/stanford-postagger-full-2015-04-20/models/wsj-0-18-bidirectional-nodistsim.tagger");  
			 List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(str_snippet.trim()));  
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
			 
				String expansion = "";
				String str_expansion = "";
				BufferedReader bufferedReaderExpansion = new BufferedReader(new FileReader( expansionfile+ i +".txt"));
				while((expansion=bufferedReaderExpansion.readLine())!=null){
					str_expansion = str_expansion + expansion + " ";
				}
				bufferedReaderExpansion.close();
				String str7 = bioasqProcessUtil.format(str_expansion.trim());
				String str8 = bioasqProcessUtil.filterSpace(str7.trim());
				String str9 = bioasqProcessUtil.stopwords(str8.trim());
			 
			 String str1 = bioasqProcessUtil.format(str_snippet.trim());
			 String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			 String str3 = bioasqProcessUtil.stopwords(str2.trim());
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"uniw\" : 0.85,\n \"odw\" : 0.15,\n \"uww\" : 0.05,");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			 if(str6.trim().length()==0){
				 bufferedWriter.write("    \"text\" : \"#sdm("+str3.trim()+")\"");
			 } else {
				 bufferedWriter.write("    \"text\" : \"#combine:0=0.6:1=0.25:2=0.15(#sdm("+str3.trim()+")#sdm("+str6.trim()+")#sdm("+str9.trim()+"))\"");
			 }
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	/**snippet的sdm+PDFR检索模型*/
	public void sdmPDFR(int questionNum,String batch,String out,String basetxt) throws Exception {
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			 String line="";
			 BufferedReader bufferedReaderbase = new BufferedReader(new FileReader(basetxt));
			 while((line=bufferedReaderbase.readLine())!=null){
				 bufferedWriter.write(line);
				 bufferedWriter.newLine();
			 }
			 bufferedReaderbase.close();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			bufferedWriter.write("    \"text\" : \"#combine:0=0.7:1=0.3(#sdm("+str3.trim()+")#pdfr("+str3.trim()+"))\"");
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	/**snippet的sdm+tfidf检索模型*/
	public void sdmtfidf(int questionNum,String batch,String out,String wordfrq) throws Exception {
		 HashMap<String , Integer> wordmap = new HashMap<String , Integer>(); 
		   String line = "";
			BufferedReader inputStopWord = new BufferedReader(new FileReader(wordfrq));
			while((line = inputStopWord.readLine())!=null){
				String[] terms=line.split("\t");
				wordmap.put(terms[0], Integer.parseInt(terms[1]));
			}
			inputStopWord.close();
		for(int i=1;i<=questionNum;i++){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(batch + i +".txt"));
			String str_snippet = "";
			String str = "";
			while((str=bufferedReader.readLine())!=null){
				str_snippet = str_snippet + str + " ";
			}
			bufferedReader.close();
			BioasqProcessUtil bioasqProcessUtil = new BioasqProcessUtil();
			bioasqProcessUtil.storeStopWords();
			String str1 = bioasqProcessUtil.format(str_snippet.trim());
			String str2 = bioasqProcessUtil.filterSpace(str1.trim());
			String str3 = bioasqProcessUtil.stopwords(str2.trim());	
			 String str4 = "";
			 String str5 = "";
			 String []str6 = str3.trim().split(" ");
			 String info="";
			 String weight="";
			 int n=0;
			 for(int k=0;k<str6.length;k++){
				 if(!wordmap.containsKey(str6[k])){
					 wordmap.put(str6[k], 1);
				 }
				 info=info+" "+str6[k];
				 float floatnum=wordmap.get(str6[k]);
				 float thisweight=1/floatnum;
				 weight=weight+":"+n+"="+thisweight;
				 n++;
			 }
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + i + ".queries"));
			bufferedWriter.write("{");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"uniw\" : 0.85,\n \"odw\" : 0.15,\n \"uww\" : 0.05,");
			bufferedWriter.newLine();
			bufferedWriter.write(" \"queries\" :[");
			bufferedWriter.newLine();
			bufferedWriter.write("  {");
			bufferedWriter.newLine();
			bufferedWriter.write("    \"number\" :" + " \"" + i + "\",");
			bufferedWriter.newLine();  
			bufferedWriter.write("    \"text\" : \"#combine:0=0.7:1=0.3(#sdm("+str3.trim()+")#combine"+weight+"("+info+"))\""); 
			bufferedWriter.newLine();
			bufferedWriter.write("  }");
			bufferedWriter.newLine();
			bufferedWriter.write("]}");
		    bufferedWriter.newLine();
			bufferedWriter.close();
		}
	}
	public static void main(String[] args) throws Exception {
		BioasqSnippetRetrievalModel retrievalModel = new BioasqSnippetRetrievalModel();
		int questionNum=100;//注意问题个数，如果不是100，需要修改这里
		String batch="bioasq5b_batch5/questions/";		
		String wordfrq="lib/queryWordFreq.txt";
		String basetxt="lib/basepdfr.txt";
//		String type="lib/types.txt";
//		String similar="bioasq5b_batch2/alluse/bioasq5b_batch2_similar.txt";
//		String expansionfile="bioasq5b_batch1/doc_expansion/nn/";
//		String outnn="bioasq5b_batch2/snippet_queries/nn/";		
//		String outexpansion="bioasq5b_batch1/snippet_queries/expansion/";		
//		String outsdm="bioasq5b_batch1/snippet_queries/sdm/";		
		String outpdfr="bioasq5b_batch5/snippet_queries/pdfr/";		
		String outtfidf="bioasq5b_batch5/snippet_queries/tfidf/";		
		
//		retrievalModel.w2v(similar, type,questionNum, batch, out);
//		retrievalModel.baseline(questionNum, batch, out);
//		retrievalModel.w2vExpansion(similar, type, questionNum, batch, out, expansionfile);
//		retrievalModel.sdmNNExpansion(questionNum, batch, out, expansionfile);
//		retrievalModel.sdmNN(questionNum, batch, outnn);
//		retrievalModel.sdmExpansion(questionNum, batch, outexpansion, expansionfile);
//		retrievalModel.sdm(questionNum, batch, outsdm);
		retrievalModel.sdmPDFR(questionNum, batch, outpdfr, basetxt);
		retrievalModel.sdmtfidf(questionNum, batch, outtfidf, wordfrq);
		System.out.println("end");
	}

}
