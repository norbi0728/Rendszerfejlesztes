package marketplace.client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {
    MainController controller = new MainController(this);
    BorderPane root;

    public void openNewListingForm() {

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

    void start() {
        setUp();
        Stage stage = new Stage();
        stage.setTitle("Piac");
        stage.setScene(new Scene(root, 800, 494));
        stage.show();
    }
}
