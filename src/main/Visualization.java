package main;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Visualization {
	Graphe web;
	Graph graph;
	public Visualization(Graphe w) {
		web=w;
		graph = new SingleGraph("Tutorial 1");
		int nbVisits=0;
		for (Page p: web.get_vertices()) {
				nbVisits+=p.get_nbVisits();
		}
		for (Page p: web.get_vertices()) {
			Node n= graph.addNode(p.get_url());
			n.addAttribute("ui.style", "fill-color:blue;size: "+Float.toString((float)(p.get_nbVisits())/ (float)(nbVisits)*1000)+"px;"); // The size of a node is proportional to its number of visits
			if ((float)(p.get_nbVisits())/ (float)(nbVisits) >0.02) { // Only display the title of major pages
				n.addAttribute("ui.label", p.get_title().substring(0, Math.min(p.get_title().length(), 20)));
			}
			
		}
		for (Page p: web.get_vertices()) {
			for (Page neighbor :p.get_neighbors()) {
				Edge e =graph.addEdge(p.get_url() + neighbor.get_url(), p.get_url(), neighbor.get_url(),true);
				e.addAttribute("ui.style", "size: 0.001px;arrow-size: 0.001px;");
				//e.addAttribute("ui.style", "");
			}
		}
	
	}
	public void display() {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.antialias");
		graph.display();
	}
	
}
