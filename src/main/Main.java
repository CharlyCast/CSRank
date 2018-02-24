package main;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main {

    public static void main(String[] args) throws InterruptedException {
    	Graphe web=new Graphe();
        Bot bot = new Bot(web);

        bot.start();
        bot.join();
        for (Page p : web.get_vertices()) {
        	System.out.println(p.get_url());
        	System.out.println(p.get_nbVisits());
        }
    }

}
