public class Main {

    public static void main(String[] args){
        Ring logicRing = new Ring();

        logicRing.createProcess();
        logicRing.doRequisition();
        logicRing.setCoordinatorInactive();
        logicRing.doProcessInactive();
    }
}
