package main;

public class Main {
    static int nbBot = 4;
    static int nbExplorationsPerBot = 50;
//    static String baseUrl="http://mythicspoiler.com/";
//    static String regex="http.+mythic.*";

//    static String baseUrl = "http://www.polytechnique.edu/";
//    static String regex = "http.+polytechnique.*";

//    static String baseUrl="http://www.centralesupelec.fr/";
//    static String regex = "http.+centralesupelec.*";

    static String baseUrl="https://www.insa-lyon.fr/";
    static String regex = "http.+insa-lyon.*";


    public static void main(String[] args) throws InterruptedException {

        long t = System.nanoTime();

        Concurrent_WebGraph web = new Concurrent_WebGraph();
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

        Displayer disp = new Displayer(web);
        disp.start();

        //Exploration du graphe
        ExplorationManager em = new ExplorationManager(web, baseUrl, regex);
        em.startExploration(nbBot, 3);


        // Détermination du PageRank.
        RandomWalker[] walkers = new RandomWalker[nbBot];
        for (int i = 0; i < nbBot; i++) {
            walkers[i] = new RandomWalker(web, nbExplorationsPerBot);
            walkers[i].start();
        }
        for (int i = 0; i < nbBot; i++) {
            walkers[i].join();
        }

        t = (System.nanoTime() - t) / 1000000000;

        System.out.println("Done in " + t + " s");

    }

}