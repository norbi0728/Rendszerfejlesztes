package marketplace.client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {
    private MainController controller = new MainController(this);
    private RestClient restClient;
    private BorderPane root;
    private TextField titleField;

    public void openNewListingForm() {
        GridPane gridPane = new GridPane();

        Label header = new Label("Új hirdetés");
        titleField = new TextField();

        gridPane.add(header, 1, 0, 2, 1);
        gridPane.add(new Label("Cím:"), 0, 1);
        gridPane.add(titleField, 1, 1);

        GridPane featureGridPane = new GridPane();

        //jellemz?k
        gridPane.add(featureGridPane, 0,2,2,1);


        root.setCenter(gridPane);
    }

    private void setUp() {
        root = new BorderPane();
        Button newListingButton = new Button("Új hirdetés");
        newListingButton.setOnAction(event -> {
            controller.newListingButtonClicked();
        });

        VBox menuButtons = new VBox();
        menuButtons.getChildren().add(newListingButton);

        root.setLeft(menuButtons);
    }

    void start(RestClient restClient) {
        this.restClient = restClient;
        setUp();
        Stage stage = new Stage();
        stage.setTitle("Piac");
        stage.setScene(new Scene(root, 800, 494));
        stage.show();
    }
}
