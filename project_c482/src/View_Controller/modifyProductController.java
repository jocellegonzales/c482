package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import static View_Controller.mainScreenController.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;


import static View_Controller.mainScreenController.confirmationBox;
import static View_Controller.mainScreenController.infoBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class modifyProductController implements Initializable {
    Inventory inv;
    Products products;

    @FXML RadioButton inHouseRadio;
    @FXML RadioButton outSourcedRadio;
    @FXML Label companyNameLabel;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TableView<Parts> associatedPartsTable;
    @FXML private TableView<Parts> partSearchTable;
    @FXML private TextField searchBar;

    int modifyProductIndex = modifyProdIndex();
    private int productID;
    private ObservableList<Parts> partInventory = FXCollections.observableArrayList();
    private ObservableList<Parts> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Parts> associatedPartsList = FXCollections.observableArrayList();


    public modifyProductController(Inventory inv, Products products){
        this.inv = inv;
        this.products = products;
    }

    private void populateSearchTable() {
        partInventory.setAll(inv.getAllParts());

        partSearchTable.setItems(partInventory);
        partSearchTable.refresh();
    }

    @FXML
    private void addPart(ActionEvent event){
        Parts addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart == null){
            return;
        } else {
            int id = addPart.getPartID();
            //validate that item doesn't already exist
            for (int i = 0; i < associatedPartsList.size(); i++){
                if(associatedPartsList.get(i).getPartID() == id) {
                    infoBox("Error", "Error", "You cannot add the same part twice.");
                    repeatedItem = true;
                }
            }
            if (!repeatedItem){
                associatedPartsList.add((addPart));
            }
            associatedPartsTable.setItems(associatedPartsList);
        }

    }

    @FXML
    private void saveButton(ActionEvent event) throws IOException {

        for (int i = 0; i < associatedPartsList.size(); i++) {

        }
        if (nameField.getText().trim().isEmpty() || nameField.getText().trim().toLowerCase().equals("product name")) {
            infoBox("Error", "Error", "Invalid input");
            return;
        }
        if (Integer.parseInt(minField.getText().trim()) > Integer.parseInt(maxField.getText().trim())) {
            infoBox("Error", "Error", "Minimum cannot be higher than the maximum");
            return;
        }
        if (Integer.parseInt(stockField.getText().trim()) < Integer.parseInt(minField.getText().trim())) {
            infoBox("Error", "Error", "Inventory cannot be lower than the minimum");
            return;
        }
        if (Integer.parseInt(stockField.getText().trim()) > Integer.parseInt(maxField.getText().trim())) {
            infoBox("Error", "Error", "Inventory cannot be higher than the maximum");
            return;
        }

        saveModifyProduct();
        //mainScreen(event);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/mainScreen.fxml"));
        mainScreenController controller = new mainScreenController(inv);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    private void saveModifyProduct(){
        String prodName = nameField.getText();
        String stock = stockField.getText();
        String cost = priceField.getText();
        String min = minField.getText();
        String max = maxField.getText();

        Products products = new Products(productID, prodName, Integer.parseInt(stock), Double.parseDouble(cost), Integer.parseInt(min), Integer.parseInt(max));

        for (int i = 0; i < associatedPartsList.size(); i++) {
            products.addAssociatedPart(associatedPartsList.get(i));
        }
        inv.updateProduct(products);
    }

    @FXML
    public void cancelButton(ActionEvent event) throws IOException {
        if(confirmationBox("Cancel?", "Are you sure?")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/mainScreen.fxml"));
            mainScreenController controller = new mainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void deletePart(ActionEvent event){
        Parts removePart = associatedPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if(removePart != null){
            boolean remove = confirmationBox(removePart.getPartName(), "Are you sure?");
            if (remove) {
                associatedPartsList.remove(removePart);
                associatedPartsTable.refresh();
                infoBox("Success", "Deleted", "Deleted successfully.");
            }
        } else {
            return;
        }

    }

    @FXML
    private void modifyProductSearch(ActionEvent event) {
        if (searchBar.getText() != null && searchBar.getText().trim().length() != 0) {
            partInventorySearch.clear();
            for (Parts p : inv.getAllParts()) {
                if (p.getPartName().contains(searchBar.getText().trim())) {
                    partInventorySearch.add(p);
                }
            }
            partSearchTable.setItems(partInventorySearch);
        }
    }


    @FXML
    void clearTextField (ActionEvent event){
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == searchBar) {
            partSearchTable.setItems(partInventory);
        }
    }


    private void setData() {
        Products products = inv.getAllProducts().get(modifyProductIndex);
        productID = inv.getAllProducts().get(modifyProductIndex).getProductID();

        idField.setText("ID:" + productID);
        idField.setDisable(true);

        for (int i = 0; i < 1000; i++) {
            Parts parts = products.searchAssociatedPart(i);
            if (parts != null){
                associatedPartsList.add(parts);
            }
        }

        associatedPartsTable.setItems(associatedPartsList);


        this.nameField.setText(products.getProductName());
        this.idField.setText(Integer.toString(products.getProductID()));
        this.stockField.setText(Integer.toString(products.getStock()));
        this.priceField.setText(Double.toString(products.getCost()));
        this.minField.setText(Integer.toString(products.getMin()));
        this.maxField.setText(Integer.toString(products.getMax()));



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSearchTable();
        setData();
    }
}
