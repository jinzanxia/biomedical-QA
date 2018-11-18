package utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Mod {
	public void mod() throws Exception {
		BufferedReader bufferedReaderVec = new BufferedReader(new FileReader("/data/EveryOne/zll/bioasq4/word2vecTools/vectors.txt"));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/data/EveryOne/zll/bioasq4/w2v_syn/mod.txt",true));
		String str1 = "";
		while((str1=bufferedReaderVec.readLine())!=null){
			String[] d1 = str1.trim().split(" ");
			double sq1 = 0;
			for (int i=0;i<d1.length;i++){
				sq1 += Double.parseDouble(d1[i]) * Double.parseDouble(d1[i]);
			}
			bufferedWriter.write(Double.toString(Math.sqrt(sq1)));
			bufferedWriter.newLine();
		}
		bufferedReaderVec.close();
		bufferedWriter.close();
	}

	public static void main(String[] args) throws Exception {
		Mod mod = new Mod();
		mod.mod();
	}

}
