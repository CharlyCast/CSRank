package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static int nbBot = 3;
    static int nbSteps = 4;
    static int nbExplorationsPerBot = 1000;
    static int nbCores = Runtime.getRuntime().availableProcessors();

//    static String baseUrl="http://mythicspoiler.com/";
//    static String regex="http.+mythic.*";

  //  static String baseUrl = "https://www.polytechnique.edu/";
 //   static String regex = "https://www.polytechnique.edu/.*";

//    static String baseUrl="http://www.centralesupelec.fr/";
//    static String regex = "http.+centralesupelec.*";

//    static String baseUrl="https://www.insa-lyon.fr";
//    static String regex = "https://www.insa-lyon.fr.*";

 //   static String baseUrl = "http://www.enseignement.polytechnique.fr/informatique/";
//    static String regex = "http://www.enseignement.polytechnique.fr/informatique/.*";
    
//    static String baseUrl = "https://www.amazon.fr";
 //   static String regex = "https://www.amazon.fr.*";
    
    static String baseUrl = "http://www.lemonde.fr";
    static String regex = "http://www.lemonde.fr.*";

    public static void main(String[] args) throws InterruptedException {

        //firstAlgorithm();
        secondAlgorithm();

    }

    public static void firstAlgorithm() throws InterruptedException {
        long t = System.nanoTime();
        Concurrent_WebGraph web = new Concurrent_WebGraph();
        Surfer[] surfers = new Surfer[nbBot];
        for (int i = 0; i < nbBot; i++) {
            surfers[i] = new Surfer(web, baseUrl, regex, nbExplorationsPerBot);
            surfers[i].start();
        }

        Displayer disp = new Displayer(web, 1000,true);
        disp.start();
        for (int i = 0; i < nbBot; i++) {
            surfers[i].join();
        }
        t = (System.nanoTime() - t) / 1000000000;

        web.computePageRank();

        System.out.println("Explored " + web.getNbVisitsTotal() + " pages in " + t + " seconds.");
        System.out.println("Speed: " + web.getNbVisitsTotal() / t + "pages/s.");

    }

    public static void secondAlgorithm() throws InterruptedException {
        Concurrent_WebGraph web = new Concurrent_WebGraph();
        long t = System.nanoTime();
        long tExploration;
        long tWalkers;
        long tPRComputation;


        //Exploration du graphe
        tExploration = System.nanoTime();
        ExplorationManager em = new ExplorationManager(web, baseUrl, regex);
        em.startExploration(nbBot, nbSteps);
        tExploration = (System.nanoTime() - tExploration) / 10000000;


        // Détermination du PageRank.
        System.out.println("Starting the random walks to estimate the PageRank");
        tWalkers = System.nanoTime();
        ExecutorService exec = Executors.newFixedThreadPool(nbCores);
        int n = web.getpages().size();
        int K = (int) (Math.log((double) n)) * 1000;
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < n; j++) {
                exec.execute(new RandomWalker(web, web.getpages().get(j)));
            }
        }
        exec.shutdown();
        exec.awaitTermination(10000, TimeUnit.MILLISECONDS);
        tWalkers = (System.nanoTime() - tWalkers) / 10000000;


        //Computing CSRank
        tPRComputation = System.nanoTime();
        System.out.println("PageRanks:");
        web.computePageRank();
        tPRComputation = (System.nanoTime() - tPRComputation) / 1000000000;

        t = (System.nanoTime() - t) / 1000000000;

        System.out.println("Done in " + t + " s"
                + "\nSpending:\n"
                + tExploration/100 + " s in exploration\n"
                + tWalkers / 100 + " s in random walks\n"
                + tPRComputation + " s in PageRank computation\n");
        System.out.println("Exploration speed: " + web.getpages().size()*100 / tExploration + "pages/s.");
        System.out.println("Walkers speed: " + web.getNbVisitsTotal() * 100 / tWalkers + "pages/s.");
        
        Displayer disp = new Displayer(web, 1000,false);
        disp.start();
    }

}