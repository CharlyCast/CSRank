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
        Bot[] bot = new Bot[8];
        for (int i=0;i<8;i++) {
        	bot[i]=new Bot(web);
        	bot[i].start();
        }
        for (int i=0;i<8;i++) {
        	bot[i].join();
        }
        for (Page p : web.get_vertices()) {
        	System.out.println(p.get_url());
        	System.out.println(p.get_nbVisits());
        }
    }

}
