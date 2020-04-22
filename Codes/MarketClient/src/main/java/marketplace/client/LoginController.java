package marketplace.client;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController {
    private LoginScreen loginScreen;
    private RestClient restClient;

    LoginController(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        this.restClient = new RestClient();
    }

    public void loginPressed() {
        //For testing
        startMainWindow();

//        String name = loginScreen.nameField.getText();
//        String password = loginScreen.passwordField.getText();
//        String passwordHash = hash(password);
//
//        new Thread(() -> {
//            String result = restClient.login(name, passwordHash);
//            Platform.runLater(() -> {
//                new Alert(Alert.AlertType.INFORMATION, result).show();
//                //if (result.equals("Correct"))
//                {
//                    startMainWindow();
//                }
//            });
//        }).start();
    }

    public void registerPressed() {
        String name = loginScreen.nameField.getText();
        String password = loginScreen.passwordField.getText();
        String passwordHash = hash(password);

        new Thread(() -> {
            String result = restClient.register(name, passwordHash);
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.INFORMATION, result).show();
            });
        }).start();
    }

    private void startMainWindow() {
        loginScreen.app.stage.show();
        loginScreen.scene.getWindow().hide();
    }

    private String hash(String s) {
        return String.valueOf(s.hashCode());
    }
}
