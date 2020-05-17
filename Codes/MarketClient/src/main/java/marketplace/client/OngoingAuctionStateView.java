package marketplace.client;

import javafx.scene.control.Label;
import marketplace.client.model.Listing;

public class OngoingAuctionStateView extends Label {
    private Listing listing;

    public OngoingAuctionStateView(Listing listing){
        this.listing = listing;
        setText(listing.getTitle());
    }
}
