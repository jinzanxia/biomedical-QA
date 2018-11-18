package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BioasqSnippetUtils {
	static int flag = 1;
	public void bioasqSnippetUtils() throws Exception {
		/**输出做准备*/
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/data/EveryOne/zll/bioasq4/dl_index_snippet.txt"));
		BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("/data/EveryOne/zll/bioasq4/dl_index_snippet_info.txt"));
		/**官方答案的snippet*/
		String jsonStr = "";
		String question = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/BioASQ-trainingDataset4b.json"));
		while((question=bufferedReader.readLine())!=null){
			jsonStr = jsonStr + question;
			System.out.println(flag++);
		}
		bufferedReader.close();
		/**拆分检索出的snippet*/
		String jsonStr1 = "";
		String question1 = "";
		BufferedReader bufferedReader1 = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/dl_snippet.json"));
		while((question1=bufferedReader1.readLine())!=null){
			jsonStr1 = jsonStr1 + question1;
			System.out.println(flag++);
		}
		bufferedReader1.close();
		/**解析json*/
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObject.getJSONArray("questions");
		JSONObject jsonObject1 = JSONObject.fromObject(jsonStr1);
		JSONArray jsonArray1 = jsonObject1.getJSONArray("questions");
		for(int i=0;i<100;i++){
			Set<String> set = new HashSet<String>();
			JSONArray snippets_array = jsonArray.getJSONObject(i).getJSONArray("snippets");
			for(int k=0;k<snippets_array.size();k++){
				set.add(snippets_array.getJSONObject(k).getString("text").trim());
			}
			JSONArray snippets_array1 = jsonArray1.getJSONObject(i).getJSONArray("snippets");
			for(int n=0;n<snippets_array1.size();n++){
				if(!set.contains(snippets_array1.getJSONObject(n).getString("text").trim())){
					int int_section = 0;
					String snippets_document = snippets_array1.getJSONObject(n).getString("document");
					String snippets_text = snippets_array1.getJSONObject(n).getString("text");
					String snippets_section = snippets_array1.getJSONObject(n).getString("beginSection");
					String snippets_offsetInBeginSection = snippets_array1.getJSONObject(n).getString("offsetInBeginSection");
					String snippets_offsetInEndSection = snippets_array1.getJSONObject(n).getString("offsetInEndSection");
					if(snippets_section.equals("abstract")){
						int_section = 1;
					} else {
						int_section = 0;
					}
					String str_question = jsonArray.getJSONObject(i).getString("body");
					String str_id = jsonArray.getJSONObject(i).getString("id");
					String string = str_question.trim() + " " + snippets_text.trim();
					String newString = "";
					for(int m=0;m<string.length();m++){
						if(string.charAt(m)==','||string.charAt(m)==';'||string.charAt(m)=='?'||string.charAt(m)=='!'||string.charAt(m)==':'){
							newString = newString + " " + string.charAt(m) + " ";
						} else if((m==string.length()-1)&&(string.charAt(m)=='.')){
							newString = newString + " " + string.charAt(m);
						} else if((m<string.length()-1)&&(string.charAt(m)=='.')&&(string.charAt(m+1)==' ')){
							newString = newString + " " + string.charAt(m);
						} else if(string.charAt(m)=='('||string.charAt(m)=='"'){
							newString = newString + " " + string.charAt(m) + " ";
						} else if(string.charAt(m)==')'||string.charAt(m)=='"'){
							newString = newString + " " + string.charAt(m) + " ";
						} else {
							newString = newString + string.charAt(m);
						}
					}
					newString = newString.replaceAll("\\s+", " ");
					bufferedWriter.write(newString);
					bufferedWriter.newLine();
					bufferedWriter1.write(str_id + "#" + snippets_document.split("/")[snippets_document.split("/").length-1] + "" +
							"-" + int_section + "-" + snippets_offsetInBeginSection + "-" + snippets_offsetInEndSection);
					bufferedWriter1.newLine();
				}
			}
		}
		bufferedWriter.close();
		bufferedWriter1.close();
	}
	
	public static void main(String[] args) throws Exception {
		BioasqSnippetUtils bioasqSnippetUtils = new BioasqSnippetUtils();
		bioasqSnippetUtils.bioasqSnippetUtils();
	}

}
