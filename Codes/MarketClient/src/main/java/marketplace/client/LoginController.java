package marketplace.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class LoginController {
    private LoginScreen loginScreen;
    private RestClient restClient;

    LoginController(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        this.restClient = new RestClient();
    }

    public void loginPressed() {
        String name = loginScreen.nameField.getText();
        String password = loginScreen.passwordField.getText();
        String passwordHash = hash(password);

        new Thread(() -> {
            String result = restClient.login(name, passwordHash);
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.INFORMATION, result).show();
            });
        }).start();
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

    private String hash(String s) {
        return String.valueOf(s.hashCode());
    }
}
