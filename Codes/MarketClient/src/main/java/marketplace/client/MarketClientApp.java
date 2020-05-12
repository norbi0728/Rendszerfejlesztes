package marketplace.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class MarketClientApp extends Application {
    Stage stage;
    private Scene scene;
    private BorderPane root;
    private MainController controller;

    private NewListingForm newListingForm;
    private Pane userListingsPane;
    private FlowPane allListingsPane;
    private Pane settingsPane;

    @Override
    public void init() throws Exception {
        controller = new MainController(this);
        root = new BorderPane();
        root.setLeft(createMenu());
    }

    VBox createMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setSpacing(10);
        menu.setPadding(new Insets(40, 40, 40, 40));

        Button newListingButton = createMenuButton("Új hirdetés", event -> {
            controller.newListingButtonClicked();
        });

        Button userListingsButton = createMenuButton("Saját hirdetéseim", event -> {
            controller.userListingsButtonClicked();
        });

        Button allListingsButton = createMenuButton("Minden hirdetés", event -> {
            controller.allListingsButtonClicked();
        });

        Button settingsButton = createMenuButton("Beállítás", event -> {
            openSettingsPane();
        });

        menu.getChildren().add(newListingButton);
        menu.getChildren().add(userListingsButton);
        menu.getChildren().add(allListingsButton);

        return menu;
    }

    private Button createMenuButton(String title, EventHandler<ActionEvent> handler) {
        Button button = new Button(title);
        button.setOnAction(handler);
        VBox.setVgrow(button, Priority.ALWAYS);
        button.getStyleClass().add("menu-button");

        return button;
    }

    private void openSettingsPane() {
        if (settingsPane != null) {
            settingsPane = createSettingsPane();
        }
        root.setCenter(settingsPane);
    }

    private Pane createSettingsPane() {
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();




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

        vBox.getChildren().add(gridPane);
        return vBox;
    }

    public void openNewListingForm() {
        if (newListingForm == null) {
            newListingForm = new NewListingForm(stage);
            newListingForm.setAlignment(Pos.TOP_CENTER);
            newListingForm.setOnNewListingReady((newListing) -> {
                controller.addListing(newListing);
            });
        }
        root.setCenter(newListingForm);
    }

    public void openUserListingsPane() {
        if (userListingsPane == null) userListingsPane = createUserListingsPanel();
        root.setCenter(userListingsPane);
    }

    public void openAllListingsPane() {
        if (allListingsPane == null) allListingsPane = createAllListingsPanel();
    }

    private FlowPane createAllListingsPanel() {
        FlowPane flowPane = new FlowPane();

        return flowPane;
    }

    private Pane createUserListingsPanel() {
        Pane pane = new Pane();
        VBox vBox = new VBox();
        vBox.getChildren().add(new Label("User Listings"));
        FlowPane flowPane = new FlowPane();

        new Thread(() -> {
            List<Listing> userListings = controller.getUserListings();
            Platform.runLater(() -> {
                for (Listing userListing : userListings) {
                    flowPane.getChildren().add(new SmallListingView(userListing));
                }
            });
        }).start();

        vBox.getChildren().add(flowPane);
        pane.getChildren().add(vBox);
        return pane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Piac");
        scene = new Scene(root, 1600, 900, Color.web("#f4f4f4"));
        scene.getStylesheets().add("main_style.css");
        stage.setScene(scene);

        new LoginScreen(this).start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
