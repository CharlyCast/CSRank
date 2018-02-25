package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Bot extends Thread {
	Concurrent_WebGraph web;
    Document doc;
    HashSet<String> pages = new HashSet<>();
    LinkedList<String> pageLinks = new LinkedList<>();

    public Bot(Concurrent_WebGraph g) {
    	web =g;
    }

    @Override
    public void run() {
    	String currentPage="http://www.polytechnique.edu/";
    	String nextPage;
    	
        for (int i = 0; i < 300; i++) {
        	
            nextPage=this.explore(currentPage);
            web.visit(currentPage, nextPage);
            currentPage=nextPage;
        }
    }

    String explore(String url) {
    	String nextUrl="";
        try {
            doc = Jsoup.connect(url).get();

            // get page title
            String title = doc.title();
            web.setTitle(url, title);

            // get all links
            Elements links = doc.select("a[href]");

            String l;
            for (Element link : links) {
                l = link.attr("href");
                if (Pattern.matches("http.+polytechnique.*", l)) { 
                    pageLinks.add(l);
                }
            }

            int index = ThreadLocalRandom.current().nextInt(0, pageLinks.size());
            int i = 0;
            
            for (String link : pageLinks) {
                pages.add(link);
                if (i == index) {
                    nextUrl= link;
                }
                i++;
            }
            

        } catch (IOException e) {
            e.printStackTrace();
            return "http://www.polytechnique.edu/";
        }
        return nextUrl;
    }
}