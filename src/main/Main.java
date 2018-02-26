package main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

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
    static int nbBot = 4;
    static int nbExplorationsPerBot=10;
    static String baseUrl="http://mythicspoiler.com/";
    static String regex="http.+mythic.*";

    public static void main(String[] args) throws InterruptedException {
        Concurrent_WebGraph web = new Concurrent_WebGraph();
        Concurrent_FIFO_Queue queue= new Concurrent_FIFO_Queue(baseUrl);
        /*Bot[] bot = new Bot[nbBot];
        for (int i = 0; i < nbBot; i++) {
            bot[i] = new Bot(web);
            bot[i].start();
        }

        Displayer disp = new Displayer(web);
        disp.start();
        for (int i = 0; i < nbBot; i++) {
            bot[i].join();
        }*/
        
        //Exploration du graphe
        ExplorationManager em=new ExplorationManager(queue,web,regex);
        em.startExploration(nbBot, 3);
        
        Displayer disp = new Displayer(web);
        disp.start();
        
        // Détermination du PageRank.
        RandomWalker[] walkers = new RandomWalker[nbBot];
        for (int i = 0; i < nbBot; i++) {
            walkers[i] = new RandomWalker(web,nbExplorationsPerBot*5);
            walkers[i].start();
        }

        for (int i = 0; i < nbBot; i++) {
            walkers[i].join();
        }
        
        

        System.out.println("Done");

    }

}
