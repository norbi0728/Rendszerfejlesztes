package marketplace.client;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
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
    public Listing listing;

    private Runnable onBid;

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
    private TextField highestBidField;
    private Button makeBidButton;
    private Button sellerInformationButton;
    private Button highestBidderButton;


    public ListingDisplay(Listing listing) {
        this.listing = listing;
        Platform.runLater(() -> init());
        refreshPeriodically();
    }

    private GridPane gridPane;
    private Label titleLabel;
    private void init() {
        titleLabel = new Label();
        titleLabel.getStyleClass().add("title-label");

        getChildren().add(titleLabel);

        gridPane = new GridPane();
        //gridPane.setGridLinesVisible(true);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40)); // TODO maybe not here
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        //gridPane.setGridLinesVisible(true);

        nameField = new TextField();
        nameField.setEditable(false);
        gridPane.add(new Label("Cikk megnevez�se:"), 0, 1);
        gridPane.add(nameField, 1, 1, 4, 1);

        Label descriptionLabel = new Label("Le�r�s:");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        gridPane.add(descriptionLabel, 0, 2);

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setPrefRowCount(3);
        gridPane.add(descriptionArea, 1, 2, 4, 1);

        imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        GridPane.setValignment(imageView, VPos.TOP);
        gridPane.add(imageView, 1, 3, 1, 2);

        Label featureHeader = new Label("Jellemz�k");
        GridPane.setHalignment(featureHeader, HPos.CENTER);
        gridPane.add(featureHeader, 2, 3, 4, 1);

        featureLines = new FeatureLines();
        featureLines.setSpacing(10);
        gridPane.add(featureLines, 2, 4, 4, 1);

        gridPane.add(new Label("Darabsz�m:"), 0, 5);
        quantityField = new TextField();
        quantityField.setEditable(false);
        gridPane.add(quantityField, 1, 5);

        gridPane.add(new Label("Fix �r:"), 3, 5);
        fixedPriceField = new TextField();
        fixedPriceField.setEditable(false);
        CurrencyChanger.getInstance().addTextField(fixedPriceField);
        gridPane.add(fixedPriceField, 4, 5);
        Label fixedPriceCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(fixedPriceCurrencyLabel);
        gridPane.add(fixedPriceCurrencyLabel, 5, 5);

        gridPane.add(new Label("Lej�rati id�:"), 0, 7);
        expirationDatePicker = new DatePicker();
        expirationDatePicker.setEditable(false);
        gridPane.add(expirationDatePicker, 1, 7);

        gridPane.add(new Label("Kateg�ria:"), 3, 7);
        categoryField = new TextField();
        categoryField.setEditable(false);
        gridPane.add(categoryField, 4, 7);

        gridPane.add(new Label("Kezd��r:"), 0, 8);
        startingBidField = new TextField();
        startingBidField.setEditable(false);
        gridPane.add(startingBidField, 1, 8);
        Label startingBidCurrencyLabel = new Label();
        CurrencyChanger.getInstance().addCurrencyLabel(startingBidCurrencyLabel);
        gridPane.add(startingBidCurrencyLabel, 2, 8);

        gridPane.add(new Label("L�p�sk�z:"), 3, 8);
        incrementField = new TextField();
        incrementField.setEditable(false);
        CurrencyChanger.getInstance().addTextField(incrementField);
        gridPane.add(incrementField, 4, 8);
        Label incrementCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(incrementCurrencyLabel);
        gridPane.add(incrementCurrencyLabel, 5, 8);

        gridPane.add(new Label("Fizet�si m�d:"), 0, 9);
        paymentMethodField = new TextField();
        paymentMethodField.setEditable(false);
        gridPane.add(paymentMethodField, 1, 9);

        gridPane.add(new Label("Sz�ll�t�si m�d:"), 3, 9);
        shippingMethodChoiceBox = new TextField();
        shippingMethodChoiceBox.setEditable(false);
        gridPane.add(shippingMethodChoiceBox, 4, 9);

        makeBidButton = new Button("Licit�l�s");
        makeBidButton.setOnAction(event -> {
            onBid.run();
        });
        gridPane.add(makeBidButton, 1, 10);

        sellerInformationButton = new Button("Hirdet� adatai");
        sellerInformationButton.setOnAction(event -> {
            sellerPopup();
        });
        gridPane.add(sellerInformationButton, 4, 10);

        gridPane.add(new Label("Legmagasabb licit:"), 1, 11);
        highestBidField = new TextField();

        highestBidField.setEditable(false);
        gridPane.add(highestBidField, 2, 11);

        highestBidderButton = new Button("Lagmagasabb licit�l� adatai");
        highestBidderButton.setOnAction(event -> {
            buyerPopup();
        });
        gridPane.add(highestBidderButton, 2, 12);

        fillInValues();
        getChildren().add(gridPane);
    }

    private void fillInValues() {
        titleLabel.setText(listing.getTitle());
        nameField.setText(listing.getItem().getName());
        descriptionArea.setText(listing.getDescription());
        if (listing.getItem().getPictures().size() > 0) {
            imageView.setImage(listing.getItem().getPictures().get(0).asImage());
        }

        featureLines.removeLines();
        for (Map.Entry<String, String> feature : listing.getItem().getFeatures().entrySet()) {
            featureLines.addLine(feature.getKey(), feature.getValue());
        }

        quantityField.setText(String.valueOf(listing.getQuantity()));
        fixedPriceField.setText(CurrencyChanger.getInstance().inChosenCurrency(listing.getFixedPrice()));
        expirationDatePicker.setValue(listing.getExpirationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        categoryField.setText(Category.forName(listing.getItem().getCategory()).toString());
        startingBidField.setText(CurrencyChanger.getInstance().inChosenCurrency(listing.getStartingBid()));
        if (listing.getStartingBid() == 0) {
            makeBidButton.setDisable(true);
        } else {
            makeBidButton.setDisable(false);
        }
        incrementField.setText(CurrencyChanger.getInstance().inChosenCurrency(listing.getIncrement()));
        paymentMethodField.setText(PaymentMethod.forName(listing.getPaymentMethod()).toString());
        shippingMethodChoiceBox.setText(ShippingMethod.forName(listing.getShippingMethod()).toString());

        Bid mostRecentBid = listing.mostRecentBid();
        if (mostRecentBid != null) {
            String who = mostRecentBid.getUserName().equals(RestClient.getRestClient().name) ? " (saj�t)" : " (m�s�)";
            String chosenCurrency = CurrencyChanger.getInstance().currency.toString();
            highestBidField.setText(CurrencyChanger.getInstance().inChosenCurrency(listing.mostRecentBid().getValue()) + " " + chosenCurrency + who);
            highestBidderButton.setDisable(false);
        } else {
            highestBidderButton.setDisable(true);
        }
    }

    private void sellerPopup() {
        personalInformationPopup(listing.getAdvertiser(), "Hirdet� adatai");
    }

    private void buyerPopup() {
        personalInformationPopup(listing.mostRecentBid().getUserName(), "Legmagasabb licit�l� adatai");
    }

    private void personalInformationPopup(String name, String message) {
        new Thread(() -> {
            StringBuilder sellerDetails = new StringBuilder();
            PersonalInformation personalInformation = RestClient.getRestClient().getPersonalInformation(name);
            sellerDetails.append(personalInformation.getLastName() + " " + personalInformation.getFirstName() + "\n");
            sellerDetails.append(personalInformation.getAddress() + "\n");
            sellerDetails.append(personalInformation.getEmail() + "\n");
            sellerDetails.append(personalInformation.getPhone() + "\n");
            Platform.runLater(() -> {
                Alert popup = new Alert(Alert.AlertType.INFORMATION);
                popup.setHeaderText(message);
                Label label = new Label(sellerDetails.toString());
                label.setWrapText(true);
                popup.getDialogPane().setContent(label);
                popup.show();
            });
        }).start();
    }

    public void setOnBid(Runnable onBid) {
        this.onBid = onBid;
    }

    public void refresh() {
        new Thread(() -> {
            Listing listing = RestClient.getRestClient().getListingById(this.listing.getId());
            if (listing == null) {
                Platform.runLater(() -> {
                    getChildren().remove(titleLabel);
                    getChildren().remove(gridPane);
                    stop = true;
                });
                return;
            }
            Platform.runLater(() -> {
                this.listing = listing;
                fillInValues();
            });
        }).start();
    }

    private boolean stop = false;
    private void refreshPeriodically() {
        if (stop) return;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                refresh();
            });
            refreshPeriodically();
        });
        thread.setDaemon(true);
        thread.start();
    }

    class FeatureLines extends VBox {
        private List<Pair<TextField, TextField>> featurePairs = new ArrayList<>();
        private List<HBox> lines = new ArrayList<>();

        void removeLines() {
            for (HBox line : lines) {
                getChildren().remove(line);
            }
            lines = new ArrayList<>();
        }

        void addLine(String key, String value) {
            HBox line = new HBox();
            line.setSpacing(10);

            TextField propertyNameField = new TextField(key);
            propertyNameField.setEditable(false);
            TextField propertyValueField = new TextField(value);
            propertyValueField.setEditable(false);
            featurePairs.add(new Pair<>(propertyNameField, propertyValueField));

            line.getChildren().add(new Label("Jellemz�:"));
            line.getChildren().add(propertyNameField);
            line.getChildren().add(new Label("�rt�k:"));
            line.getChildren().add(propertyValueField);

            lines.add(line);

            getChildren().add(line);
        }
    }
}