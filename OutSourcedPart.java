package Model;


public class OutSourcedPart extends Parts {
    private String companyName;

    //constructor
    public OutSourcedPart(int partID, String partName, int stock, double cost, int min, int max, String companyName){
        setPartID(partID);
        setPartName(partName);
        setStock(stock);
        setCost(cost);
        setMin(min);
        setMax(max);
        setCompanyName(companyName);
    }

    //Mutator (setters)
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    //Accessors (getters)
    public String getCompanyName(){
        return companyName;
    }
}
