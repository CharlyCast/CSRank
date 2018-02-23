package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Bot extends Thread {
    Document doc;
    HashSet<String> pages = new HashSet<>();

    public Bot() {

    }

    @Override
    public void run() {
        this.explore("http://www.polytechnique.edu/");
    }

    void explore(String url) {
        try {
            doc = Jsoup.connect(url).get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            // get all links
            Elements links = doc.select("a[href]");

            int c = 0;

            String l;

            for (Element link : links) {

                // get the value from href attribute
//                System.out.println("\nlink : " + link.attr("href"));
//                System.out.println("text : " + link.text());

                l = link.attr("href");
                if (Pattern.matches("http.+polytechnique.*", l)) {
                    pages.add(l);
                    System.out.println("Matches");
                    c++;
                }
            }

            System.out.println(c + " valid links");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}