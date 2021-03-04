package geral;

public class Process {
	
	public long id;
	
	public Process() {
		App.availableIdsLock.lock();
		try {
			if (!App.availableIds.isEmpty()) {
				id = App.availableIds.pop();
			} else {
				App.nextIdLock.lock();
				try {
					id = App.nextId++;
				} finally {
					App.nextIdLock.unlock();
				}
			}
		} finally {
			App.availableIdsLock.unlock();
		}
	} 
}
