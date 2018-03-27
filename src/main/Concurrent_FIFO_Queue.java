package main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent_FIFO_Queue {
    ReentrantLock lockCurrentQueue;
    ReentrantLock lockNextQueue;
    LinkedList<Page> currentQueue;
    LinkedList<Page> nextQueue;
    HashSet<Page> alreadyVisited;

    public Concurrent_FIFO_Queue() {
        lockCurrentQueue = new ReentrantLock();
        lockNextQueue = new ReentrantLock();
        currentQueue = new LinkedList<Page>();
        nextQueue = new LinkedList<Page>();
        alreadyVisited = new HashSet<Page>();
    }

    public void add(Page p) {
        lockNextQueue.lock();
        try {
            if (!alreadyVisited.contains(p)) {
                nextQueue.add(p);
                alreadyVisited.add(p);
            }
        } finally {
            lockNextQueue.unlock();
        }
    }

    public Page poll() {
        lockCurrentQueue.lock();
        try {
            Page p = currentQueue.poll();
            return p;
        } finally {
            lockCurrentQueue.unlock();
        }
    }

    public boolean isEmpty() {
        return currentQueue.isEmpty();
    }

    public void switchQueue() {
    	System.out.println("Switching queue. "  + nextQueue.size() + " pages will be explored on the next depth.");
        currentQueue = nextQueue;
        nextQueue = new LinkedList<Page>();
    }
}
