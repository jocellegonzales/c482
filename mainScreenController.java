package View_Controller;

import Model.Inventory;
import Model.Parts;
import Model.Products;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class mainScreenController implements Initializable {

    public mainScreenController(Inventory inv) { this.inv = inv; }
    Inventory inv;
    Parts parts;
    Products products;

    @FXML private TextField partSearch;
    @FXML private TextField productSearch;
    @FXML private TableView<Parts> partsTable;
    @FXML private TableView<Products> productsTable;
    private ObservableList<Parts> partInventory = FXCollections.observableArrayList();
    private ObservableList<Products> productInventory = FXCollections.observableArrayList();
    private ObservableList<Parts> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Products> productInventorySearch = FXCollections.observableArrayList();
    private static int modPartInd;
    private static int modProdInd;

    public static int modifyPartIndex() {
        return modPartInd;
    }

    public static int modifyProdIndex() {
        return modProdInd;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partsTableFill();
        productsTableFill();

    }

    private void partsTableFill() {
        partInventory.setAll(inv.getAllParts());

        partsTable.setItems(partInventory);
        partsTable.refresh();

    }
    private void productsTableFill(){
        productInventory.setAll(inv.getAllProducts());

        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    //BUTTONS
    @FXML
    private void addPart(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/addPart.fxml"));
        addPartController controller = new addPartController(inv, parts);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void modifyPart(ActionEvent event) throws IOException {

            Parts selected = partsTable.getSelectionModel().getSelectedItem();
            modPartInd = inv.getAllParts().indexOf(selected);
            if (partInventory.isEmpty()) {
                infoBox("Error", "Selection Error", "There is nothing to modify");
                return;
            }
            if (!partInventory.isEmpty() && selected == null) {
                infoBox("Error", "Selection Error", "There is nothing to modify");
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyPart.fxml"));
                modifyPartController controller = new modifyPartController(inv, selected);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }

    }

    @FXML
    private void deletePart(ActionEvent event) {
        Parts removePart = partsTable.getSelectionModel().getSelectedItem();

        if (partInventory.isEmpty()){
            infoBox("Error", "Error", "There is nothing to delete.");
            return;
        }
        if (!partInventory.isEmpty() && removePart == null){
            infoBox("Error", "Error", "Select an item to delete.");
            return;
        } else if (confirmationBox(removePart.getPartName(), "Are you sure?")) {
            inv.deletePart(removePart);
            partInventory.remove(removePart);
            partsTable.refresh();
            infoBox("Success", "Deleted", "Deleted successfully.");
        }

    }
    @FXML
    private void addProduct(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/addProduct.fxml"));
        addProductController controller = new addProductController(inv, products);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void modifyProduct(ActionEvent event) throws IOException {
        Products selected = productsTable.getSelectionModel().getSelectedItem();
        modProdInd = inv.getAllProducts().indexOf(selected);
        if (productInventory.isEmpty()) {
            infoBox("Error", "Selection Error", "There is nothing to modify");
            return;
        }
        if (!productInventory.isEmpty() && selected == null) {
            infoBox("Error", "Selection Error", "There is nothing to modify");
            return;
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/modifyProduct.fxml"));
            modifyProductController controller = new modifyProductController(inv, products);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void deleteProduct() {
        Products removeProduct = productsTable.getSelectionModel().getSelectedItem();

        if (productInventory.isEmpty()){
            infoBox("Error", "Error", "There is nothing to delete.");
            return;
        }
        if (!productInventory.isEmpty() && removeProduct == null){
            infoBox("Error", "Error", "Select an item to delete.");
            return;
        } else if (confirmationBox(removeProduct.getProductName(), "Are you sure?")) {
            inv.deleteProduct(removeProduct);
            productInventory.remove(removeProduct);
            productsTable.refresh();
            infoBox("Success", "Deleted", "Deleted successfully.");
        }
    }

    @FXML
    void exitButton(ActionEvent event) {
        Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
        exit.setHeaderText("Are you sure you want to exit?");
        exit.initModality(Modality.NONE);
        Optional<ButtonType> choice = exit.showAndWait();
        if (choice.get() == ButtonType.OK) {
            Platform.exit();
        } else {
            System.out.println("Canceled");
        }
    }

    @FXML
    private void searchPartButton(ActionEvent event){
        if (!partSearch.getText().trim().isEmpty()){
            partInventorySearch.clear();
            for(Parts p : inv.getAllParts()) {
                if (p.getPartName().contains(partSearch.getText().trim())) {
                    partInventorySearch.add(p);
                }
            }
            partsTable.setItems(partInventorySearch);
            partsTable.refresh();
        }
    }

    @FXML
    private void searchProductButton(ActionEvent event){
        if (!productSearch.getText().trim().isEmpty()){
            productInventorySearch.clear();
            for(Products p : inv.getAllProducts()) {
                if (p.getProductName().contains(productSearch.getText().trim())) {
                    productInventorySearch.add(p);
                }
            }
            productsTable.setItems(productInventorySearch);
            productsTable.refresh();
        }
    }


    //DIALOG BOXES
    static void infoBox(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    static boolean confirmationBox(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

}
