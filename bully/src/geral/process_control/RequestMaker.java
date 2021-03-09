package geral.process_control;

import java.util.Random;

import geral.App;
import geral.Process;

public class RequestMaker extends Thread {
	//makes a request every 25s
	
	@Override
	public void run() {
		while (!false) {
			App.processesLock.lock();
			try {
				Random r 	    = new Random();
				long sequence   = r.nextInt(App.processes.size()); 
				Process requester = App.processes.get((int) sequence);
				App.request(requester);
			} finally {
				App.processesLock.unlock();
			}
			try {
				Thread.sleep(25000);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
