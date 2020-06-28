package View_Controller;

import Model.Inventory;
import Model.Parts;
import Model.Products;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.mainScreenController.confirmationBox;
import static View_Controller.mainScreenController.infoBox;

public class addProductController implements Initializable {
    Inventory inv;
    Products products;

    @FXML
    RadioButton inHouseRadio;
    @FXML RadioButton outSourcedRadio;
    @FXML
    Label companyNameLabel;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TableView<Parts> associatedPartsTable;
    @FXML private TableView<Parts> partSearchTable;
    @FXML private TextField searchBar;

    private ObservableList<Parts> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Parts> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Parts> associatedPartsList = FXCollections.observableArrayList();


    public addProductController(Inventory inv, Products products){
        this.inv = inv;
        this.products = products;
    }

    private void populateSearchTable() {
        partsInventory.setAll(inv.getAllParts());

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }

    @FXML
    private void addProductPart(ActionEvent event){
        Parts addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart == null){
            return;
        } else {
            int id = addPart.getPartID();
            //validate that item doesn't already exist
            for (int i = 0; i < associatedPartsList.size(); i++){
                if(associatedPartsList.get(i).getPartID() == id) {
                    infoBox("Error", "Error", "Item already exists");
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
        if (nameField.getText().trim().isEmpty() || nameField.getText().trim().toLowerCase().equals("part name")) {
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

        saveAddProduct();
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


    private void saveAddProduct() {
        String prodName = nameField.getText();
        String stock = stockField.getText();
        String cost = priceField.getText();
        String min = minField.getText();
        String max = maxField.getText();

        //auto-gen new ID
        int newProdID = 1;
        for (Products p : inv.getAllProducts()) {
            if (p.getProductID() >= newProdID) {
                newProdID = p.getProductID() + 1;
            }
        }

        Products products = new Products(newProdID, prodName, Integer.parseInt(stock), Double.parseDouble(cost), Integer.parseInt(min), Integer.parseInt(max));

        for (int i = 0; i < associatedPartsList.size(); i++) {
            products.addAssociatedPart(associatedPartsList.get(i));
        }
        inv.addProducts(products);
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
    private void deleteProductPart(ActionEvent event){
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
    private void addProductSearch(ActionEvent event){
        if (searchBar.getText() != null && searchBar.getText().trim().length() != 0){
            partInventorySearch.clear();
            for (Parts p : inv.getAllParts()) {
                if(p.getPartName().contains(searchBar.getText().trim())) {
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
            partSearchTable.setItems(partsInventory);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSearchTable();
        idField.setText("Auto Gen - Disabled");
        idField.setDisable(true);
    }
}

