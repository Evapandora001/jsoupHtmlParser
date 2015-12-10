/**  
 * @Title: ODPParaser.java
 * @Prject: jsoupHtmlParser
 * @Package: jsoupHtmlParser
 * @Description: TODO
 * @author: Evapandora  
 * @date: 2015年12月8日 下午5:32:33
 */
package jsoupHtmlParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * @ClassName: ODPParaser
 * @Description: TODO
 * @author: Evapandora
 * @date: 2015年12月8日 下午5:32:33
 */
public class ODPParaser {
	private Connection conn;
	private HashMap<String,Elements> urls=new HashMap<String,Elements>();
	public ODPParaser(){
//		conn=SQL.getConn();
	}
	public void finished(){
		try {
			conn.close();//关闭数据库连接
		} catch (SQLException e) {
			System.err.println("无法关闭连接");
			e.printStackTrace();
		}
	}
	
	public void start(){
		conn=SQL.getConn();
		for(int i=0;i<=108233;i+=1000){
			String selectSql="SELECT DISTINCT urllong"
					+" FROM final_s_t_l2"
					+" ORDER BY urllong"
					+ " limit "+i+",1000;";
			System.out.println("############"+i+"###########");
			selectData(selectSql);
			}
		finished();
	}
	
	private void selectData(String sql) {
		try {
			String unavailableURL="";
			String update="";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String url=urlProcess(rs.getString(1));
				if(url==null||url.length()==0){
					unavailableURL+=rs.getString(1)+"\r\n";
					continue;
				}
				Elements type= null;
				if(urls.containsKey(url)){
					type=urls.get(url);
				}else{
					System.out.println(url);
					Document  doc = Jsoup.connect("https://www.dmoz.org/search?q="+url).timeout(100*1000).get();
					type= doc.select("#bd-cross ol li a strong");
					urls.put(url, type);
				}
				if(type.toArray().length==0){
					unavailableURL+=rs.getString(1)+"\r\n";
					continue;
				}
				String out="";
				for(Object ele:type.toArray()){
					out+=ele.toString().substring(8,ele.toString().length()-9)+"#";
				}
				out=out.substring(0,out.length()-1);
				
				update+="UPDATE final_s_t_l2 tb1 SET tb1.typeOfODP=\""+out+"\" WHERE tb1.urllong=\""+rs.getString(1)+"\";\r\n";
				
			}
			writeAppend(unavailableURL,"out\\unavailableURL.txt");
			writeAppend(update,"out\\updateURL.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Title: urlProcess
	 * @Description: get host of URL
	 * @param string
	 * @return
	 * @return: String
	 */
	private String urlProcess(String url) {
		int start=0,end=0;
		if(url.contains(".com")){
			end=url.indexOf(".com");
			start=end-1;
			if(end+4<=url.length()){
				end=end+4;
			}else{
				end=url.length();
			}
			while(start>=0&&url.charAt(start)!='.') start--;
			start=start+1;
		}else{
			if(url.contains(".cn")){
				end=url.indexOf(".cn");
				start=end-1;
				if(end+3<=url.length()){
					end=end+3;
				}else{
					end=url.length();
				}
				while(start>=0&&url.charAt(start)!='.') start--;
				start=start+1;
			}else{
				if(url.contains("http://")){
					start=url.indexOf("http://");
					end=start+7;
					while(end<=url.length()-1&&url.charAt(end)!='/') end++;
				}else{
					return null;
				}
			}
		}
		
		url=url.substring(start,end);
		return url;
	}
	public static void main(String[] args){
//		Document doc = Jsoup.connect("https://www.dmoz.org/search?q=finance.sina.com.cn").get();
//		Elements newsHeadlines = doc.select("#bd-cross ol li a strong");
//		System.out.println(newsHeadlines.toArray().length);//null flag
//		System.out.println(newsHeadlines.text().replace(' ','#'));
//		System.out.println();
//		System.out.println(newsHeadlines.toString());
//		System.out.println();
//		for(Object ele:newsHeadlines.toArray())
//			System.out.println(ele.toString().substring(8,ele.toString().length()-9));
//		(new ODPParaser()).getHost("out//unavailableURL.txt");;
		(new ODPParaser()).getHighFreWordsInHost("out//UnavailableURLHost-noRepeated");
	}
	
	public void writeAppend(String content,String filePath){
		try {
		        FileOutputStream fos = new FileOutputStream(filePath,true);//追加方式
		        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");//指明写文件的编码格式
		        osw.write(content);
		        osw.flush();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	public void getHost(String urlPath){
		urls.clear();
		String out="";
		String outPath="out//UnavailableURLHost-noRepeated";
		int count=0;
		try {
		     FileInputStream fis = new FileInputStream(urlPath);
		     InputStreamReader isr = new InputStreamReader(fis,"utf-8");
		     BufferedReader br = new BufferedReader(isr);
		     String line = null;
		     while ((line = br.readLine()) != null) {
		    	 if(line.length()==0) continue;
		    	 line=urlHost(line);
		    	 if(urls.containsKey(line)){
		    		 continue;
		    	 }
		    	 urls.put(line,null);
		    	 out+=line+"\r\n";
		    	 count++;
		    	 if(count==5000){
		    		 writeAppend(out,outPath);
		    		 count=0;
		    		 out="";
		    	 }
		        }
		     writeAppend(out,outPath);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }			
	}
	/**
	 * @Title: urlHost
	 * @Description:  提取url host，用于分类 
	 * @param url
	 * @return: String
	 */
	private String urlHost(String url) {
		String newUrl="";
		if(url.contains("?")){
			url=url.split("\\?")[0];
		}
		if(url.contains("://")){
			newUrl=url.split("://",2)[0]+"://";
			url=url.split("://",2)[1];
			int end=0;
			if((end=url.indexOf("/"))==-1){
				newUrl+=url;
			}else{
				newUrl+=url.substring(0,end);
				if(end+1<url.length()){
					url=url.substring(end+1);
					for(String ele:url.split("/")){
						if(ele.matches("[a-zA-Z]*")){
							newUrl+="/"+ele;
						}else{
							return newUrl;
						}
					}
				}
			}
		}else{
			return url;
		}
		return newUrl;
	}
	public void getHighFreWordsInHost(String urlPath){
		String out="";
		String outPath="out//highFreWordsInHost";
		TreeMap<String,Integer> words=new TreeMap<String,Integer>();
		try {
		     FileInputStream fis = new FileInputStream(urlPath);
		     InputStreamReader isr = new InputStreamReader(fis,"utf-8");
		     BufferedReader br = new BufferedReader(isr);
		     String line = null;
		     while ((line = br.readLine()) != null) {
		    	 if(line.contains("://")){
		    		 line=line.split("://",2)[1];
		    	 }
		    	 for(String ele:line.split("/")){// \W非单词字符
		    		 ele=ele.trim();
		    		 if(ele.length()<=1){
		    			 continue;
		    		 }
		    		 if(words.containsKey(ele)){
		    			 words.put(ele,words.get(ele)+1);
		    		 }else{
		    			 words.put(ele, 1);
		    		 }
		    	 }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		System.out.println("TreeMap.size()="+words.size());
		
		ArrayList<Map.Entry<String,Integer>> list=new ArrayList<Map.Entry<String,Integer>>(words.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
			//降序排序
			public int compare(Entry<String,Integer> o1,Entry<String,Integer> o2){
				if(o1.getValue()==o2.getValue())	return 0;
				if(o1.getValue()>o2.getValue()){
					return -1;
				}else{
					return 1;// 01 排在 o2 前面
				}
			}
		});

		Iterator<Map.Entry<String,Integer>> it=list.iterator();
		while(it.hasNext()){
			Map.Entry<String,Integer> ele=it.next();
			out+=ele.getKey()+"\t:\t"+ele.getValue()+"\r\n";
		}
		writeAppend(out,outPath);
	}
	
}
