package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Graphe {
	private ArrayList<Page> vertices;
	private ReentrantLock lock;
	private HashMap<String,Page> map;
	
	public Graphe() {
		vertices= new ArrayList<Page>();
		lock= new ReentrantLock();
		map=new HashMap<String,Page>();
	}
	
	public boolean contains(Page p) {
		lock.lock();
		try {
			return vertices.contains(p);
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
	public Page get_page(String url) { //if the page is not contained, it is created.
		lock.lock();
		try {
			if (map.containsKey(url)){
				return map.get(url);
			}
			else {
				Page p = new Page(url);
				add_vertex(p);
				return p;
			}
		}
		finally {
			lock.unlock();
		}
	}
	public void add_vertex(Page p) {
		lock.lock();
		try {
			vertices.add(p);
			map.put(p.get_url(), p);
		}
		finally {
			lock.unlock();
		}
	}
	public ArrayList<Page> get_vertices(){
		return vertices;
	}
	public void lock() {
		lock.lock();
	}
	public void unlock() {
		lock.unlock();
	}
}
