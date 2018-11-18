package step3Snippets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class BioasqSnippet {
	/**两个标签*/
	static String[] lable = {"ArticleTitle","AbstractText"};
	/**读取效果最好的链接*/
	public void readXMLFile(String bestResult,String outsnippet) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(bestResult));
		String str = "";
		while((str=bufferedReader.readLine())!=null){
			if(Integer.parseInt(str.split(" ")[3])<=10){
				bioasqSnippet(str,outsnippet); 
			}				
		}
		bufferedReader.close();
	} 
	/**拆分句子，组成源文件*/
	public void bioasqSnippet(String str,String outsnippet) throws Exception {
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		db = dbf.newDocumentBuilder();
		String[] fileName = str.split(" ");
		Document dt = db.parse(new InputSource(new InputStreamReader(new FileInputStream(fileName[2]),"UTF-8")));
		String pmid=fileName[2].substring(fileName[2].lastIndexOf("/")+1,fileName[2].length()-4);
		for(int i=0;i<lable.length;i++){
			NodeList list = dt.getElementsByTagName(lable[i]);
			if(list.getLength()>0){
			//String fp = "d:/space/snippet2";
			String filePath =outsnippet + fileName[0];
			File fp = new File(filePath);  
		    if (!fp.exists()) {  
		            fp.mkdir();
		    }  
			NodeList nodeList = dt.getElementsByTagName("MedlineCitation");
			String content = list.item(0).getTextContent();
			/**转义字符*/
			if(content.length()>0){
				if(content.contains("&amp;")||content.contains("&lt;")||content.contains("&gt;")||content.contains("&apos;")||content.contains("&quot;")){
					 content = content.replaceAll("&amp;","&");
					 content = content.replaceAll("&lt;","<");
					 content = content.replaceAll("&gt;",">");
					 content = content.replaceAll("&apos;","'");
					 content = content.replaceAll("&quot;","\"");	
				}
				/**两种方案*/
				String[] snippet = null;
				if(lable[i]=="ArticleTitle"){
					snippet = (content + "##").split("##");
				} else {
					snippet = content.split("\\. |\\.\n");
				}
				
				/**两个句号的情况
				int start2 = 0, end2 = 0;
				for(int m=0;m<snippet.length-1;m++){
					end2 = start2 + snippet[m].length() + snippet[m + 1].length() + 3;
					String doubleSnippet = snippet[m] + ". " + snippet[m + 1] + ".";
					FileWriter fileWriter = new FileWriter(fp  + "/"+ nodeList.item(0).getChildNodes().item(1).getTextContent() + "-" + i + "-" + start2 +"-" + end2 + ".xml");
					fileWriter.write(doubleSnippet);
					fileWriter.close();
					start2 = end2 - snippet[m + 1].length()-1;
				}
				*/
				/**单个句号的情况*/
				
				int start = 0, end = 0;
				for(int j=0;j<snippet.length;j++){
					if(snippet[j]!=""){
						if(lable[i]=="ArticleTitle"){
							end = start + snippet[j].trim().length() + 1;
							FileWriter fileWriter = new FileWriter(fp  + "/"+ pmid + "-" + i + "-" + start +"-" + end + ".xml");
							fileWriter.write(snippet[j].trim());
							fileWriter.close();
							start = end + 1;
						} else {
							end = start + snippet[j].length() + 1;
							FileWriter fileWriter = new FileWriter(fp  + "/"+ pmid + "-" + i + "-" + start +"-" + end + ".xml");
							fileWriter.write(snippet[j].trim() + ".");
							fileWriter.close();
							start = end + 1;
						}
						
					}
				}
			}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		String bestResult="bioasq5b_batch5/doc_queries/nnMesh/bioasq5b_batch5_NNsdmfsdmw3Mesh_expansionw3mesh.result";
		String outsnippet="bioasq5b_batch5/snippet_doc/";
		BioasqSnippet xmlData = new BioasqSnippet();
		xmlData.readXMLFile(bestResult,outsnippet);
		System.out.println("end");
	}

}
