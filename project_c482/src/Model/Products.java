package Model;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;

import java.util.ArrayList;

public class Products {

    private ArrayList<Parts> associatedParts = new ArrayList<>();
    private int productID;
    private String productName;
    private double cost;
    private int stock = 0;
    private int min;
    private int max;
    private double price;

    //constructor
    public Products(int productID, String productName, int stock, double cost, int min, int max) {
        setProductID(productID);
        setProductName(productName);
        setStock(stock);
        setCost(cost);
        setMin(min);
        setMax(max);
    }

    //ASSOCIATED PARTS
    public ArrayList<Parts> getAssociatedParts() {
        return associatedParts;
    }

    public void addAssociatedPart(Parts partToAdd){
        associatedParts.add(partToAdd);
    }

    public boolean removeAssociatedPart(int partToRemove){
        int i;
        for(i = 0; i < associatedParts.size(); i++){
            if(associatedParts.get(i).getPartID() == partToRemove){
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public Parts searchAssociatedPart(int partToSearch){
        for(int i = 0; i < associatedParts.size(); i++){
            if (associatedParts.get(i).getPartID() == partToSearch){
                return associatedParts.get(i);
            }
        }
        return null;
    }
    //GETTERS AND SETTERS

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getCost() { return cost; }

    public void setCost(double cost) { this.cost = cost; }

    public int getMin() {
        return min;
    }

    public void setMin(int min) { this.min = min; }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}