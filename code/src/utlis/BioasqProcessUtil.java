package utlis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BioasqProcessUtil {
	static Set<String> set = new HashSet<String>();
	 /**
	 * 存储停用词
	 */
	public void storeStopWords(){
		String stopWord = "";
		try {
			BufferedReader inputStopWord = new BufferedReader(new FileReader("lib/Inquery stopwords.txt"));
//			BufferedReader inputStopWord = new BufferedReader(new FileReader("lib/NewInquerystopwords2.txt"));
			while((stopWord = inputStopWord.readLine())!=null){
				set.add(stopWord);
			}
			inputStopWord.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 格式化单词
	 * @param str
	 */
	public String format(String str) {
		String newString = "";
		if(str.length()>0){
			for(int i=0;i<str.length();i++){
				if(Character.isLetterOrDigit(str.charAt(i))){
					newString = newString + str.charAt(i);
				} else {
					newString = newString + " ";
				}
			}
		}
		return newString;
	}
	/**
	 * 去除特殊字符
	 * @param str
	 */
	public String filtersymbol(String str){
		String newString = "";
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？,，.]"; 
		Pattern p = Pattern.compile(regEx); 
		if(p.matcher(str) != null){
			Matcher m = p.matcher(str);
			newString=m.replaceAll("").trim();							
		}
		return newString;	
	}
	/**
	 * 过滤掉多余空格
	 */
	public String filterSpace(String str){
		str = str.replaceAll("\\s+", " ");
		return str;
	}
	/**
	 *去除停用词
	 */
	public String stopwords(String str){
		String result = "";
		String[] strArray = str.toLowerCase().split(" ");
		if(strArray.length>0){
			for(int i=0;i<strArray.length;i++){
				if(!set.contains(strArray[i])){
					result = result + strArray[i] + " ";
				}
			}
		}
		return result;
	}
	/**
	 * 词干化
	 * @param str
	 * @return
	 */
	public String filterXml(String str){
		String finalStr = "";
		if(str.length()>0){
			char[] word = new char[501];
			int j = 0;
			int ch = 0;
		    Stemmer stemmer = new Stemmer();
		    for(int i=0;i<str.length();i++){
		    	if(Character.isLetterOrDigit(str.charAt(i))){
	               ch = Character.toLowerCase(str.charAt(i));
	               word[j] = (char)ch;
	               if (j < 500){
	            	   j++;
	               } 
		    	} 
		    	if(!Character.isLetterOrDigit(str.charAt(i))){
		    		for (int c = 0; c < j; c++) {
		    			stemmer.add(word[c]);
		    		}
		    		stemmer.stem();
		    		finalStr = finalStr + stemmer.toString() + " ";
		    		j=0;
		    		word = new char[501];
		    	}
		    }
		}
	    return finalStr;
	}
	
	
}
