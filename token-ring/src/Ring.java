import java.util.ArrayList;
import java.util.Random;

/**
 * Every 30s a new process should start
 * Every 25s a new process should make a coordinator req
 *
 */
public class Ring {

    private final int ADD = 3000;
    private final int REQ = 50;
    private final int INACTIVE_CORD = 10000;
    private final int INACTIVE_PROCESS = 8000;

    public static ArrayList<Process> activeProcess;
    private final Object lock = new Object();

    public Ring(){
        activeProcess = new ArrayList<Process>();
    }

    public void createProcess(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    synchronized (lock){
                        if(activeProcess.isEmpty()){
                            activeProcess.add(new Process(1, true));
                        }else{
                            activeProcess.add(new Process(
                                    activeProcess.get(activeProcess.size()-1).getId() + 1,false));
                            System.out.println("Process: "+activeProcess.get(activeProcess.size() - 1).getId()+" was created!");
                        }

                        try{
                            Thread.sleep(ADD);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void doRequisition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(REQ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    synchronized (lock) {
                        if(activeProcess.size() > 0){
                            int idxRandomProcess = new Random().nextInt(activeProcess.size());

                            Process reqProcess = activeProcess.get(idxRandomProcess);
                            System.out.println("Process"+reqProcess.getId()+" has made a requisition.");
                            reqProcess.sendRequisition();
                        }
                    }
                }
            }
        }).start();
    }

    public void doProcessInactive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(INACTIVE_PROCESS);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    synchronized (lock) {
                        if(!activeProcess.isEmpty()){
                            int idxRandomProcess = new Random().nextInt(activeProcess.size());
                            Process pRemove = activeProcess.get(idxRandomProcess);
                            if (pRemove != null && !pRemove.isCoordinator()){
                                activeProcess.remove(pRemove);
                                System.out.println("Process : "+pRemove.getId() + "is inactive!");
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void setCoordinatorInactive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(INACTIVE_CORD);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    synchronized (lock) {
                        Process coord = null;
                        for(Process p : activeProcess){
                            if(p.isCoordinator()){
                                coord = p;
                            }
                        }
                        if(coord != null){
                            activeProcess.remove(coord);
                            System.out.println("Coordinator Process: "+coord.getId()+"is inactive!");
                        }
                    }
                }
            }
        }).start();
    }
}
