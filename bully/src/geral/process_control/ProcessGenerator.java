package geral.process_control;

import geral.App;
import geral.Process;

public class ProcessGenerator extends Thread {
	
	@Override
	public void run() {
		while (true) {
			App.processesLock.lock();
			try {
				Process p = new Process();
				App.processes.add(p);
				System.out.println("New process started (#"+p.id+").");
			} finally {
				App.processesLock.unlock();
			}
			try {
				/** Every 30s a new process is created */
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
