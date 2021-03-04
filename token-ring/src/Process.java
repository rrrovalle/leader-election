import java.util.LinkedList;

public class Process {

    private int id;
    private boolean isCoordinator;

    public Process(int id){
        setId(id);
    }

    public Process(int id, boolean isCoordinator){
        setId(id);
        setCoordinator(isCoordinator);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }

    public boolean sendRequisition() {
        boolean reqResult = false;
        for(Process p : Ring.activeProcess) {
            if (p.isCoordinator) {
                reqResult = p.receiveReq(this.id);
            }
        }

        // If there's no coordinator yet
        if(!reqResult)
            this.doElection();

        System.out.println("Requisition process is finish!");
        return reqResult;
    }

    private boolean receiveReq(int pIdReqOrigin){
        /** REQUISITION CODE HERE */
        System.out.println("Process Requisition: "+pIdReqOrigin+" was successfully received!");
        return true;
    }

    private void doElection(){
        System.out.println("Election was started!");

        /** First of all we check all process, adding each one in a new list */
        LinkedList<Integer> idCheckedProcess = new LinkedList<>();
        for(Process p : Ring.activeProcess){
            p.checkProcess(idCheckedProcess);
        }

        /** then search for the biggest inside the list */
        int idNewCoordinator = this.getId();
        for (Integer id : idCheckedProcess){
            if (id > idNewCoordinator){
                idNewCoordinator = id;
            }
        }

        /** Update the new coordinator */
        boolean updateResult = false;
        updateResult = updateCoordinator(idNewCoordinator);

        if(updateResult){
            System.out.println("Election is finished! The process that was elected is "+idNewCoordinator);
        }else{
            System.out.println("Election was over and no one was elected.");
        }
    }

    private void checkProcess(LinkedList<Integer> checkedProcess){
        checkedProcess.add(this.getId());
    }

    private boolean updateCoordinator(int coordinatorId) {
        /** Make sure that there's only one process coordinator */
        for (Process p : Ring.activeProcess){
            if(p.getId() == coordinatorId){
                p.setCoordinator(true);
            }else{
                p.setCoordinator(false);
            }
        }
        return true;
    }
}
