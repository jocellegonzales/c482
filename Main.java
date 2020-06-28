import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception{
        Inventory inv = new Inventory();
        addSampleData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/mainScreen.fxml"));
        View_Controller.mainScreenController controller = new View_Controller.mainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    private void addSampleData(Inventory inv){
        Parts a1 = new InHousePart(1, "Part A1", 10, 2.99, 5, 100, 101);
        inv.addParts(a1);
        Parts o1 = new OutSourcedPart(6, "Part O1", 10, 2.99, 5, 100, "ACME Co.");
        inv.addParts(o1);
        Products prod1 = new Products(100, "Product 1", 10, 2.99, 5, 100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(o1);
        inv.addProducts(prod1);
    }





}
