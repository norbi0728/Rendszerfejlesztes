package marketplace.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.beans.PropertyVetoException;

public class LoginScreen extends Application {
    private LoginController controller = new LoginController(this);

    Scene scene;

    TextField nameField;
    TextField passwordField;
    Button loginButton;
    Button registerButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label nameLabel = new Label("Név:");
        Label passwordLabel = new Label("Jelszó:");
        nameField = new TextField();
        passwordField = new TextField();
        limitText(nameField);
        limitText(passwordField);
        loginButton = new Button("Belépés");
        registerButton = new Button("Regisztráció");

        loginButton.setOnAction(event -> controller.loginPressed());
        registerButton.setOnAction(event -> controller.registerPressed());

        VBox labelBox = new VBox(20,
                nameLabel,
                passwordLabel
        );

        labelBox.setAlignment(Pos.BASELINE_RIGHT);
        labelBox.setPrefWidth(50);

        VBox inputFieldBox = new VBox(10,
                nameField,
                passwordField
        );

        labelBox.setLayoutX(60);
        labelBox.setLayoutY(47);

        inputFieldBox.setPrefWidth(200);
        inputFieldBox.setLayoutX(140);
        inputFieldBox.setLayoutY(45);

        loginButton.setLayoutX(90);
        loginButton.setLayoutY(170);

        registerButton.setLayoutX(240);
        registerButton.setLayoutY(170);

        Pane root = new Pane();
        root.getChildren().addAll(labelBox, inputFieldBox, loginButton, registerButton);

        scene = new Scene(root, 400, 250);
        scene.getStylesheets().add("my_style.css");

        primaryStage.setTitle("Belépés a piacra");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void limitText(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 20) {
                nameField.setText(oldValue);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
