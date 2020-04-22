package marketplace.client;

import javafx.application.Application;
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

public class MarketClientApp extends Application {
    Stage stage;
    private Scene scene;
    private BorderPane root;
    private MainController controller;

    private TextField titleField;
    private GridPane newListingForm;
    private GridPane userListingsPane;

    @Override
    public void init() throws Exception {
        controller = new MainController(this);
        root = new BorderPane();

        root.setLeft(createMenu());

    }

    VBox createMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        menu.setPadding(new Insets(40, 40, 40, 40));

        Button newListingButton = createMenuButton("Új hirdetés", event -> {
            controller.newListingButtonClicked();
        });

        Button userListingsButton = createMenuButton("Saját hirdetéseim", event -> {
            controller.userListingsButtonClicked();
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
        if (newListingForm == null) newListingForm = createNewListingForm();
        root.setCenter(newListingForm);
    }

    public void openUserListingsPane() {
        if (userListingsPane == null) userListingsPane = createUserListingsPanel();
        root.setCenter(userListingsPane);
    }

    private GridPane createNewListingForm() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label header = new Label("Új hirdetés");
        titleField = new TextField();

        gridPane.add(header, 1, 0, 2, 1);
        gridPane.add(new Label("Cím:"), 0, 1);
        gridPane.add(titleField, 1, 1);

        GridPane featureGridPane = new GridPane();

        //jellemz?k
        gridPane.add(featureGridPane, 0,2,2,1);

        Button newListingOKButton = new Button("OK");
        newListingOKButton.setOnAction((event) -> {

        });
        gridPane.add(newListingOKButton, 0, 3);

        return gridPane;
    }

    private GridPane createUserListingsPanel() {
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("User Listings"), 0, 0);
        return gridPane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Piac");
        scene = new Scene(root, 1024, 768, Color.web("#f4f4f4"));
        scene.getStylesheets().add("main_style.css");
        stage.setScene(scene);

        new LoginScreen(this).start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
