package View_Controller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutSourcedPart;
import Model.Parts;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.mainScreenController.confirmationBox;
import static View_Controller.mainScreenController.infoBox;

public class addPartController implements Initializable {
    Inventory inv;
    Parts parts;

    @FXML RadioButton inHouseRadio;
    @FXML RadioButton outSourcedRadio;
    @FXML ToggleGroup partSourceToggle;
    @FXML Label companyNameLabel;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField costField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField companyNameField;
    @FXML private Button saveButton;
    private Stage stage;
    private Parent scene;

    private boolean isPartOutsourced;

    public addPartController(Inventory inv, Parts parts) {
        this.inv = inv;
        this.parts = parts;
    }

    @FXML
    private void clearTextField(ActionEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    @FXML
    private void inHouseSelect(ActionEvent event) {
        isPartOutsourced = false;
        companyNameLabel.setText("Machine ID");
    }

    @FXML
    private void outSourcedSelect(ActionEvent event) {
        isPartOutsourced = true;
        companyNameLabel.setText("Company Name");
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

    //save button
    @FXML
    public void saveButton(ActionEvent event) throws IOException {
        String partName = nameField.getText();
        String partStock = stockField.getText();
        String partCost = costField.getText();
        String partMin = minField.getText();
        String partMax = maxField.getText();
        String partSource = companyNameField.getText();

        //auto-generate new ID
        int newPartID = 1;
        for (Parts p : inv.getAllParts()) {
            if (p.getPartID() >= newPartID) {
                newPartID = p.getPartID() + 1;
            }
        }
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
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

            else if (!isPartOutsourced) {

                InHousePart inPart = new InHousePart(newPartID, partName, Integer.parseInt(partStock), Double.parseDouble(partCost), Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partSource));

                inv.addParts(inPart);

            } else if (isPartOutsourced) {
                OutSourcedPart outPart = new OutSourcedPart(newPartID, partName, Integer.parseInt(partStock), Double.parseDouble(partCost), Integer.parseInt(partMin), Integer.parseInt(partMax), partSource);

                inv.addParts(outPart);

            } else {
                infoBox("Error", "Error", "Input invalid");
                return;
            }

        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partSourceToggle = new ToggleGroup();
        inHouseRadio.setToggleGroup(partSourceToggle);
        outSourcedRadio.setToggleGroup(partSourceToggle);
        isPartOutsourced = false;

        idField.setText("Auto Gen - Disabled");
        idField.setDisable(true);
    }
}
