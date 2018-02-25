package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
/*
!!!WARNING !!!
!!!WARNING !!!
THIS CLASS IS NOT USED ANYMORE
IT IS REPLACED BY Concurrent_WebGraph
IT WILL BE DELETED SOON
 */
public class Graphe {
	private ArrayList<Page> nodes;
	private ReentrantLock lock;
	private HashMap<String,Page> map;
	
	public Graphe() {
		nodes= new ArrayList<Page>();
		lock= new ReentrantLock();
		map=new HashMap<String,Page>();
	}
	
	public boolean contains(Page p) {
		lock.lock();
		try {
			return nodes.contains(p);
		}
		finally {
			lock.unlock();
		}
	}
	public boolean contains(String url) {
		lock.lock();
		try {
			return map.containsKey(url);
		}
		finally {
			lock.unlock();
		}
	}
	public Page getNode(String url) { //if the page is not contained in the Graphe, it is created and added to the Graphe.
		lock.lock();
		try {
			if (map.containsKey(url)){
				return map.get(url);
			}
			else {
				Page p = new Page(url);
				addNode(p);
				return p;
			}
		}
		finally {
			lock.unlock();
		}
	}
	public void addNode(Page p) {
		lock.lock();
		try {
			nodes.add(p);
			map.put(p.get_url(), p);
		}
		finally {
			lock.unlock();
		}
	}
	public ArrayList<Page> getNodes(){
		return nodes;
	}
	public void lock() {
		lock.lock();
	}
	public void unlock() {
		lock.unlock();
	}
}
