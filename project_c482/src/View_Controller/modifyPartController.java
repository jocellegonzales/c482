package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Model.InHousePart;
import Model.Inventory;
import Model.OutSourcedPart;
import Model.Parts;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.mainScreenController.*;

public class modifyPartController implements Initializable {
    Inventory inv;
    Parts parts;

    @FXML RadioButton inHouseRadio;
    @FXML RadioButton outSourcedRadio;
    @FXML Label companyNameLabel2;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField costField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField companyNameField;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    int modifyPartIndex = modifyPartIndex();
    private int partID;
    private ToggleGroup partSourceToggle;

    private boolean isPartOutsourced;

    public modifyPartController(Inventory inv, Parts parts){
        this.inv = inv;
        this.parts = parts;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    @FXML
    void inHouseSelect(ActionEvent event) {
        isPartOutsourced = false;
        companyNameLabel2.setText("Machine ID");
    }

    @FXML
    void outSourcedSelect(ActionEvent event) {
        isPartOutsourced = true;
        companyNameLabel2.setText("Company Name");
    }


    private void setData() {
        partSourceToggle = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(partSourceToggle);
        this.outSourcedRadio.setToggleGroup(partSourceToggle);

        Parts parts = inv.getAllParts().get(modifyPartIndex);
        partID = inv.getAllParts().get(modifyPartIndex).getPartID();


        //if inhouse selected
        if (parts instanceof InHousePart){
            InHousePart part1 = (InHousePart) parts;
            inHouseRadio.setSelected(true);
            isPartOutsourced = false;
            companyNameLabel2.setText("Machine ID");
            idField.setText("ID:" + partID);
            idField.setDisable(true);

            this.nameField.setText(part1.getPartName());
            this.idField.setText(Integer.toString(part1.getPartID()));
            this.stockField.setText(Integer.toString(part1.getStock()));
            this.costField.setText(Double.toString(part1.getCost()));
            this.minField.setText(Integer.toString(part1.getMin()));
            this.maxField.setText(Integer.toString(part1.getMax()));
            this.companyNameField.setText(Integer.toString(part1.getMachineId()));
        }
        //if outsourced selected
        else if (parts instanceof OutSourcedPart){
            OutSourcedPart part2 = (OutSourcedPart) parts;
            outSourcedRadio.setSelected(true);
            isPartOutsourced = true;
            companyNameLabel2.setText("Company Name");
            idField.setText("ID:" + partID);
            idField.setDisable(true);

            this.nameField.setText(part2.getPartName());
            this.idField.setText(Integer.toString(part2.getPartID()));
            this.stockField.setText(Integer.toString(part2.getStock()));
            this.costField.setText(Double.toString(part2.getCost()));
            this.minField.setText(Integer.toString(part2.getMin()));
            this.maxField.setText(Integer.toString(part2.getMax()));
            this.companyNameField.setText((part2.getCompanyName()));
    }
    }

    @FXML
    private void clearTextField(ActionEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
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

                InHousePart inPart2 = new InHousePart(partID, partName, Integer.parseInt(partStock), Double.parseDouble(partCost), Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partSource));

                inv.updatePart(inPart2);

            } else if (isPartOutsourced) {

                OutSourcedPart outPart2 = new OutSourcedPart(partID, partName, Integer.parseInt(partStock), Double.parseDouble(partCost), Integer.parseInt(partMin), Integer.parseInt(partMax), partSource);

                inv.updatePart(outPart2);

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



}

