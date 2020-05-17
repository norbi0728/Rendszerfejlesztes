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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MarketClientApp extends Application {
    Stage stage;
    private Scene scene;
    private BorderPane root;
    private MainController controller;

    private ListingEditor listingEditor;
    private Region userListingsPane;
    private FlowPane allListingsPane;
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

        menu.getChildren().add(newListingButton);
        menu.getChildren().add(userListingsButton);
        menu.getChildren().add(allListingsButton);
        menu.getChildren().add(settingsButton);

        return menu;
    }

    void createPersonalOfferPane(){
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
                }
            });
        }).start();
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(hbox);
        root.setBottom(scrollPane);
    }
    void refreshOngoingAuctions(){
        TimerTask refreshOngoingAuctionsPane = new TimerTask() {
            @Override
            public void run() {
                VBox vbox = new VBox();
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
        refreshTimer.schedule(refreshOngoingAuctionsPane, 10000, 10000);
    }
    void createOngoingAuctionsPane(){
        VBox vbox = new VBox();

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
            settingsPane = createSettingsPane();
        }
        root.setCenter(settingsPane);
    }

    private Pane createSettingsPane() {
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        ChoiceBox<Currency> currencyChoiceBox;

        gridPane.add(new Label("Keresztnév:"), 0, 3);
        gridPane.add(firstNameField, 1, 3);

        gridPane.add(new Label("Családnév:"), 0, 4);
        gridPane.add(lastNameField, 1, 4);

        gridPane.add(new Label("Lakcím:"), 0, 5);
        gridPane.add(addressField, 1, 5);

        gridPane.add(new Label("Telefon:"), 0, 6);
        gridPane.add(phoneField, 1, 6);

        gridPane.add(new Label("E-mail:"), 0, 7);
        gridPane.add(emailField, 1, 7);

        gridPane.add(new Label("Pénznem:"), 2, 3);
        currencyChoiceBox = new ChoiceBox<>();
        currencyChoiceBox.getItems().setAll(Currency.values());
        currencyChoiceBox.setValue(CurrencyChanger.getInstance().currency);
        currencyChoiceBox.setOnAction((event -> {
            CurrencyChanger.getInstance().changeDisplayCurrency(currencyChoiceBox.getValue());
        }));
        gridPane.add(currencyChoiceBox, 3, 3);

        vBox.getChildren().add(gridPane);
        return vBox;
    }

    public void openNewListingForm() {
        if (listingEditor == null) {
            listingEditor = new ListingEditor(stage, null);
            listingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
            listingEditor.setOnNewListingReady((newListing) -> {
                controller.addListing(newListing);
            });
        }
        root.setCenter(listingEditor);
    }

    public void openListingForEditing(Listing listing) {
        listingEditor = new ListingEditor(stage, listing);
        listingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
        listingEditor.setOnNewListingReady((newListing) -> {
            controller.updateListing(newListing);
        });
        root.setCenter(listingEditor);
    }

    public void openUserListingsPane() {
        if (userListingsPane == null) userListingsPane = createUserListingsPanel();
        //userListingsPane.setStyle("-fx-background-color: purple;");
        root.setCenter(userListingsPane);
        BorderPane.setAlignment(userListingsPane, Pos.CENTER);
    }

    public void openAllListingsPane() {
        if (allListingsPane == null) allListingsPane = createAllListingsPanel();
    }

    private FlowPane createAllListingsPanel() {
        FlowPane flowPane = new FlowPane();

        return flowPane;
    }

    private Region createUserListingsPanel() {
        VBox vBox = new VBox();
        //vBox.setStyle("-fx-background-color: green;");
        HBox hBox = new HBox();
        hBox.getChildren().add(new Label("Saját hirdetések"));
        vBox.getChildren().add(hBox);
        FlowPane flowPane = new FlowPane();

        new Thread(() -> {
            List<Listing> userListings = controller.getUserListings();
            Platform.runLater(() -> {
                for (Listing userListing : userListings) {
                    SmallListingView smallListingView = new SmallListingView(userListing);
                    smallListingView.setPadding(new Insets(10, 10, 10, 10)); //TODO not here
                    smallListingView.setOnMouseClicked((event) -> {
                        openListingDisplay(userListing);
                    });
                    flowPane.getChildren().add(smallListingView);
                }
            });
        }).start();

        vBox.getChildren().add(flowPane);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    private void openListingDisplay(Listing listing) {
        ListingDisplay listingDisplay = new ListingDisplay(listing);
        listingDisplay.setAlignment(Pos.TOP_CENTER);
        root.setCenter(listingDisplay);
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
