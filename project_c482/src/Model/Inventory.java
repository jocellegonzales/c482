package Model;


import Model.Parts;
import Model.Products;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.util.ArrayList;



public class Inventory {

    private ArrayList<Parts> allParts;
    private ArrayList<Products> allProducts;


    public Inventory() {
        allParts = new ArrayList<>();
        allProducts = new ArrayList<>();
    }

    public ArrayList<Parts> getAllParts() { return allParts; }

    public ArrayList<Products> getAllProducts() {
        return allProducts;
    }

    public int partsListSize() { return allParts.size(); }
    public int productsListSize() { return allProducts.size(); }


    //PARTS
    public void addParts(Parts partToAdd) {
        if (partToAdd != null) {
            allParts.add(partToAdd);
        }
    }

    public boolean deletePart(Parts partToDelete) {
        return allParts.remove(partToDelete);
    }

    public void updatePart(Parts partToUpdate) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToUpdate.partID) {
                allParts.set(i, partToUpdate);
                break;
            }
        }
        return;
    }

    public Parts searchForPart(int partToSearch) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allParts.get(i).getPartID() == partToSearch) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }

    //PRODUCTS
    public void addProducts(Products productToAdd) {
        if (productToAdd != null) {
            this.allProducts.add(productToAdd);
        }
    }

    public boolean deleteProduct(Products productToDelete) {
        return allProducts.remove(productToDelete);
    }

    public void updateProduct(Products products) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == products.getProductID()) {
                allProducts.set(i, products);
                break;
            }
        }
        return;
    }

    public Products searchForProduct(int productToSearch) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductID() == productToSearch) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

}




