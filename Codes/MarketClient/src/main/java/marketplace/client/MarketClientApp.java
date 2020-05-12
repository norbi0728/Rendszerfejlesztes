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
    Pane userListingsPane;
    private FlowPane allListingsPane;

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

        menu.getChildren().add(newListingButton);
        menu.getChildren().add(userListingsButton);

        return menu;
    }

    private Button createMenuButton(String title, EventHandler<ActionEvent> handler) {
        Button button = new Button(title);
        button.setOnAction(handler);
        VBox.setVgrow(button, Priority.ALWAYS);
        button.getStyleClass().add("menu-button");

        return button;
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
