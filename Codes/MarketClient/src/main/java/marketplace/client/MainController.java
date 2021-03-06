package marketplace.client;

import javafx.application.Platform;
import javafx.stage.Stage;
import marketplace.client.currencycomponents.Currency;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.Bid;
import marketplace.client.model.Listing;
import marketplace.client.model.PersonalInformation;

import java.util.List;

public class MainController {
    private LoginScreen loginScreen;
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

    public void logoutButtonClicked() {
        app.stage.hide();
        try {
            new LoginScreen(app).start(new Stage());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String addListing(Listing listing) {
        String serverResponse = restClient.addListing(listing);
        return  serverResponse;
    }

    public List<Listing> getUserListings() {
        List<Listing> userListings = restClient.getUserListings();
        return userListings;
    }

    public List<Listing> getAllListings() {
        return restClient.getAllListings();
    }

    public void updateListing(Listing newListing) {
        restClient.updateListing(newListing);
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
        return restClient.addBid(listing.nextBidValue(), listing);
    }

    public String delete(Listing listing) {
        return restClient.delete(listing);
    }
}
