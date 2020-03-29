package marketplace.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private LoginController controller = new LoginController(this);



    @Override
    public void start(Stage primaryStage) throws Exception {


        HBox nameLine = new HBox();
        nameLine.getChildren().add(new Label("Név:"));


        VBox root = new VBox();
        root.getChildren().add(nameLine);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Belépés a piacra");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
