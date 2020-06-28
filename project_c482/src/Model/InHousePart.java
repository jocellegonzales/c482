package Model;


public class InHousePart extends Parts {
    private int machineId;

    //constructor
    public InHousePart(int partID, String partName, int stock, double cost, int min, int max, int machineId){
        setPartID(partID);
        setPartName(partName);
        setStock(stock);
        setCost(cost);
        setMin(min);
        setMax(max);
        setMachineId(machineId);
    }

    //Mutator (setters)
    public void setMachineId(int machID){
        this.machineId = machID;
    }

    //Accessors (getters)
    public int getMachineId(){
        return machineId;
    }
}
