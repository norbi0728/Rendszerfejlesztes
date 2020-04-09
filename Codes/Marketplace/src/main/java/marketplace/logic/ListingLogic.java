package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.Item;
import marketplace.model.Listing;
import marketplace.model.Picture;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListingLogic {
    Integer listingLimit = 10;  //Admin tudja majd változtatni

    /**
     *Uploads the advertise if the user hasn't reached their limit yet.
     */
    public String create(String title, String description,
                  int quantity, String name, Map<String, String> features, List<Picture> pictures, String category, String advertiser,
                  int increment, int maximumBid, int startingBid,
                  int fixedPrice, Date expirationDate, Date creationDate,
                  Date removeDate, String paymentMethod, String shippingMethod){
        List<Listing> lists = Database.getDatabase().getListings(advertiser);
        if(!lists.isEmpty() && lists.size() < 10) {
            Item item = new Item(name, features, pictures, category);
            Listing listing = new Listing(title, description,
                    quantity, item, advertiser,
                    increment, maximumBid, startingBid,
                    fixedPrice, expirationDate, creationDate,
                    removeDate, paymentMethod, shippingMethod);
            Database.getDatabase().addListing(listing);
            return "Done";
        } else {
            return "This user has too many advertises";
        }
    }

    public List<Listing> listListings(String useName){
        List<Listing> listings = Database.getDatabase().getListings(useName);
        return listings;
    }

    public String remove(){
        return "Done";
    }

    public static void main(String[] args) throws InterruptedException {
        ListingLogic temp = new ListingLogic();
        Map<String, String> features = new HashMap<String, String>();
        List<Picture> pictures = null;
        Date date = new Date();
        temp.create("title", "try", 1, "try",
                features, pictures, "somewich", "me",
                10000, 9999999, 0, 10,
                date, date, date, "VatikaniValuta", "Teleport");
        temp.listListings("me");
        temp.remove();
    }
}
