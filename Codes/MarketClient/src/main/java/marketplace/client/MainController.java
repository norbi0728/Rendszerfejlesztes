package marketplace.client;

public class MainController {
    private MarketClientApp app;
    public MainController(MarketClientApp app) {
        this.app = app;
    }

    public void newListingButtonClicked() {
        app.openNewListingForm();
    }

    public void userListingsButtonClicked() {
        app.openUserListingsPane();
    }
}
