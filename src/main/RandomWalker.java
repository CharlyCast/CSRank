package main;

import java.util.ArrayList;
import java.util.Random;

public class RandomWalker extends Thread{
	Concurrent_WebGraph web;
	int nbSteps;
	Random rd;
	
	public RandomWalker(Concurrent_WebGraph web, int nb) {
		this.web=web;
		nbSteps=nb;
		rd=new Random();
	}
	
	public void run() {
		Page page=web.getRandomPage(rd);
		for(int i=0;i<nbSteps;i++) {
			page=explore(page);
		}
	}
	public Page explore(Page currentPage) {
		ArrayList<Page> neighbors = currentPage.get_neighbors();
		Page nextPage = neighbors.get(Math.abs(rd.nextInt()) % neighbors.size());
		nextPage.visit();
		web.incrVisits();
		return nextPage;
	}
}
