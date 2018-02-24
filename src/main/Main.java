package main;

import java.awt.Color;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

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
        
        Visualization v=new Visualization(web);
        v.display();
    }

}
