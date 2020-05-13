package marketplace.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SmallListingView extends VBox {
    private Listing listing;
    private TextField titleField;
    private ImageView imageView;
    private Label priceLabel;

    public SmallListingView(Listing listing) {
        this.listing = listing;
        Platform.runLater(() -> init());
    }

    private void init() {
        titleField = new TextField();
        imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        priceLabel = new Label();

        getChildren().add(titleField);
        getChildren().add(imageView);
        getChildren().add(priceLabel);

        setListing(listing);
    }

    public void setListing(Listing listing) {
        this.listing = listing;
        titleField.setText(listing.getTitle());
        if (listing.getItem().getPictures().size() > 0) {
            imageView.setImage(listing.getItem().getPictures().get(0).asImage());
        }
        if (listing.mostRecentBid() != null) {
            priceLabel.setText(String.valueOf(listing.mostRecentBid().getValue()));
        }
    }
}
