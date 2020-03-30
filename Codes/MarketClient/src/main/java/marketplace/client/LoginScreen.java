package marketplace.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private LoginController controller = new LoginController(this);

    TextField nameField;
    TextField passwordField;
    Button loginButton;
    Button registerButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        nameField = new TextField();
        passwordField = new TextField();
        loginButton = new Button("Belépés");
        registerButton = new Button("Regisztráció");

        loginButton.setOnAction(event -> controller.loginPressed());

        HBox nameLine = new HBox();
        nameLine.getChildren().add(new Label("Név:"));
        nameLine.getChildren().add(nameField);

        HBox passwordLine = new HBox();
        passwordLine.getChildren().add(new Label("Jelszó:"));
        passwordLine.getChildren().add(passwordField);

        VBox root = new VBox();
        root.getChildren().add(nameLine);
        root.getChildren().add(passwordLine);
        root.getChildren().add(loginButton);
        root.getChildren().add(registerButton);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Belépés a piacra");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
