package main;

public class ExplorationManager {
	Concurrent_FIFO_Queue queue;
	Concurrent_WebGraph web;
	String regex;

	
	public ExplorationManager(Concurrent_WebGraph web, String firstUrl,String regex) {
		this.queue=new Concurrent_FIFO_Queue();
		this.web=web;
		this.regex=regex;
		Page firstPage=new Page(firstUrl);
		queue.add(firstPage);
		web.addPage(firstPage);
		queue.switchQueue();
	}
	
	public void startExploration(int nbExplorers, int nbSteps) throws InterruptedException {
		Explorer[] explo = new Explorer[nbExplorers];
        
		for (int j=0;j<nbSteps;j++) {
			for (int i = 0; i < nbExplorers; i++) {
				explo[i] = new Explorer(web,queue,regex);
				explo[i].start();
			}
			for (int i = 0; i < nbExplorers; i++) {
				explo[i].join();
			}
			queue.switchQueue();
		}
        
	}

}
