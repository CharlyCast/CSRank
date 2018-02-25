package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Page {
	private final String url;
	private String title;
	private int nbVisits;
	//private HashMap<String,Page> outNeighbors;
	private ArrayList<Page> outNeighbors;
	private ReentrantLock lock;
	
	public Page(String url) {
		this.url=url;
		this.title="";
		lock = new ReentrantLock();
		nbVisits=0;
		outNeighbors=new ArrayList<Page>();
	}
	
	public void visit() {
		lock.lock();
		try {
			nbVisits+=1;
		}
		finally {
			lock.unlock();
		}
	}
	
	public boolean add_neighbor(Page p) {
		lock.lock();
		try {
			if (!outNeighbors.contains(p)) {
				outNeighbors.add(p);
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}
	}
	public void setTitle(String t) {
		title=t;
	}
	public String getTitle() {
		return title;
	}
	public String get_url() {
		return url;
	}
	public int get_nbVisits() {
		lock.lock();
		try {
			return nbVisits;
		}
		finally {
			lock.unlock();
		}
	}
	public int get_nbVisits_unsafe() {
		return nbVisits;
	}
	public ArrayList<Page> get_neighbors(){
		return this.outNeighbors;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page that = (Page) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

}
