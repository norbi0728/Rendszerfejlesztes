package marketplace.client;

import javafx.application.Platform;
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

    public SmallListingView(Listing listing) {
        this.listing = listing;
        Platform.runLater(() -> init());
    }

    private void init() {
        getStyleClass().add("small-listing-view");

        titleLabel = new Label();
        imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        priceField = new TextField();
        priceField.setDisable(true);
        CurrencyChanger.getInstance().addTextField(priceField);
        checkBox = new CheckBox();

        getChildren().add(titleLabel);
        getChildren().add(imageView);

        HBox bottomLine = new HBox();
        bottomLine.getChildren().add(priceField);
        currencyLabel = new Label();
        CurrencyChanger.getInstance().addCurrencyLabel(currencyLabel);
        bottomLine.getChildren().add(currencyLabel);
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
}
