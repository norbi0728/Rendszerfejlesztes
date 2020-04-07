package marketplace.client;

public class MainController {
    private MainWindow mainWindow;
    public MainController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void newListingButtonClicked() {
        mainWindow.openNewListingForm();
    }
}
