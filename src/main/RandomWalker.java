package main;

import java.util.ArrayList;
import java.util.Random;

public class RandomWalker implements Runnable{
    static double alpha=0.85;
	Concurrent_WebGraph web;
	Page startPage;
	
	Random rd;

	public RandomWalker(Concurrent_WebGraph web, Page p) {
		this.web=web;
		startPage=p;
		rd=new Random();
	}

	public void run() {
		Page page=startPage;
		while(page!=null) {
			page=explore(page);
		}
	}

	public Page explore(Page currentPage) {
		ArrayList<Page> neighbors = currentPage.get_neighbors();
		Page nextPage;
		if (Math.abs(rd.nextDouble())%1>alpha){
			nextPage=null;
        }
        else{
            if (neighbors.size()>0) {
                nextPage = neighbors.get(Math.abs(rd.nextInt()) % neighbors.size());
                nextPage.visit();
        		web.incrVisits();
            }
            else {
            	nextPage=null;
            }
        }
		return nextPage;
	}
}
