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
import marketplace.client.model.Listing;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListingComparatorDisplay extends ScrollPane {
    private Listing listing1;
    private Listing listing2;

    private Label title;

    private TextField nameField1;
    private TextArea descriptionArea1;
    private ImageView imageView1;
    private TextField quantityField1;
    private DatePicker expirationDatePicker1;
    private TextField categoryField1;
    private FeatureLines featureLines1;
    private TextField startingBidField1;
    private TextField incrementField1;
    private TextField fixedPriceField1;
    private TextField paymentMethodField1;
    private TextField shippingMethodChoiceBox1;

    private TextField nameField2;
    private TextArea descriptionArea2;
    private ImageView imageView2;
    private TextField quantityField2;
    private DatePicker expirationDatePicker2;
    private TextField categoryField2;
    private FeatureLines featureLines2;
    private TextField startingBidField2;
    private TextField incrementField2;
    private TextField fixedPriceField2;
    private TextField paymentMethodField2;
    private TextField shippingMethodChoiceBox2;

    private VBox left;
    private VBox right;

    public ListingComparatorDisplay(Listing listing1, Listing listing2){
        this.listing1 = listing1;
        this.listing2 = listing2;
        Platform.runLater(() -> init());
    }

    private void initLeft(){
        left = new VBox();
        left.setSpacing(10);

        nameField1 = new TextField();
        nameField1.setEditable(false);
        nameField1.setText(listing1.getItem().getName());
        HBox nameHbox = new HBox(new Label("Cikk megnevezése:"), nameField1);
        nameHbox.setSpacing(10);
        left.getChildren().add(nameHbox);

        Label descriptionLabel = new Label("Leírás:");

        descriptionArea1 = new TextArea();
        descriptionArea1.setEditable(false);
        descriptionArea1.setPrefRowCount(3);
        descriptionArea1.setPrefColumnCount(20);
        descriptionArea1.setText(listing1.getDescription());

        HBox descriptionHbox = new HBox(descriptionLabel, descriptionArea1);
        descriptionHbox.setSpacing(10);
        left.getChildren().add(descriptionHbox);

        imageView1 = new ImageView();
        imageView1.setImage(listing1.getItem().getPictures().get(0).asImage());
        imageView1.setFitHeight(150);
        imageView1.setFitWidth(150);
        imageView1.setPreserveRatio(true);

        left.getChildren().add(imageView1);

        Label featureHeader = new Label("Jellemzõk");
        left.getChildren().add(featureHeader);

        featureLines1 = new FeatureLines();
        featureLines2 = new FeatureLines();
        featureLines1.setSpacing(10);
        featureLines2.setSpacing(10);
        for (Map.Entry<String, String> feature1 : listing1.getItem().getFeatures().entrySet()) {
            for (Map.Entry<String, String> feature2 : listing2.getItem().getFeatures().entrySet()) {
                if (feature1.getKey().equals(feature2.getKey())) {
                    featureLines1.addLine(feature1.getKey(), feature1.getValue());
                    featureLines2.addLine(feature2.getKey(), feature2.getValue());
                }
            }

        }
        left.getChildren().add(featureLines1);

        quantityField1 = new TextField(String.valueOf(listing1.getQuantity()));
        quantityField1.setEditable(false);
        HBox quantityHbox = new HBox(new Label("Darabszám:"), quantityField1);
        quantityHbox.setSpacing(10);
        left.getChildren().add(quantityHbox);

        fixedPriceField1 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing1.getFixedPrice()));
        fixedPriceField1.setEditable(false);
        CurrencyChanger.getInstance().addTextField(fixedPriceField1);

        Label fixedPriceCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(fixedPriceCurrencyLabel);
        HBox fixedPriceHbox = new HBox(new Label("Fix ár:"), fixedPriceField1, fixedPriceCurrencyLabel);
        fixedPriceHbox.setSpacing(10);
        left.getChildren().add(fixedPriceHbox);

        expirationDatePicker1 = new DatePicker(listing1.getExpirationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        expirationDatePicker1.setEditable(false);
        HBox expirationDateHbox = new HBox(new Label("Lejárati idõ:"), expirationDatePicker1);
        expirationDateHbox.setSpacing(10);
        left.getChildren().add(expirationDateHbox);

        categoryField1 = new TextField(listing1.getItem().getCategory());
        categoryField1.setEditable(false);
        HBox categoryHbox = new HBox(new Label("Kategória:"), categoryField1);
        categoryHbox.setSpacing(10);
        left.getChildren().add(categoryHbox);

        startingBidField1 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing1.getStartingBid()));
        startingBidField1.setEditable(false);
        Label startingBidCurrencyLabel = new Label();
        CurrencyChanger.getInstance().addCurrencyLabel(startingBidCurrencyLabel);;

        HBox startingBidHbox = new HBox(new Label("Kezdõár:"), startingBidField1, startingBidCurrencyLabel);
        startingBidHbox.setSpacing(10);
        left.getChildren().add(startingBidHbox);


        incrementField1 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing1.getIncrement()));
        incrementField1.setEditable(false);
        CurrencyChanger.getInstance().addTextField(incrementField1);

        Label incrementCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(incrementCurrencyLabel);

        HBox incrementHbox = new HBox(new Label("Lépésköz:"), incrementField1, incrementCurrencyLabel);
        incrementHbox.setSpacing(10);
        left.getChildren().add(incrementHbox);

        paymentMethodField1 = new TextField();
        paymentMethodField1.setEditable(false);
        paymentMethodField1.setText(listing1.getPaymentMethod());

        HBox paymentMethodHbox = new HBox(new Label("Fizetési mód:"), paymentMethodField1);
        paymentMethodHbox.setSpacing(10);
        left.getChildren().add(paymentMethodHbox);

        shippingMethodChoiceBox1 = new TextField();
        shippingMethodChoiceBox1.setEditable(false);
        shippingMethodChoiceBox1.setText(listing1.getShippingMethod());

        HBox shippingMethodHbox = new HBox(new Label("Szállítási mód:"), shippingMethodChoiceBox1);
        shippingMethodHbox.setSpacing(10);
        left.getChildren().add(shippingMethodHbox);

    }
    private void initRight(){
        right = new VBox();
        right.setSpacing(10);

        nameField2 = new TextField();
        nameField2.setEditable(false);
        nameField2.setText(listing2.getItem().getName());
        HBox nameHbox = new HBox(new Label("Cikk megnevezése:"), nameField2);
        nameHbox.setSpacing(10);
        right.getChildren().add(nameHbox);

        Label descriptionLabel = new Label("Leírás:");

        descriptionArea2 = new TextArea();
        descriptionArea2.setEditable(false);
        descriptionArea2.setPrefColumnCount(20);
        descriptionArea2.setPrefRowCount(3);
        descriptionArea2.setText(listing2.getDescription());

        HBox descriptionHbox = new HBox(descriptionLabel, descriptionArea2);
        descriptionHbox.setSpacing(10);
        right.getChildren().add(descriptionHbox);

        imageView2 = new ImageView();
        imageView2.setImage(listing2.getItem().getPictures().get(0).asImage());
        imageView2.setFitHeight(150);
        imageView2.setFitWidth(150);
        imageView2.setPreserveRatio(true);

        right.getChildren().add(imageView2);

        Label featureHeader = new Label("Jellemzõk");
        right.getChildren().add(featureHeader);

        right.getChildren().add(featureLines2);

        quantityField2 = new TextField(String.valueOf(listing2.getQuantity()));
        quantityField2.setEditable(false);
        HBox quantityHbox = new HBox(new Label("Darabszám:"), quantityField2);
        quantityHbox.setSpacing(10);
        right.getChildren().add(quantityHbox);

        fixedPriceField2 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing2.getFixedPrice()));
        fixedPriceField2.setEditable(false);
        CurrencyChanger.getInstance().addTextField(fixedPriceField2);

        Label fixedPriceCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(fixedPriceCurrencyLabel);
        HBox fixedPriceHbox = new HBox(new Label("Fix ár:"), fixedPriceField2, fixedPriceCurrencyLabel);
        fixedPriceHbox.setSpacing(10);
        right.getChildren().add(fixedPriceHbox);

        expirationDatePicker2 = new DatePicker(listing2.getExpirationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        expirationDatePicker2.setEditable(false);
        HBox expirationDateHbox = new HBox(new Label("Lejárati idõ:"), expirationDatePicker2);
        expirationDateHbox.setSpacing(10);
        right.getChildren().add(expirationDateHbox);

        categoryField2 = new TextField(listing2.getItem().getCategory());
        categoryField2.setEditable(false);

        HBox categoryHbox = new HBox(new Label("Kategória:"), categoryField2);
        categoryHbox.setSpacing(10);
        right.getChildren().add(categoryHbox);

        startingBidField2 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing2.getStartingBid()));
        startingBidField2.setEditable(false);
        Label startingBidCurrencyLabel = new Label();
        CurrencyChanger.getInstance().addCurrencyLabel(startingBidCurrencyLabel);

        HBox startingBidHbox = new HBox(new Label("Kezdõár:"), startingBidField2, startingBidCurrencyLabel);
        startingBidHbox.setSpacing(10);
        right.getChildren().add(startingBidHbox);


        incrementField2 = new TextField(CurrencyChanger.getInstance().inChosenCurrency(listing2.getIncrement()));
        incrementField2.setEditable(false);
        CurrencyChanger.getInstance().addTextField(incrementField2);

        Label incrementCurrencyLabel = new Label("Ft");
        CurrencyChanger.getInstance().addCurrencyLabel(incrementCurrencyLabel);

        HBox incrementHbox = new HBox(new Label("Lépésköz:"), incrementField2, incrementCurrencyLabel);
        incrementHbox.setSpacing(10);
        right.getChildren().add(incrementHbox);

        paymentMethodField2 = new TextField();
        paymentMethodField2.setEditable(false);
        paymentMethodField2.setText(listing2.getPaymentMethod());

        HBox paymentMethodHbox = new HBox(new Label("Fizetési mód:"), paymentMethodField2);
        paymentMethodHbox.setSpacing(10);
        right.getChildren().add(paymentMethodHbox);

        shippingMethodChoiceBox2 = new TextField();
        shippingMethodChoiceBox2.setEditable(false);
        shippingMethodChoiceBox2.setText(listing2.getShippingMethod());

        HBox shippingMethodHbox = new HBox(new Label("Szállítási mód:"), shippingMethodChoiceBox2);
        shippingMethodHbox.setSpacing(10);
        right.getChildren().add(shippingMethodHbox);

    }

    private void init(){
        VBox vBox = new VBox();
        title = new Label(listing1.getTitle() + " és " + listing2.getTitle() + " összehasonlítása");
        title.getStyleClass().add("title-label");

        vBox.getChildren().add(title);

        initLeft();
        initRight();

        HBox hBox = new HBox(left, right);
        hBox.setSpacing(10);
        vBox.getChildren().add(hBox);
        setPadding(new Insets(0,0, 0, 10));
        setContent(vBox);
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
