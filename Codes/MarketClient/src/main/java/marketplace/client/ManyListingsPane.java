package marketplace.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import marketplace.client.SmallListingView;
import marketplace.client.model.Listing;

import java.util.List;
import java.util.function.Consumer;

public class ManyListingsPane extends VBox {
    private String title;
    private FlowPane flowPane;
    private Consumer<Listing> onListingClicked;

    public ManyListingsPane(String title) {
        this.title = title;
        init();
    }

    private void init() {
        getChildren().add(new Label(title));
        flowPane = new FlowPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getChildren().add(scrollPane);
    }

    public void addListing(Listing listing) {
        SmallListingView smallListingView = new SmallListingView(listing);
        smallListingView.setPadding(new Insets(10, 10, 10, 10)); //TODO not here
        smallListingView.setOnMouseClicked((event) -> {
            onListingClicked.accept(listing);
        });
        flowPane.getChildren().add(smallListingView);
    }

    public void addListings(List<Listing> listings) {
        for (Listing listing : listings) {
            addListing(listing);
        }
    }

    public void setOnListingClicked(Consumer<Listing> onListingClicked) {
        this.onListingClicked = onListingClicked;
    }
}
