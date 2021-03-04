package geral;

public class CoordinatorKiller extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(800000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			App.processesLock.lock();
			try {
					if(App.coordinator != null) {
						System.out.println("Coordinator is dead (#"+App.coordinator.id+").");
					}
					App.kill(App.coordinator);
			} finally {
					App.processesLock.unlock();
			}
		}
	}
}
