package geral;

import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
	
	public static final Lock processesLock    = new ReentrantLock();
	public static final Lock availableIdsLock = new ReentrantLock();
	public static final Lock nextIdLock       = new ReentrantLock();
	
	public static List<Process> processes = new LinkedList<>();
	public static Deque<Long> availableIds = new ArrayDeque<>();
	public static long nextId = (long) 1;
	public static Process coordinator;
	public static boolean electionGoingOn = false;
	
	public static void main(String[] args) {
		System.out.println("Starting...");
		
		/** Start simulation */
		ProcessGenerator 	generator   = new ProcessGenerator();
		ProcessKiller    	killer      = new ProcessKiller();
		RequestMaker 	 	reqMaker    = new RequestMaker();
		CoordinatorKiller 	coordKiller = new CoordinatorKiller();
		generator.start();
		killer.start();
		reqMaker.start();
		coordKiller.start();
		
		System.out.println("Threads started..");
		System.out.println("Updating simulation status..");
		
		while (true) {
			String processesList = "";
			for (Process process : processes) {
				if(processesList != "") {
					processesList += ", ";
				}
				processesList += process.id;
			}
			
			System.out.println("Number of processes: " + processes.size());
			System.out.println("Processes: [" + processesList + "]");
			System.out.println("Master: " + (App.coordinator == null? "null" : App.coordinator.id));
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			System.out.println("\n\n");
		}
	}
	
	public static void election(Process requester) {
		electionGoingOn = true;
		for (Process process : processes ) {
			if(process != requester) {
				if (process.id > requester.id) {
					System.out.println("#" + requester.id + "calls election for #" + process.id);
					System.out.println("#" + process.id   + ": "+"Ok");
				election(process);
				return;
				}
			}
		}
		electionGoingOn = false;
		coordinator     = requester;
	}
	
	public static void request(Process requester) {
		if (App.coordinator == null && !App.electionGoingOn) {
			App.election(requester);
			System.out.println("Process #"+ requester.id + " started an election and #"+ App.coordinator.id + " won.");
		} else {
			System.out.println("Process #"+ requester.id + " made a succesful request to the coordinator (#"+ App.coordinator.id + ").");
		}
	}
	
	public static void kill(Process process) {
		if(process != null) {
			if (coordinator == process) {
				coordinator = null;
			}
			App.availableIdsLock.lock();
			try {
				availableIds.push(process.id);
			} finally {
				App.availableIdsLock.unlock();
			}
			processes.remove(process);
		}
	} 
}
