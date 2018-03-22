package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static int nbBot = 4;
    static int nbExplorationsPerBot = 100000;
    static int nbCores = Runtime.getRuntime().availableProcessors();
//    static String baseUrl="http://mythicspoiler.com/";
//    static String regex="http.+mythic.*";

//    static String baseUrl = "http://www.polytechnique.edu/";
//    static String regex = "http.+polytechnique.*";

//    static String baseUrl="http://www.centralesupelec.fr/";
//    static String regex = "http.+centralesupelec.*";

    static String baseUrl="https://www.insa-lyon.fr";
    static String regex = "http.+insa-lyon.*";


    public static void main(String[] args) throws InterruptedException {

        firstAlgorithm();
        //secondAlgorithm();

    }
    
    public static void firstAlgorithm() throws InterruptedException{
    	Concurrent_WebGraph web = new Concurrent_WebGraph();
        Bot[] bot = new Bot[nbBot];
        for (int i = 0; i < nbBot; i++) {
            bot[i] = new Bot(web,baseUrl,regex);
            bot[i].start();
        }

        Displayer disp = new Displayer(web,1);
        disp.start();
        for (int i = 0; i < nbBot; i++) {
            bot[i].join();
    }
    }
    public static void secondAlgorithm() throws InterruptedException{
    	Concurrent_WebGraph web = new Concurrent_WebGraph();
    	long t = System.nanoTime();
        long tExploration;
        long tWalkers;
        long tCSComputation;

        Displayer disp = new Displayer(web,1000);
        disp.start();

        //Exploration du graphe
        tExploration=System.nanoTime();
        ExplorationManager em = new ExplorationManager(web, baseUrl, regex);
        em.startExploration(nbBot, 3);
        tExploration=(System.nanoTime()-tExploration)/ 1000000000;


        // Détermination du PageRank.
        tWalkers=System.nanoTime();
        ExecutorService exec = Executors.newFixedThreadPool(nbCores);
        int n=web.getpages().size();
        int K= (int) (Math.log((double) n)) *1000;
        for (int i=0;i<K;i++) {
        	for (int j=0;j<n;j++) {
        		exec.execute(new RandomWalker(web,web.getpages().get(j)));
        	}
        }
        exec.shutdown();
        exec.awaitTermination(10000, TimeUnit.MILLISECONDS);
        tWalkers=(System.nanoTime()-tWalkers)/ 1000000000;
        


        //Computing CSRank
        tCSComputation=System.nanoTime();
        web.computeCSRank();
        tCSComputation=(System.nanoTime()-tCSComputation)/ 1000000000;

        t = (System.nanoTime() - t) / 1000000000;

        System.out.println("Done in " + t + " s"
        +"\nSpending:\n"
        +tExploration+" s in exploration\n"
        +tWalkers+" s in random walks\n"
        +tCSComputation+" s in CS Rank computation\n");
    }

}