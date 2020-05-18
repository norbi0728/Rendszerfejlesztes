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
            openComparisonPanel();
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
        gridPane.setGridLinesVisible(true);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        ChoiceBox<Currency> currencyChoiceBox;
        Button saveButton;

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

        saveButton = new Button("Mentés");
        saveButton.setOnAction(event -> {
            PersonalInformation newPersonalInformation = new PersonalInformation(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    currencyChoiceBox.getValue().toString()
            );
            new Thread(() -> {
                controller.setPersonalInformation(newPersonalInformation);
            }).start();
        });
        gridPane.add(saveButton, 0, 8);

        vBox.getChildren().add(gridPane);
        return vBox;
    }

    public void openNewListingForm() {
        if (newListingEditor == null) {
            newListingEditor = new ListingEditor(stage, null);
            newListingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
            newListingEditor.setOnSaveClicked((newListing) -> {
                controller.addListing(newListing);
            });
        }
        root.setCenter(newListingEditor);
    }

    public void openListingForEditing(Listing listing) {
        ListingEditor listingEditor = new ListingEditor(stage, listing);
        listingEditor.setAlignment(Pos.TOP_CENTER); //TODO Not here
        listingEditor.setOnSaveClicked((newListing) -> {
            controller.updateListing(newListing);
        });
        listingEditor.setOnDeleteClicked(() -> {
            controller.delete(listing);
        });
        root.setCenter(listingEditor);
    }

    public void openUserListingsPane() {
        userListingsPane = createUserListingsPanel();
        //userListingsPane.setStyle("-fx-background-color: purple;");
        root.setCenter(userListingsPane);
        BorderPane.setAlignment(userListingsPane, Pos.CENTER);
    }

    public void openAllListingsPane() {
        if (allListingsPane == null) allListingsPane = createAllListingsPanel();
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
            new Thread(() -> {
                controller.addBid(listing);
            }).start();
        });

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
