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
	Graphe web;
    Document doc;
    HashSet<String> pages = new HashSet<>();
    LinkedList<String> pageLinks = new LinkedList<>();

    public Bot(Graphe g) {
    	web =g;
    }

    @Override
    public void run() {
        //String nextPage = "http://www.polytechnique.edu/";
    	Page nextPage;
        Page currentPage=new Page("http://www.polytechnique.edu/");
        for (int i = 0; i < 100; i++) {
        	
            nextPage=this.explore(currentPage);
            currentPage.add_neighbor(nextPage);
            nextPage.visit();
            currentPage=nextPage;
        }
    }

    Page explore(Page p) {
        Page nextPage;

        try {
            //System.out.println("URL : " + p.getUrl());
            doc = Jsoup.connect(p.get_url()).get();

            // get page title
            String title = doc.title();
            p.set_title(title);
           // System.out.println("title : " + title);


            // get all links
            Elements links = doc.select("a[href]");

            int nbOfLinks = 0;

            String l;

            for (Element link : links) {

                l = link.attr("href");
                if (Pattern.matches("http.+polytechnique.*", l)) {
                    pageLinks.add(l);
//                    System.out.println("Matches");
                    nbOfLinks++;
                }
            }

          //  System.out.println(nbOfLinks + " valid links\n");

            int index = ThreadLocalRandom.current().nextInt(0, pageLinks.size());
            int i = 0;
            
            String nexturl="";
            for (String link : pageLinks) {
                pages.add(link);
                if (i == index) {
                    nexturl= link;
                }
                i++;
            }
            if (web.contains(nexturl)) {
            	nextPage=web.get_page(nexturl);
            }
            else {
            	nextPage=new Page(nexturl);
            	web.add_vertex(nextPage);
            }
            

        } catch (IOException e) {
            e.printStackTrace();
            return new Page("http://www.polytechnique.edu/");
        }
        return nextPage;
    }
}