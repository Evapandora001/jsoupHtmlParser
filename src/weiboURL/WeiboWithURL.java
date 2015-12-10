/**  
 * @Title: WeiboWithURL.java
 * @Prject: jsoupHtmlParser
 * @Package: weiboURL
 * @Description: TODO
 * @author: Evapandora  
 * @date: 2015年12月10日 下午7:01:25
 */
package weiboURL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @ClassName: WeiboWithURL
 * @Description: TODO
 * @author: Evapandora
 * @date: 2015年12月10日 下午7:01:25
 */
public class WeiboWithURL {

	public void getURL(String urlPath){
		String out="";
	//	String outPath="//FC1//weiboWithURL//";
		String outPath="out//";
		int count=0;
		File[] files=new File(urlPath).listFiles();
		for(File file:files){
			if(file.getName().endsWith(".dat")==false) continue;
			System.out.println(file.getName());
			try {
			     FileInputStream fis = new FileInputStream(file);
			     InputStreamReader isr = new InputStreamReader(fis,"utf-8");
			     BufferedReader br = new BufferedReader(isr);
			     String line = null;
			     while ((line = br.readLine()) != null) {
			    	 if(line.split(",")[2].contains("http://t.cn/")==false) continue;
			    	 out+=line+"\r\n";
			    	 count++;
			    	 if(count==10000){
			    		 writeAppend(out,outPath+file.getName());
			    		 count=0;
			    		 out="";
			    	 }
			        }
			     writeAppend(out,outPath+file.getName());
			    } catch (Exception e) {
			        e.printStackTrace();
			    }		
			
		}
	}

	public static void main(String[] args) {
		WeiboWithURL a=new WeiboWithURL();
	//	a.getURL("//FC1//weibo");
//		a.getURL("C://Users//lenovo//Desktop//ApacheSpark");
		a.getURLThread("//FC1//weibo");

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
	
	public void getURLThread(String urlPath){
		File[] files=new File(urlPath).listFiles();
		for(File file:files){
			if(file.getName().endsWith(".dat")==false) continue;
			System.out.println(file.getName());
			(new ReadFileThread(file)).start();
		}
		
	}
	
	class ReadFileThread extends Thread{
		private File file;
		
		/**
		 * @return the file
		 */
		public File getFile() {
			return file;
		}

		/**
		 * @param file the file to set
		 */
		public void setFile(File file) {
			this.file = file;
		}

		/**
		 * @Title:WeiboWithURL.ReadFileThread
		 * @Description:TODO
		 */
		public ReadFileThread(File file) {
			this.setFile(file);
		}
		
		public void run(){
			String out="";
			String outPath="//FC1//weiboWithURL//";
			int count=0;
			try {
			     FileInputStream fis = new FileInputStream(getFile());
			     InputStreamReader isr = new InputStreamReader(fis,"utf-8");
			     BufferedReader br = new BufferedReader(isr);
			     String line = null;
			     while ((line = br.readLine()) != null) {
			    	 if(line.split(",")[2].contains("http://t.cn/")==false) continue;
			    	 out+=line+"\r\n";
			    	 count++;
			    	 if(count==10000){
			    		 writeAppend(out,outPath+getFile().getName());
			    		 count=0;
			    		 out="";
			    	 }
			        }
			     writeAppend(out,outPath+getFile().getName());
			    } catch (Exception e) {
			        e.printStackTrace();
			    }		
		}
	}
	
}


