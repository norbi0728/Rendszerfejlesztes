package marketplace.client;

import marketplace.client.model.Listing;

import java.util.List;

public class MainController {
    private MarketClientApp app;
    private RestClient restClient = RestClient.getRestClient();
    public MainController(MarketClientApp app) {
        this.app = app;
    }

    public void newListingButtonClicked() {
        app.openNewListingForm();
    }

    public void userListingsButtonClicked() {
        app.openUserListingsPane();
    }

    public void allListingsButtonClicked() {
        app.openAllListingsPane();
    }

    public void addListing(Listing listing) {
        String serverResponse = restClient.addListing(listing);
        System.out.println("addListing(): " + serverResponse);
    }

    public List<Listing> getUserListings() {
        List<Listing> userListings = restClient.getUserListings();
        return userListings;
    }
}
