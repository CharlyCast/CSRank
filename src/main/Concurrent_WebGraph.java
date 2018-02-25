package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent_WebGraph{
	private ArrayList<Page> nodes;
	private ReentrantLock lock;
	private HashMap<String,Page> map;
	private int nbVisitsTotal;
	public Concurrent_WebGraph() {
		nodes= new ArrayList<Page>();
		lock= new ReentrantLock();
		map=new HashMap<String,Page>();
		nbVisitsTotal=1;
	}

	public void visit(String origin, String destination) {
		Page pOrigin = getPage(origin);
		Page pDest = getPage(destination);
		pDest.visit();
		pOrigin.add_neighbor(pDest);
		
	}
	public void setTitle(String url, String title) {
		Page p=getPage(url);
		p.setTitle(title);
	}
	
	private Page getPage(String url) {
		lock.lock();
		try {
			if (map.containsKey(url)) {
				return map.get(url);
			}
			else {
				Page p = new Page(url);
				nodes.add(p);
				map.put(url, p);
				nbVisitsTotal+=1;
				return p;
			}
		}
		finally {
			lock.unlock();
		}
	}
	public ArrayList<Page> getNodes(){
		return nodes;
	}
	public int getNbVisitsTotal() {
		return nbVisitsTotal;
	}

}
