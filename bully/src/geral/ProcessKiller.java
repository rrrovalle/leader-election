package geral;

import java.util.Random;

public class ProcessKiller extends Thread {
	
	@Override
	public void run() {
		while (true) {
			try {
				/** wait 80s */
				Thread.sleep(80000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			App.processesLock.lock();
			try {
				Random r = new Random();
				long sequence   = r.nextInt(App.processes.size());
				Process process = App.processes.get((int) sequence);
				boolean isMaster = process == App.coordinator;
				App.kill(process);
				System.out.println("Process #"+process.id+" was terminated" + (isMaster ? " and it was the coordinator.":"."));
			} finally {
				App.processesLock.unlock();
			}
		}
	} 
}
