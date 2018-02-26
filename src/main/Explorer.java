package main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Explorer extends Thread{
	Concurrent_WebGraph web;
	Concurrent_FIFO_Queue queue;
	Page currentPage;
	Document doc;
	String regex;
	
	public Explorer(Concurrent_WebGraph web, Concurrent_FIFO_Queue queue, String regex) {
		this.web=web;
		this.queue=queue;
		this.regex=regex;
	}
	public void run(){
		while(!queue.isEmpty()) {
			currentPage=queue.poll();
			if (currentPage!=null) {
				explore(currentPage);
			}
			
		}
	}
	public void explore(Page currentPage) {
		try {
			doc = Jsoup.connect(currentPage.get_url()).get();
			String title = doc.title();
			currentPage.setTitle(title);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 
	     Elements links = doc.select("a[href]");

	     String l;
	     for (Element link : links) {
	         l = link.attr("href");
	         
	         if (Pattern.matches(regex, l)) {
	        	 Page p=new Page(l);
	             queue.add(p); // p will actually be added to queue only if it had never been visited
	             currentPage.add_neighbor(p);
	             web.addPage(p);
	         }
	         
	     }
	}
}
