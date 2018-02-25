package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Bot extends Thread {
    static double alpha=0.85;// at each iteration the bot will jump to a random page with a probability of 1-alpha

//    static String baseUrl="http://www.polytechnique.edu/";
//    static String regex="http.+polytechnique.*";

//    static String baseUrl="http://www.lemonde.fr/";
//    static String regex="http.+lemonde.*";

//    static String baseUrl="https://fr.wikipedia.org/wiki/Wikip%C3%A9dia:Accueil_principal";
//    static String regex="http.+wikipedia.*";

    static String baseUrl="https://www.youtube.com/";
    static String regex="http.+youtube.*";

//    static String baseUrl="https://docs.oracle.com/javase/8/docs/api/overview-summary.html";
//    static String regex="http.+docs.oracle.com.*";

//    static String baseUrl="http://mythicspoiler.com/";
//    static String regex="http.+mythic.*";

	Concurrent_WebGraph web;
    Document doc;
    LinkedList<String> pageLinks = new LinkedList<>();

    public Bot(Concurrent_WebGraph g) {
    	web =g;
    }

    @Override
    public void run() {
    	String currentPage=baseUrl;
    	String nextPage;
    	
        for (int i = 0; i < 300; i++) {
        	
            nextPage=this.explore(currentPage);
            web.visit(currentPage, nextPage);
            currentPage=nextPage;
        }
    }

    String explore(String url) {
    	String nextUrl="";
        pageLinks = new LinkedList<>();

//        System.out.println(this.getName() + "\nExploring : "+url);

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
                if (Pattern.matches(regex, l)) {
                    pageLinks.add(l);
                }
            }

            int index = ThreadLocalRandom.current().nextInt(-1, pageLinks.size());

            if (index==-1){
                nextUrl=web.getRandomUrl();
            }
            else {
                int i = 0;

                for (String link : pageLinks) {
//                pages.add(link);
                    if (i == index) {
                        nextUrl= link;
                    }
                    i++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return web.getRandomUrl();
        }
        return nextUrl;
    }
}