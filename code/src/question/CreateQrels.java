package question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class CreateQrels {
	public void answer2qrels() throws IOException{
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("qrels/phaseB_5b_01.qrels"));
		BufferedReader bufferedReader = new BufferedReader(new FileReader("qrels/BioASQ-task5bPhaseB-testset1"));
		String jsonStr = "";
		String question = "";
		while((question=bufferedReader.readLine())!=null){
			jsonStr = jsonStr + question;
		}
		bufferedReader.close();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObject.getJSONArray("questions");
		int num=0;
		for(int i=0;i<jsonArray.size();i++){
			num++;
			int score=0;
			JSONArray documents =jsonArray.getJSONObject(i).getJSONArray("documents");
			for(int j=0;j<documents.size();j++){
				String doc=documents.getString(j);
				score++;
				bufferedWriter.write(num+" 0 "+doc.substring(doc.lastIndexOf("/")+1)+"  "+score+ "\n");
			}				
		}
		bufferedWriter.close();
	}
	public static void main(String[] args) throws Exception {
		CreateQrels qrels=new CreateQrels();
		qrels.answer2qrels();
	}
}
