package marketplace.client;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class NewListingForm extends VBox {
    private Image image;
    private Stage stage;
    private Consumer<Listing> onNewListingReady;

    private TextField titleField;
    private TextField nameField;
    private TextArea descriptionArea;
    private ImageView imageView;
    private TextField quantityField;
    private DatePicker expirationDatePicker;
    private ChoiceBox<Category> categoryChoiceBox;
    private FeatureLines featureLines;
    private TextField startingBidField;
    private TextField incrementField;
    private TextField fixedPriceField;
    private ChoiceBox<PaymentMethod> paymentMethodChoiceBox;
    private ChoiceBox<ShippingMethod> shippingMethodChoiceBox;


    public NewListingForm(Stage stage) {
        this.stage = stage;
        Platform.runLater(() -> init());
    }

    private void init() {
        Label titleLabel = new Label("Új hirdetés létrehozása");
        titleLabel.getStyleClass().add("title-label");

        getChildren().add(titleLabel);

        GridPane gridPane = new GridPane();
        //gridPane.setGridLinesVisible(true);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        titleField = new TextField();
        gridPane.add(new Label("Cím:"), 0, 0);
        gridPane.add(titleField, 1, 0, 4, 1);

        nameField = new TextField();
        gridPane.add(new Label("Cikk megnevezése:"), 0, 1);
        gridPane.add(nameField, 1, 1, 4, 1);

        Label descriptionLabel = new Label("Leírás:");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        gridPane.add(descriptionLabel, 0, 2);

        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        gridPane.add(descriptionArea, 1, 2, 4, 1);

        imageView = new ImageView();
        imageView.setImage(new Image("Photo-icon.png"));
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        GridPane.setValignment(imageView, VPos.TOP);
        gridPane.add(imageView, 1, 3, 1, 2);

        Button newImageButton = new Button("Kép kiválasztása");
        newImageButton.setOnAction((event -> {
            chooseImage();
            imageView.setImage(image);
        }));
        gridPane.add(newImageButton, 0, 3);

        Button addFeatureLineButton = new Button("Új jellemzõ hozzáadása");
        addFeatureLineButton.setOnAction((event -> {
            featureLines.addLine();
        }));
        GridPane.setHalignment(addFeatureLineButton, HPos.CENTER);
        gridPane.add(addFeatureLineButton, 2, 3, 4, 1);

        featureLines = new FeatureLines();
        featureLines.setSpacing(10);
        featureLines.addLine();
        gridPane.add(featureLines, 2, 4, 4, 1);

        gridPane.add(new Label("Darabszám:"), 0, 5);
        quantityField = new TextField();
        gridPane.add(quantityField, 1, 5);

        gridPane.add(new Label("Fix ár:"), 3, 5);
        fixedPriceField = new TextField();
        gridPane.add(fixedPriceField, 4, 5);

        gridPane.add(new Label("Lejárati idõ:"), 0, 7);
        expirationDatePicker = new DatePicker();
        gridPane.add(expirationDatePicker, 1, 7);

        gridPane.add(new Label("Kategória:"), 3, 7);
        categoryChoiceBox = new ChoiceBox<>();
        categoryChoiceBox.getItems().setAll(Category.values());
        gridPane.add(categoryChoiceBox, 4, 7);

        gridPane.add(new Label("Kezdõár:"), 0, 8);
        startingBidField = new TextField();
        gridPane.add(startingBidField, 1, 8);
        gridPane.add(new Label("Ft"), 2, 8);

        gridPane.add(new Label("Lépésköz:"), 3, 8);
        incrementField = new TextField();
        gridPane.add(incrementField, 4, 8);
        gridPane.add(new Label("Ft"), 5, 8);

        gridPane.add(new Label("Fizetési mód:"), 0, 9);
        paymentMethodChoiceBox = new ChoiceBox<>();
        paymentMethodChoiceBox.getItems().setAll(PaymentMethod.values());
        gridPane.add(paymentMethodChoiceBox, 1, 9);

        gridPane.add(new Label("Szállítási mód:"), 3, 9);
        shippingMethodChoiceBox = new ChoiceBox<>();
        shippingMethodChoiceBox.getItems().setAll(ShippingMethod.values());
        gridPane.add(shippingMethodChoiceBox, 4, 9);

        Button newListingOKButton = new Button("Új hirdetés feladása");
        newListingOKButton.setOnAction((event) -> {
            Listing newListing = compileNewListing();
            onNewListingReady.accept(newListing);
        });
        gridPane.add(newListingOKButton, 0, 10);

        getChildren().add(gridPane);
    }

    private Listing compileNewListing() {
        List<Picture> pictures = new ArrayList<>();
        if (image != null) {
            Picture picture = new Picture(image);
            pictures.add(picture);
        }
        Item item = new Item(
                nameField.getText(),
                featureLines.features(),
                pictures,
                categoryChoiceBox.getValue().getEnglishName()
        );
        Listing newListing = new Listing(
                -1,
                titleField.getText(),
                descriptionArea.getText(),
                Integer.valueOf(quantityField.getText()),
                item,
                RestClient.getRestClient().name,
                Integer.valueOf(incrementField.getText()),
                -1,
                Integer.valueOf(startingBidField.getText()),
                Integer.valueOf(fixedPriceField.getText()),
                Date.from(expirationDatePicker.getValue().atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()),
                new Date(),
                new Date(System.currentTimeMillis() + 1000 * 3600 * 24 * 365),
                paymentMethodChoiceBox.getValue().getEnglishName(),
                shippingMethodChoiceBox.getValue().getEnglishName(),
                new ArrayList<Bid>()
        );

        return newListing;
    }

    public void setOnNewListingReady(Consumer<Listing> consumer) {
        this.onNewListingReady = consumer;
    }

    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Új kép megnyitása");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File imageFile = fileChooser.showOpenDialog(stage);
        try {
            image = new Image(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    class FeatureLines extends VBox {
        private List<Pair<TextField, TextField>> featureLines = new ArrayList<>();

        void addLine() {
            HBox line = new HBox();
            line.setSpacing(10);

            TextField propertyNameField = new TextField();
            TextField propertyValueField = new TextField();
            featureLines.add(new Pair<>(propertyNameField, propertyValueField));

            line.getChildren().add(new Label("Jellemzõ:"));
            line.getChildren().add(propertyNameField);
            line.getChildren().add(new Label("Érték:"));
            line.getChildren().add(propertyValueField);

            Button deleteButton = new Button("X");
            deleteButton.setOnAction((event) -> {
                getChildren().remove(line);
                featureLines.remove(line);
            });
            line.getChildren().add(deleteButton);

            getChildren().add(line);
        }

        Map<String, String> features() {
            Map<String, String> features = new HashMap<>();
            for (Pair<TextField, TextField> featureLine : featureLines) {
                if (!featureLine.getKey().getText().equals("")) {
                    features.put(featureLine.getKey().getText(), featureLine.getValue().getText());
                }
            }
            return features;
        }

    }
}