package main;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent_WebGraph {
    private ArrayList<Page> pages;
    private ReentrantLock lock;
    private HashMap<String, Page> map;
    private AtomicInteger nbVisitsTotal;

    static Random rd = new Random();

    public Concurrent_WebGraph() {
        pages = new ArrayList<Page>();
        lock = new ReentrantLock();
        map = new HashMap<String, Page>();
        nbVisitsTotal = new AtomicInteger(0);
    }

    public void visit(String origin, String destination) {
        Page pOrigin = getPage(origin);
        Page pDest = getPage(destination);
        pDest.visit();
        pOrigin.add_neighbor(pDest);
    }

    public Page getPage(String url) { //add the page if it does not exist
        lock.lock();
        try {
            if (map.containsKey(url)) {
                return map.get(url);
            } else {
                Page p = new Page(url);
                pages.add(p);
                map.put(url, p);
                return p;
            }
        } finally {
            lock.unlock();
        }
    }

    public void addPage(Page p) {
        lock.lock();
        try {
            if (!map.containsKey(p.get_url())) {
                pages.add(p);
                map.put(p.get_url(), p);
            }

        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Page> getpages() {
        return pages;
    }

    public int getNbVisitsTotal() {
        return nbVisitsTotal.get();
    }

    public void incrVisits() {
        int nb = nbVisitsTotal.get();
        while (!nbVisitsTotal.compareAndSet(nb, nb + 1)) {
            nb = nbVisitsTotal.get();
        }
    }

    public void incrVisits(Page p) {
        int nb = nbVisitsTotal.get();
        while (!nbVisitsTotal.compareAndSet(nb, nb + 1)) {
            nb = nbVisitsTotal.get();
        }
        p.visit();
    }

    public String getRandomUrl() {
//        Return a random url
        return pages.get(Math.abs(rd.nextInt()) % pages.size()).get_url();
    }

    public Page getRandomPage(Random rand) {
        return pages.get(Math.abs(rand.nextInt()) % pages.size());
    }

    public void computePageRank() {
        double s = 0;
        for (Page p : pages) {
            p.set_pageRank((double) p.get_nbVisits() / (double) nbVisitsTotal.get());
            s += p.get_pageRank();
            p.set_pageRank(-1/Math.log(p.get_pageRank()));
//            System.out.println(p.get_CSRank() + " CS for " + p.get_url());
        }

        LinkedList<Page> pagesSorted = new LinkedList(map.values());

        Collections.sort(pagesSorted);

        for (Page p : pagesSorted){
            System.out.println(p.get_pageRank() + " for " + p.get_url());
        }

        System.out.println("PageRank sum : " + s);
    }

}