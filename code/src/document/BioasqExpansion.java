package step2Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class BioasqExpansion {
	/**expansion*/
	public void bioasqExpansion(String bestResult,String outexpansion,int expansionNum) throws Exception {
		String queryData = "";
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(bestResult));
			while((queryData=bufferedReader.readLine())!=null){
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outexpansion+ queryData.split(" ")[0] + ".txt",true));
				if(Integer.parseInt(queryData.split(" ")[3])<=expansionNum){
					bufferedWriter.write(readXmlFileTitle(queryData.split(" ")[2].trim()) + " ");
				}	
				bufferedWriter.close();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  readXmlFileTitle(String path) throws Exception {
		String str = "";
		String strContent = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		while((str=bufferedReader.readLine())!=null){
			/*提取title
			if(str.trim().startsWith("<ArticleTitle>")&&str.trim().endsWith("</ArticleTitle>")){
				strContent = str.substring(14, str.length()-15);
			}*/
			/*提取mesh,原来没有*/
			if(str.trim().startsWith("<DescriptorName>")&&str.trim().endsWith("</DescriptorName>")){
				strContent = str.substring(16, str.length()-17);
			}

		}
		bufferedReader.close();
		return strContent;
	}
	
	public static void main(String[] args) throws Exception {
		//注意提取的是title还是mesh，挑选上面的代码
		String bestResult="bioasq5b_batch5/doc_queries/nnMesh/bioasq5b_batch5_NNsdmfsdmw3Mesh.result";
		String outexpansion="bioasq5b_batch5/doc_expansion/nnMesh/" ;		//注意这里输出是叠加不是覆盖，所以记得删除原来的
		int expansionNum=3; //规定提取几个文件内容
		BioasqExpansion bioasqExpansion = new BioasqExpansion();
		bioasqExpansion.bioasqExpansion(bestResult,outexpansion,expansionNum);
		System.out.println("end");
	}

}
