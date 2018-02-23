package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Bot extends Thread {
    Document doc;
    HashSet<String> pages = new HashSet<>();
    LinkedList<String> pageLinks = new LinkedList<>();

    public Bot() {

    }

    @Override
    public void run() {
        String nextPage = "http://www.polytechnique.edu/";
        for (int i = 0; i < 10; i++) {
            nextPage=this.explore(nextPage);
        }
    }

    String explore(String url) {
        String nextPage = "";

        try {
            System.out.println("URL : " + url);
            doc = Jsoup.connect(url).get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

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

            System.out.println(nbOfLinks + " valid links");

            int index = ThreadLocalRandom.current().nextInt(0, pageLinks.size());
            int i = 0;

            for (String link : pageLinks) {
                pages.add(link);
                if (i == index) {
                    nextPage = link;
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextPage;
    }
}