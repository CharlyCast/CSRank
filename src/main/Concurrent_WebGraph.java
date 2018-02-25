package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Concurrent_WebGraph{
	private ArrayList<Page> nodes;
	private ReentrantLock lock;
	private HashMap<String,Page> map;
	private SingleGraph graph;
	private int nbVisitsTotal;
	public Concurrent_WebGraph() {
		nodes= new ArrayList<Page>();
		lock= new ReentrantLock();
		map=new HashMap<String,Page>();
		nbVisitsTotal=1;
		graph=new SingleGraph("graph");
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.antialias");
		graph.display();
	}

	public void visit(String origin, String destination) {
		Page pOrigin = getPage(origin);
		Page pDest = getPage(destination);
		pDest.visit();
		lock.lock();
		try {
			if (pOrigin.add_neighbor(pDest)) {
			Edge e= graph.addEdge(origin+destination, origin, destination,true);
			e.addAttribute("ui.style", "size: 0.001px;arrow-size: 1px;");
			}
			
			Node n=graph.getNode(destination);
			n.addAttribute("ui.style", "size:"+ (pDest.get_nbVisits()*100/nbVisitsTotal)+"px;fill-color:blue;");
			n.addAttribute("layout.weight", (float)(pDest.get_nbVisits())/(float)(nbVisitsTotal)*10);
			if ((float)(pDest.get_nbVisits())/(float)(nbVisitsTotal)>0.02 && pDest.get_nbVisits()>10) {
				n.addAttribute("ui.label", pDest.getTitle().substring(0, Math.min(pDest.getTitle().length(), 25)));
			}
		}
		finally {
			lock.unlock();
		}
		
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
				Node n=graph.addNode(url);
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

}
