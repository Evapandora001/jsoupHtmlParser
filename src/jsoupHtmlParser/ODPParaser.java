/**  
 * @Title: ODPParaser.java
 * @Prject: jsoupHtmlParser
 * @Package: jsoupHtmlParser
 * @Description: TODO
 * @author: Evapandora  
 * @date: 2015年12月8日 下午5:32:33
 */
package jsoupHtmlParser;

import java.io.IOException;

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

	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("https://www.dmoz.org/search?q=http://finance.sina.com.cn").get();
		Elements newsHeadlines = doc.select("#bd-cross ol li a strong");
		System.out.println(newsHeadlines.toString());
		
		

	}

}
