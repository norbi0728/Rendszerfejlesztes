package marketplace.client;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.*;

import java.time.ZoneId;
import java.util.*;

public class ListingDisplay extends VBox {
    private Listing listing;

    private Runnable onBid;

    private Image image;
    private TextField titleField;
    private TextField nameField;
    private TextArea descriptionArea;
    private ImageView imageView;
    private TextField quantityField;
    private DatePicker expirationDatePicker;
    private TextField categoryField;
    private FeatureLines featureLines;
    private TextField startingBidField;
    private TextField incrementField;
    private TextField fixedPriceField;
    private TextField paymentMethodField;
    private TextField shippingMethodChoiceBox;
    private Button makeBidButton;
    private Button buyForFixedPriceButton;


    public ListingDisplay(Listing listing) {
        this.listing = listing;
        Platform.runLater(() -> init());
    }

    private void init() {

        Label titleLabel = new Label(listing.getTitle());
        titleLabel.getStyleClass().add("title-label");

        getChildren().add(titleLabel);

        GridPane gridPane = new GridPane();
        //gridPane.setGridLinesVisible(true);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40)); // TODO maybe not here
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        //gridPane.setGridLinesVisible(true);

        nameField = new TextField();
        nameField.setEditable(false);
        nameField.setText(listing.getItem().getName());
        gridPane.add(new Label("Cikk megnevezése:"), 0, 1);
        gridPane.add(nameField, 1, 1, 4, 1);

        Label descriptionLabel = new Label("Leírás:");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        gridPane.add(descriptionLabel, 0, 2);

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setText(listing.getDescription());
        gridPane.add(descriptionArea, 1, 2, 4, 1);

        imageView = new ImageView();
        imageView.setImage(listing.getItem().getPictures().get(0).asImage());
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        GridPane.setValignment(imageView, VPos.TOP);
        gridPane.add(imageView, 1, 3, 1, 2);

        Label featureHeader = new Label("Jellemzõk");
        GridPane.setHalignment(featureHeader, HPos.CENTER);
        gridPane.add(featureHeader, 2, 3, 4, 1);

        featureLines = new FeatureLines();
        featureLines.setSpacing(10);
        for (Map.Entry<String, String> feature : listing.getItem().getFeatures().entrySet()) {
            featureLines.addLine(feature.getKey(), feature.getValue());
        }
        gridPane.add(featureLines, 2, 4, 4, 1);

        gridPane.add(new Label("Darabszám:"), 0, 5);
        quantityField = new TextField(String.valueOf(listing.getQuantity()));
        quantityField.setEditable(false);
        gridPane.add(quantityField, 1, 5);

        gridPane.add(new Label("Fix ár:"), 3, 5);
        fixedPriceField = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing.getFixedPrice()));
        fixedPriceField.setEditable(false);
        CurrencyChanger.getInstance().addTextField(fixedPriceField);
        gridPane.add(fixedPriceField, 4, 5);
        Label fixedPriceCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(fixedPriceCurrencyLabel);
        gridPane.add(fixedPriceCurrencyLabel, 5, 5);

        gridPane.add(new Label("Lejárati idõ:"), 0, 7);
        expirationDatePicker = new DatePicker(listing.getExpirationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        expirationDatePicker.setEditable(false);
        gridPane.add(expirationDatePicker, 1, 7);

        gridPane.add(new Label("Kategória:"), 3, 7);
        categoryField = new TextField(listing.getItem().getCategory());
        categoryField.setEditable(false);
        gridPane.add(categoryField, 4, 7);

        gridPane.add(new Label("Kezdõár:"), 0, 8);
        startingBidField = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing.getStartingBid()));
        startingBidField.setEditable(false);
        gridPane.add(startingBidField, 1, 8);
        Label startingBidCurrencyLabel = new Label();
        CurrencyChanger.getInstance().addCurrencyLabel(startingBidCurrencyLabel);
        gridPane.add(startingBidCurrencyLabel, 2, 8);

        gridPane.add(new Label("Lépésköz:"), 3, 8);
        incrementField = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing.getIncrement()));
        incrementField.setEditable(false);
        CurrencyChanger.getInstance().addTextField(incrementField);
        gridPane.add(incrementField, 4, 8);
        Label incrementCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(incrementCurrencyLabel);
        gridPane.add(incrementCurrencyLabel, 5, 8);

        gridPane.add(new Label("Fizetési mód:"), 0, 9);
        paymentMethodField = new TextField();
        paymentMethodField.setEditable(false);
        paymentMethodField.setText(listing.getPaymentMethod());
        gridPane.add(paymentMethodField, 1, 9);

        gridPane.add(new Label("Szállítási mód:"), 3, 9);
        shippingMethodChoiceBox = new TextField();
        shippingMethodChoiceBox.setEditable(false);
        shippingMethodChoiceBox.setText(listing.getShippingMethod());
        gridPane.add(shippingMethodChoiceBox, 4, 9);

        makeBidButton = new Button("Licitálás");
        makeBidButton.setOnAction(event -> {
            onBid.run();
        });
        gridPane.add(makeBidButton, 1, 10);

        buyForFixedPriceButton = new Button("Megveszem a fix áron");
        gridPane.add(buyForFixedPriceButton, 4, 10);

        getChildren().add(gridPane);
    }

    public void setOnBid(Runnable onBid) {
        this.onBid = onBid;
    }

    class FeatureLines extends VBox {
        private List<Pair<TextField, TextField>> featureLines = new ArrayList<>();

        void addLine(String key, String value) {
            HBox line = new HBox();
            line.setSpacing(10);

            TextField propertyNameField = new TextField(key);
            propertyNameField.setEditable(false);
            TextField propertyValueField = new TextField(value);
            propertyValueField.setEditable(false);
            featureLines.add(new Pair<>(propertyNameField, propertyValueField));

            line.getChildren().add(new Label("Jellemzõ:"));
            line.getChildren().add(propertyNameField);
            line.getChildren().add(new Label("Érték:"));
            line.getChildren().add(propertyValueField);

            getChildren().add(line);
        }
    }
}