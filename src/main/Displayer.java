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
	public Displayer(Concurrent_WebGraph w) {
		web=w;
		graph_gs=new SingleGraph("graph");
		//pages=new HashSet<Page>();
		links=new HashMap<Page,HashSet<Page>>();
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
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
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			nbVisitsTotal=web.getNbVisitsTotal();
			pages= (ArrayList<Page>)(web.getNodes()).clone();
			for (Page p : pages) {
				nbVisits=p.get_nbVisits_unsafe();
				if (links.containsKey(p)) {
					n=graph_gs.getNode(p.get_url());
				}
				else {
					links.put(p, new HashSet<Page>());
					n=graph_gs.addNode(p.get_url());
					n.addAttribute("ui.style", "size:"+ ((float)(nbVisits)/(float)(nbVisitsTotal)*1000)+"px;fill-color:blue;");
					n.addAttribute("layout.weight", (float)(nbVisits)/(float)(nbVisitsTotal)*10);
				}
				
				n.addAttribute("ui.style", "size:"+ ((float)(nbVisits)/(float)(nbVisitsTotal)*100)+"px;fill-color:blue;");
				n.addAttribute("layout.weight", (float)(nbVisits)/(float)(nbVisitsTotal)*10);
				if ((float)(nbVisits)/(float)(nbVisitsTotal)>0.02 && nbVisits>10) {
					n.addAttribute("ui.label", p.getTitle().substring(0, Math.min(p.getTitle().length(), 25)));
				}
				
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
