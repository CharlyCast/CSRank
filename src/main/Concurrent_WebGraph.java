package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent_WebGraph {
    private ArrayList<Page> pages;
    private ReentrantLock lock;
    private HashMap<String, Page> map;
    private int nbVisitsTotal;
    private ReentrantLock visitsLock; // protects the access to nbVisitsTotal

    static Random rd = new Random();

    public Concurrent_WebGraph() {
        pages = new ArrayList<Page>();
        lock = new ReentrantLock();
        map = new HashMap<String, Page>();
        nbVisitsTotal = 1;
        visitsLock = new ReentrantLock();
    }

    public void visit(String origin, String destination) { // Not used anymore (by Explorers and RandomWalkers)
        Page pOrigin = getPage(origin);
        Page pDest = getPage(destination);
        pDest.visit();
        pOrigin.add_neighbor(pDest);
    }

    public Page getPage(String url) {
        lock.lock();
        try {
            if (map.containsKey(url)) {
                return map.get(url);
            } else {
                Page p = new Page(url);
                pages.add(p);
                map.put(url, p);
                nbVisitsTotal += 1;
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
        return nbVisitsTotal;
    }

    public void incrVisits() {
        visitsLock.lock();
        try {
            nbVisitsTotal += 1;
        } finally {
            visitsLock.unlock();
        }
    }

    public String getRandomUrl() {
//        Return a random url

        return pages.get(Math.abs(rd.nextInt()) % pages.size()).get_url();
    }

    public Page getRandomPage(Random rand) {
        return pages.get(Math.abs(rand.nextInt()) % pages.size());
    }


}
