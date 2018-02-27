package main;

import java.util.ArrayList;
import java.util.Random;

public class RandomWalker extends Thread{
    static double alpha=0.85;
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
		Page nextPage;
		if (Math.abs(rd.nextDouble())%1>alpha){
            nextPage=web.getRandomPage(rd);
        }
        else{
            if (neighbors.size()>0) {
                nextPage = neighbors.get(Math.abs(rd.nextInt()) % neighbors.size());
            }
            else {
                nextPage=web.getRandomPage(rd);
            }
        }

		nextPage.visit();
		web.incrVisits();
		return nextPage;
	}
}
