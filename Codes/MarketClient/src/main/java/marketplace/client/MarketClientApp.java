package marketplace.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import marketplace.client.currencycomponents.CurrencyChanger;
import marketplace.client.model.Listing;
import marketplace.client.currencycomponents.Currency;
import marketplace.client.model.PersonalInformation;
import marketplace.currencyexchange.CurrencyExchange;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MarketClientApp extends Application {
    Stage stage;
    private Scene scene;
    private BorderPane root;
    public MainController controller;

    private ListingEditor newListingEditor;
    private Region userListingsPane;
    private Region allListingsPane;
    private Pane settingsPane;
    private ScrollPane ongoingAuctionsPane;

    @Override
    public void init() throws Exception {
        controller = new MainController(this);
        root = new BorderPane();
        //root.setStyle("-fx-background-color: orange;");
        root.setLeft(createMenu());
    }

    VBox createMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setSpacing(10);
        menu.setPadding(new Insets(40, 40, 40, 40));

        Button newListingButton = createMenuButton("Új hirdetés", event -> {
            controller.newListingButtonClicked();
        });

        Button userListingsButton = createMenuButton("Saját hirdetéseim", event -> {
            controller.userListingsButtonClicked();
        });

        Button allListingsButton = createMenuButton("Minden hirdetés", event -> {
            controller.allListingsButtonClicked();
        });

        Button settingsButton = createMenuButton("Beállítás", event -> {
            openSettingsPane();
        });

        Button compareButton = createMenuButton(("Összehasonlítás"), event -> {
            if (Listing.selected.size() >= 2) {
                openComparisonPanel();
            } else {
                new Alert(Alert.AlertType.WARNING, "Ki kell jelölni két hirdetést").show();
            }
        });

        menu.getChildren().add(newListingButton);
        menu.getChildren().add(userListingsButton);
        menu.getChildren().add(allListingsButton);
        menu.getChildren().add(settingsButton);
        menu.getChildren().add(compareButton);

        return menu;
    }

    void openComparisonPanel() {
        Listing listing1 = Listing.selected.get(0);
        Listing listing2 = Listing.selected.get(1);
        ListingComparatorDisplay listingComparatorDisplay = new ListingComparatorDisplay(listing1, listing2);
        root.setCenter(listingComparatorDisplay);
    }

    void createPersonalOfferPane() {
        HBox hbox = new HBox();
        ScrollPane scrollPane = new ScrollPane();
        new Thread(() -> {
            try {
                sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Listing> personalOffer = controller.getPersonalOffer();
            Platform.runLater(() -> {
                for (Listing offer : personalOffer) {
                    SmallListingView smallListingView = new SmallListingView(offer);
                    smallListingView.setOnMouseClicked(event -> openListingDisplay(offer));
                    hbox.getChildren().add(smallListingView);
                    scrollPane.setMinViewportHeight(smallListingView.getHeight() + 10);
                }
            });
        }).start();
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(hbox);

        root.setBottom(scrollPane);
    }

    void refreshOngoingAuctions() {
        TimerTask refreshOngoingAuctionsPane = new TimerTask() {
            @Override
            public void run() {
                VBox vbox = new VBox();
                vbox.getStyleClass().add("ongoing-auctions-panel");
                Label title = new Label("Folyamatban lévõ aukciók");
                title.getStyleClass().add("title-label");
                vbox.getChildren().add(title);

                new Thread(() -> {
                    List<Listing> ongoingAuctions = controller.getOngoingAuctions();
                    Platform.runLater(() -> {
                        for (Listing ongoing : ongoingAuctions) {
                            OngoingAuctionStateView ongoingAuctionStateView
                                    = new OngoingAuctionStateView(ongoing);
                            ongoingAuctionStateView.setOnMouseClicked(event -> openListingDisplay(ongoing));
                            vbox.getChildren().add(ongoingAuctionStateView);
                        }
                        ongoingAuctionsPane.setContent(vbox);
                    });
                }).start();
            }
        };

        Timer refreshTimer = new Timer();
        refreshTimer.schedule(refreshOngoingAuctionsPane, 5000, 5000);
    }

    void createOngoingAuctionsPane() {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("ongoing-auctions-panel");
        Label title = new Label("Folyamatban lévõ aukciók");
        title.getStyleClass().add("title-label");
        vbox.getChildren().add(title);

        ongoingAuctionsPane = new ScrollPane();

        new Thread(() -> {
            try {
                sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Listing> ongoingAuctions = controller.getOngoingAuctions();
            Platform.runLater(() -> {
                for (Listing ongoing : ongoingAuctions) {
                    OngoingAuctionStateView ongoingAuctionStateView
                            = new OngoingAuctionStateView(ongoing);
                    ongoingAuctionStateView.setOnMouseClicked(event -> openListingDisplay(ongoing));
                    vbox.getChildren().add(ongoingAuctionStateView);
                }
            });
        }).start();
        ongoingAuctionsPane.setContent(vbox);
        root.setRight(ongoingAuctionsPane);
    }

    private Button createMenuButton(String title, EventHandler<ActionEvent> handler) {
        Button button = new Button(title);
        button.setOnAction(handler);
        VBox.setVgrow(button, Priority.ALWAYS);
        button.getStyleClass().add("menu-button");

        return button;
    }

    private void openSettingsPane() {
        if (settingsPane == null) {
            settingsPane = new SettingsPanel(controller);
        }
        root.setCenter(settingsPane);
    }

    public void openNewListingForm() {
        if (newListingEditor == null) {
            newListingEditor = new ListingEditor(stage, null);
            newListingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
            newListingEditor.setOnSaveClicked((newListing) -> {
                String ret = controller.addListing(newListing);
                new Alert(Alert.AlertType.INFORMATION, ret).show();
            });
        }
        root.setCenter(new ScrollPane(newListingEditor));
    }

    public void openListingForEditing(Listing listing) {
        ListingEditor listingEditor = new ListingEditor(stage, listing);
        listingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
        listingEditor.setOnSaveClicked((newListing) -> {
            controller.updateListing(newListing);
        });
        listingEditor.setOnDeleteClicked(() -> {
            String ret = controller.delete(listing);
            new Alert(Alert.AlertType.INFORMATION, ret).show();
        });
        root.setCenter(new ScrollPane(listingEditor));
    }

    public void openUserListingsPane() {
        userListingsPane = createUserListingsPanel();
        //userListingsPane.setStyle("-fx-background-color: purple;");
        root.setCenter(userListingsPane);
        BorderPane.setAlignment(userListingsPane, Pos.CENTER);
    }

    public void openAllListingsPane() {
        allListingsPane = createAllListingsPanel();
        root.setCenter(allListingsPane);
        BorderPane.setAlignment(allListingsPane, Pos.CENTER);
    }

    private VBox createAllListingsPanel() {
        ManyListingsPane allListingsPanel = new ManyListingsPane(("Minden hirdetés"));
        new Thread(() -> {
            List<Listing> allListings = controller.getAllListings();
            Platform.runLater(() -> {
                allListingsPanel.setOnListingClicked(listing -> {
                    openListingDisplay(listing);
                });
                allListingsPanel.addListings(allListings);
            });
        }).start();
        return allListingsPanel;
    }

    private VBox createUserListingsPanel() {
        ManyListingsPane userListingsPanel = new ManyListingsPane("Saját hirdetések");
        new Thread(() -> {
            List<Listing> userListings = controller.getUserListings();
            Platform.runLater(() -> {
                userListingsPanel.setOnListingClicked(listing -> {
                    openListingForEditing(listing);
                });
                userListingsPanel.addListings(userListings);
            });
        }).start();
        return userListingsPanel;
    }

    private void openListingDisplay(Listing listing) {
        ListingDisplay listingDisplay = new ListingDisplay(listing);
        listingDisplay.setAlignment(Pos.TOP_CENTER);
        listingDisplay.setOnBid(() -> {
            if (listingDisplay.listing.getExpirationDate().before(new Date())) {
                new Alert(Alert.AlertType.WARNING, "Licit lejárt").show();
                return;
            }

            int nextBidValue = listingDisplay.listing.nextBidValue();
            String nextBidValueInChosenCurrency = CurrencyChanger.getInstance().inChosenCurrency(nextBidValue);
            String chosenCurrency = CurrencyChanger.getInstance().currency.toString();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Licitálás " + nextBidValueInChosenCurrency + " " + chosenCurrency + " értékben?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                new Thread(() -> {
                    controller.addBid(listingDisplay.listing);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(() -> {
                        listingDisplay.refresh();
                    });
                }).start();
            }
        });

        root.setCenter(new ScrollPane(listingDisplay));
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Piac");
        scene = new Scene(root, 1600, 900, Color.web("#f4f4f4"));
        scene.getStylesheets().add("main_style.css");
        stage.setScene(scene);

        new LoginScreen(this).start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
