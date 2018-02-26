package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent_FIFO_Queue {
	ReentrantLock lock;
	LinkedList<Page> currentQueue;
	LinkedList<Page> nextQueue;
	HashSet<Page> alreadyVisited;
	
	public Concurrent_FIFO_Queue() {
		lock= new ReentrantLock();
		currentQueue=new LinkedList<Page>();
		nextQueue=new LinkedList<Page>();
		alreadyVisited=new HashSet<Page>();	
	}
	
	public void add(Page p) {
		lock.lock();
		try {
			if(!alreadyVisited.contains(p)) {
				nextQueue.add(p);
				alreadyVisited.add(p);
			}
		}
		finally {
			lock.unlock();
		}
	}
	public Page poll() {
		lock.lock(); 
		try {
			Page p =currentQueue.poll();;
			return p;
		}
		finally {
			lock.unlock();
		}
	}
	public boolean isEmpty() {
		return currentQueue.isEmpty();
	}
	public void switchQueue() {
		currentQueue=nextQueue;
		nextQueue = new LinkedList<Page>();
	}
}
