package question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class BioasqQuestion {
	static int flag = 0;
	/**
	 * 取出Bioasq中的问题
	 * @throws Exception
	 */
	public void getBioasqQuestion(String questionjson,String out) throws Exception {
		String jsonStr = "";
		String question = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(questionjson));
		while((question=bufferedReader.readLine())!=null){
			System.out.println(flag++);
			jsonStr = jsonStr + question;
		}
		bufferedReader.close();

		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObject.getJSONArray("questions");
		for(int i=0;i<jsonArray.size();i++){
			System.out.println(i);
			String str_question = jsonArray.getJSONObject(i).getString("body");
			System.out.println(str_question);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out + (i+1) +".txt"));
			bufferedWriter.write(str_question);
			bufferedWriter.flush();
			bufferedWriter.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		BioasqQuestion bioasqQuestion = new BioasqQuestion();
		String questionjson="bioasq5b_batch5/BioASQ-task5bPhaseA-testset5";
		String out="bioasq5b_batch5/questions/";
		bioasqQuestion.getBioasqQuestion(questionjson,out);
		System.out.println("end");
	}

}
