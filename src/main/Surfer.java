package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Surfer extends Thread {
    static double alpha = 0.85;// at each iteration the bot will jump to a random page with a probability of 1-alpha

    String baseUrl;
    String regex;
    int nbSteps;
    Concurrent_WebGraph web;
    Document doc;
    LinkedList<String> pageLinks = new LinkedList<>();

    public Surfer(Concurrent_WebGraph g, String baseUrl, String regex, int nbSteps) {
        web = g;
        this.baseUrl=baseUrl;
        this.regex=regex;
        this.nbSteps=nbSteps;
    }

    @Override
    public void run() {
        String currentPage = baseUrl;
        String nextPage;

        for (int i = 0; i < nbSteps; i++) {

            nextPage = this.explore(currentPage);
            if (!nextPage.equals("")&& !currentPage.equals("")) {
            	web.visit(currentPage, nextPage);
            	web.incrVisits();
            }
            currentPage = nextPage;
        }
    }

    String explore(String url) {
        String nextUrl = "";
        pageLinks = new LinkedList<>();

        try {

//            If the url is empty, we return a random url. Avoid some bugs
            if (url.equals("")) return web.getRandomUrl();
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            //e.printStackTrace();
            return web.getRandomUrl();
        }

        // get page title
        String title = doc.title();
        web.getPage(url).setTitle(title);

        // get all links
        Elements links = doc.select("a[href]");

        String l;
        for (Element link : links) {
            l = link.attr("href");
            if (Pattern.matches(regex, l)) {
                pageLinks.add(l);
            }
        }

        if (pageLinks.size() == 0 || ThreadLocalRandom.current().nextDouble(0, 1) >= alpha) {
            nextUrl = web.getRandomUrl();
        } else {
            int index = ThreadLocalRandom.current().nextInt(-1, pageLinks.size());
            int i = 0;

            for (String link : pageLinks) {
                if (i == index) {
                    nextUrl = link;
                }
                i++;
            }
        }

        return nextUrl;
    }
}