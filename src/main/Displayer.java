package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javafx.util.Pair;

public class Displayer extends Thread {
	Concurrent_WebGraph web;
	SingleGraph graph_gs;
	//HashSet<Page> pages;
	HashMap<Page,HashSet<Page>> links;
	int nodeSize;
	public Displayer(Concurrent_WebGraph w, int size) {
		web=w;
		nodeSize=size;
		graph_gs=new SingleGraph("graph");
		//pages=new HashSet<Page>();
		links=new HashMap<Page,HashSet<Page>>();
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph_gs.addAttribute("ui.antialias");
		graph_gs.display();
	}
	
	@Override
    public void run() {
		int nbVisits;
		int nbVisitsTotal;
		Node n;
		ArrayList<Page> pages;
		ArrayList<Page> neighbors;
		while(true) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			nbVisitsTotal=web.getNbVisitsTotal();
			pages= (ArrayList<Page>)(web.getpages()).clone();
			for (Page p : pages) {
				nbVisits=p.get_nbVisits_unsafe();
				if (links.containsKey(p)) {
					n=graph_gs.getNode(p.get_url());
				}
				else {
					links.put(p, new HashSet<Page>());
					n=graph_gs.addNode(p.get_url());
				}
				float csrank= (float)(nbVisits)/(float)(nbVisitsTotal);
				n.addAttribute("ui.style", "size:"+ (csrank*nodeSize)+"px;fill-color:rgb(0,62,150);");
				n.addAttribute("layout.weight", csrank*nodeSize);
				if (csrank>0.002f && nbVisits>10) {
					n.addAttribute("ui.label", p.get_url().substring(0, Math.min(p.get_url().length(), 45)));
					//n.addAttribute("ui.label", p.getTitle().substring(0, Math.min(p.getTitle().length(), 25)));
				}
				/*else{
					System.out.println(p.get_url());
					System.out.println(p.get_nbVisits_unsafe());
				}*/
				
				neighbors=(ArrayList<Page>)(p.get_neighbors()).clone();
				for (Page neighbor : neighbors) {
					if (!links.get(p).contains(neighbor) && links.containsKey(neighbor)) {
						links.get(p).add(neighbor);
						Edge e= graph_gs.addEdge(p.get_url()+neighbor.get_url(), p.get_url(), neighbor.get_url(),true);
						e.addAttribute("ui.style", "size: 0.001px;arrow-size: 1px;");
					}
				}
				
			}
		}
	}
}
