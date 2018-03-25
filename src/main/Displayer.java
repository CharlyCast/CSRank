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
	HashMap<Page,HashSet<Page>> links;
	int nodeSize;
	public Displayer(Concurrent_WebGraph w, int size) {
		web=w;
		nodeSize=size;
		graph_gs=new SingleGraph("graph");
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
			nbVisitsTotal=Math.max(web.getNbVisitsTotal(),1);
			pages= (ArrayList<Page>)(web.getpages()).clone();
			for (Page p : pages) {
				nbVisits=p.get_nbVisits();
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
				if (csrank>0.5f/pages.size()) {
					//n.addAttribute("ui.label", p.get_url().substring(0, Math.min(p.get_url().length(), 45)));
					n.addAttribute("ui.label", p.getTitle().substring(0, Math.min(p.getTitle().length(), 25)));
				}
				else{
					n.removeAttribute("ui.label");
				}
				
				neighbors=(ArrayList<Page>)(p.get_neighbors()).clone();
				for (Page neighbor : neighbors) {
					if (links.containsKey(neighbor)){
						if (!links.get(p).contains(neighbor)) {
							links.get(p).add(neighbor);
							Edge e= graph_gs.addEdge(p.get_url()+neighbor.get_url(), p.get_url(), neighbor.get_url(),true);
						}
					
						float csrank2 = (float)(neighbor.get_nbVisits())/(float)(nbVisitsTotal);
						float mean_csrank= (float)Math.sqrt((csrank2*csrank));
						if (neighbor==p){
							mean_csrank=0;
						}
						Edge e=graph_gs.getEdge(p.get_url()+neighbor.get_url());
						e.addAttribute("ui.style", "size:"+ mean_csrank*nodeSize*0.05+"px;arrow-size:"+ mean_csrank*nodeSize*0.1+"px;");
					}		
				}
				
			}
		}
	}
}
