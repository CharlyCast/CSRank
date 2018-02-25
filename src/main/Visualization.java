package main;
/*!!! OBSOLETE!!!
 *!!! OBSOLETE!!!
 *!!! OBSOLETE!!!
 */
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Visualization {
	Concurrent_WebGraph web;
	SingleGraph graph;
	public Visualization(Concurrent_WebGraph w) {
		web=w;
		graph = new SingleGraph("Tutorial 1");
		//graph = new Graphe_gs("Tutorial 1");
		int nbVisits=0;
		for (Page p: web.getNodes()) {
				nbVisits+=p.get_nbVisits();
		}
		for (Page p: web.getNodes()) {
			Node n= graph.addNode(p.get_url());
			n.addAttribute("ui.style", "fill-color:blue;size: "+Float.toString((float)(p.get_nbVisits())/ (float)(nbVisits)*1000)+"px;"); // The size of a node is proportional to its number of visits
			n.addAttribute("layout.weight", p.get_nbVisits()); // The important pages are more "repulsive" (ie, they are more distant from one another in the display)
			if ((float)(p.get_nbVisits())/ (float)(nbVisits) >0.015) { // Only display the title of major pages
				n.addAttribute("ui.label", p.getTitle().substring(0, Math.min(p.getTitle().length(), 20)));
			}
			
		}
		for (Page p: web.getNodes()) {
			for (Page neighbor :p.get_neighbors()) {
				Edge e =graph.addEdge(p.get_url() + neighbor.get_url(), p.get_url(), neighbor.get_url(),true);
				e.addAttribute("ui.style", "size: 0.0001px;arrow-size: 0.0001px;");
			}
		}
	
	}
	public void display() {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.antialias");
		graph.display();
	}
	
}
