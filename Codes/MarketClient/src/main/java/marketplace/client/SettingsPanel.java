package marketplace.client;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import marketplace.client.currencycomponents.Currency;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.PersonalInformation;

public class SettingsPanel extends VBox {
    private PersonalInformation ownPersonalInformation;
    MainController controller;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField addressField;
    private TextField phoneField;
    private TextField emailField;
    private ChoiceBox<Currency> currencyChoiceBox;
    private Button saveButton;

    public SettingsPanel(MainController controller) {
        this.controller = controller;
        new Thread(() -> {
            ownPersonalInformation = controller.getOwnPersonalInformation();
            Platform.runLater(() -> {
                init();
            });
        }).start();
    }

    private void init() {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("settings");
        //gridPane.setGridLinesVisible(true);

        firstNameField = new TextField();
        lastNameField = new TextField();
        addressField = new TextField();
        phoneField = new TextField();
        emailField = new TextField();

        gridPane.add(new Label("Keresztnév:"), 0, 3);
        gridPane.add(firstNameField, 1, 3);

        gridPane.add(new Label("Családnév:"), 0, 4);
        gridPane.add(lastNameField, 1, 4);

        gridPane.add(new Label("Lakcím:"), 0, 5);
        gridPane.add(addressField, 1, 5);

        gridPane.add(new Label("Telefon:"), 0, 6);
        gridPane.add(phoneField, 1, 6);

        gridPane.add(new Label("E-mail:"), 0, 7);
        gridPane.add(emailField, 1, 7);

        gridPane.add(new Label("Pénznem:"), 2, 3);
        currencyChoiceBox = new ChoiceBox<>();
        currencyChoiceBox.getItems().setAll(Currency.values());
        currencyChoiceBox.setValue(CurrencyChanger.getInstance().currency);
        currencyChoiceBox.setOnAction((event -> {
            CurrencyChanger.getInstance().changeDisplayCurrency(currencyChoiceBox.getValue());
        }));
        gridPane.add(currencyChoiceBox, 3, 3);

        saveButton = new Button("Mentés");
        saveButton.setOnAction(event -> {
            PersonalInformation newPersonalInformation = new PersonalInformation(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    currencyChoiceBox.getValue().toString()
            );
            new Thread(() -> {
                controller.setPersonalInformation(newPersonalInformation);
            }).start();
        });
        gridPane.add(saveButton, 0, 8);
        fillInFields();
        getChildren().add(gridPane);
    }

    private void fillInFields() {
        firstNameField.setText(ownPersonalInformation.getFirstName());
        lastNameField.setText(ownPersonalInformation.getLastName());
        addressField.setText(ownPersonalInformation.getAddress());
        phoneField.setText(ownPersonalInformation.getPhone());
        emailField.setText(ownPersonalInformation.getEmail());
        if (ownPersonalInformation.getPreferredCurrency() != null) {
            currencyChoiceBox.setValue(Currency.forName(ownPersonalInformation.getPreferredCurrency()));
        } else {
            currencyChoiceBox.setValue(Currency.HUF);
        }
    }
}
