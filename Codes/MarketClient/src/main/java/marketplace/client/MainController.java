package marketplace.client;

import javafx.application.Platform;
import marketplace.client.currencycomponents.Currency;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.Bid;
import marketplace.client.model.Listing;
import marketplace.client.model.PersonalInformation;

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
        //System.out.println("addListing(): " + serverResponse);
    }

    public List<Listing> getUserListings() {
        List<Listing> userListings = restClient.getUserListings();
        return userListings;
    }

    public List<Listing> getAllListings() {
        return restClient.getAllListings();
    }

    public void updateListing(Listing newListing) {
        // TODO Call api
    }

    public List<Listing> getPersonalOffer() {
        List<Listing> personalOffer = restClient.getPersonalOffer();
        return personalOffer;
    }

    public void setPersonalInformation(PersonalInformation newPersonalInformation) {
        restClient.setPersonalInformation(newPersonalInformation);
    }

    public PersonalInformation getOwnPersonalInformation() {
        return restClient.getOwnPersonalInformation();
    }

    public void setPreferredCurrency() {
        new Thread(() -> {
            PersonalInformation ownPersonalInformation = getOwnPersonalInformation();
            Currency preferredCurrency = Currency.forName(ownPersonalInformation.getPreferredCurrency());
            if (preferredCurrency == null) return;
            Platform.runLater(() -> {
                CurrencyChanger.getInstance().changeDisplayCurrency(preferredCurrency);
            });
        }).start();
    }
    public List<Listing> getOngoingAuctions(){
        List<Listing> ongoingAuctions = restClient.getOngoingAuctions();
        return ongoingAuctions;
    }

    public String addBid(Listing listing) {
        int value;
        Bid mostRecentBid = listing.mostRecentBid();
        if (mostRecentBid != null) {
            value = mostRecentBid.getValue() + listing.getIncrement();
        } else {
            value = listing.getStartingBid();
        }
        return restClient.addBid(value, listing);
    }

    public void delete(Listing listing) {
        restClient.delete(listing);
    }
}
