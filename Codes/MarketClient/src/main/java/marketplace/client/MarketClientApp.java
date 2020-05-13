package marketplace.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.Listing;
import marketplace.client.currencycomponents.Currency;
import java.util.List;

public class MarketClientApp extends Application {
    Stage stage;
    private Scene scene;
    private BorderPane root;
    private MainController controller;

    private NewListingForm newListingForm;
    private Region userListingsPane;
    private FlowPane allListingsPane;
    private Pane settingsPane;

    @Override
    public void init() throws Exception {
        controller = new MainController(this);
        root = new BorderPane();
        root.setStyle("-fx-background-color: orange;");
        root.setLeft(createMenu());
    }

    VBox createMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setSpacing(10);
        menu.setPadding(new Insets(40, 40, 40, 40));

        Button newListingButton = createMenuButton("�j hirdet�s", event -> {
            controller.newListingButtonClicked();
        });

        Button userListingsButton = createMenuButton("Saj�t hirdet�seim", event -> {
            controller.userListingsButtonClicked();
        });

        Button allListingsButton = createMenuButton("Minden hirdet�s", event -> {
            controller.allListingsButtonClicked();
        });

        Button settingsButton = createMenuButton("Be�ll�t�s", event -> {
            openSettingsPane();
        });

        menu.getChildren().add(newListingButton);
        menu.getChildren().add(userListingsButton);
        menu.getChildren().add(allListingsButton);
        menu.getChildren().add(settingsButton);

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
        if (settingsPane == null) {
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
        ChoiceBox<Currency> currencyChoiceBox;

        gridPane.add(new Label("Keresztn�v:"), 0, 3);
        gridPane.add(firstNameField, 1, 3);

        gridPane.add(new Label("Csal�dn�v:"), 0, 4);
        gridPane.add(lastNameField, 1, 4);

        gridPane.add(new Label("Lakc�m:"), 0, 5);
        gridPane.add(addressField, 1, 5);

        gridPane.add(new Label("Telefon:"), 0, 6);
        gridPane.add(phoneField, 1, 6);

        gridPane.add(new Label("E-mail:"), 0, 7);
        gridPane.add(emailField, 1, 7);

        gridPane.add(new Label("P�nznem:"), 2, 3);
        currencyChoiceBox = new ChoiceBox<>();
        currencyChoiceBox.getItems().setAll(Currency.values());
        currencyChoiceBox.setValue(CurrencyChanger.getInstance().currency);
        currencyChoiceBox.setOnAction((event -> {
            CurrencyChanger.getInstance().changeDisplayCurrency(currencyChoiceBox.getValue());
        }));
        gridPane.add(currencyChoiceBox, 3, 3);

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
        userListingsPane.setStyle("-fx-background-color: purple;");
        root.setCenter(userListingsPane);
        BorderPane.setAlignment(userListingsPane, Pos.CENTER);
    }

    public void openAllListingsPane() {
        if (allListingsPane == null) allListingsPane = createAllListingsPanel();
    }

    private FlowPane createAllListingsPanel() {
        FlowPane flowPane = new FlowPane();

        return flowPane;
    }

    private Region createUserListingsPanel() {
        VBox vBox = new VBox();
        //vBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //vBox.setPrefSize(1000, 1000);
        vBox.setStyle("-fx-background-color: green;");
        //vBox.setPrefWidth(1000000);
        vBox.getChildren().add(new Label("Saj�t hirdet�sek"));
        FlowPane flowPane = new FlowPane();

        new Thread(() -> {
            List<Listing> userListings = controller.getUserListings();
            Platform.runLater(() -> {
                for (Listing userListing : userListings) {
                    SmallListingView smallListingView = new SmallListingView(userListing);
                    smallListingView.setPadding(new Insets(10, 10, 10, 10)); //TODO not here
                    flowPane.getChildren().add(smallListingView);
                }
            });
        }).start();

        vBox.getChildren().add(flowPane);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
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
