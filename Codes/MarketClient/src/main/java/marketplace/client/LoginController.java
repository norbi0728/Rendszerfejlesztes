package marketplace.client;

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

        restClient.login(name, passwordHash);
    }

    private String hash(String s) {
        return String.valueOf(s.hashCode());
    }
}
