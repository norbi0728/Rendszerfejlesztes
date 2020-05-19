package marketplace.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import marketplace.client.currencycomponents.Currency;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.Listing;

public class SmallListingView extends VBox {
    private Listing listing;
    private Label titleLabel;
    private ImageView imageView;
    private TextField priceField;
    private Label currencyLabel;
    private CheckBox checkBox;

    private Runnable onSelected;
    private Runnable onDeselected;

    public SmallListingView(Listing listing) {
        this.listing = listing;
        Platform.runLater(() -> init());
    }

    private void init() {
        getStyleClass().add("small-listing-view");

        titleLabel = new Label();
        titleLabel.setMaxWidth(100);
        imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        priceField = new TextField();
        priceField.setEditable(false);
        CurrencyChanger.getInstance().addTextField(priceField);
        checkBox = new CheckBox();
        getChildren().add(titleLabel);
        getChildren().add(imageView);

        HBox bottomLine = new HBox();
        VBox.setMargin(bottomLine, new Insets(10, 0, 0, 0));
        HBox.setMargin(checkBox, new Insets(0, 0, 0, 30));
        bottomLine.getChildren().add(priceField);
        currencyLabel = new Label();
        HBox.setMargin(currencyLabel, new Insets(0, 0, 0, 10));
        currencyLabel.setAlignment(Pos.BOTTOM_CENTER);
        CurrencyChanger.getInstance().addCurrencyLabel(currencyLabel);
        bottomLine.getChildren().add(currencyLabel);
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                Listing.selected.push(listing);
                if (onSelected != null) onSelected.run();
            } else {
                Listing.selected.remove(listing);
                if (onDeselected != null) onDeselected.run();
            }
        });
        bottomLine.getChildren().add(checkBox);
        getChildren().add(bottomLine);

        setListing(listing);
    }

    public void setListing(Listing listing) {
        this.listing = listing;
        titleLabel.setText(listing.getTitle());
        if (listing.getItem().getPictures().size() > 0) {
            imageView.setImage(listing.getItem().getPictures().get(0).asImage());
        }
        int displayPrice = listing.displayPrice();
        priceField.setText(String.valueOf(CurrencyChanger.getInstance().inChosenCurrency(displayPrice)));
    }

    public void setOnSelected(Runnable onSelected) {
        this.onSelected = onSelected;
    }

    public void setOnDeselected(Runnable onDeselected) {
        this.onDeselected = onDeselected;
    }

    public boolean isChecked() {
        return checkBox.isSelected();
    }
}
