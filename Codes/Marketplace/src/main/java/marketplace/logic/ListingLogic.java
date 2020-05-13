package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.Item;
import marketplace.model.Listing;
import marketplace.model.Picture;

import java.util.*;


public class ListingLogic {
    Integer listingLimit = 10;  //Admin tudja majd változtatni

    /**
     *Uploads the advertise if the user hasn't reached their limit yet.
     */
    public String create(String advertiser,Listing listing){
        List<Listing> lists = Database.getDatabase().getListings(advertiser);
        if(/*!lists.isEmpty() &&*/ lists.size() < 10) {
            Database.getDatabase().addListing(listing);
            return "Done";
        } else {
            return "This user has too many advertises";
        }
    }

    public List<Listing> listByCategory(List<String> categories){
        Database db = Database.getDatabase();
        List<Listing> allListings = db.getAllListing();
        List<Listing> matchingListings = new ArrayList<Listing>();
        allListings.forEach(temp -> {
            if(categories.contains(temp.getItem().getCategory())){
                matchingListings.add(temp);
            }
        });
        return matchingListings;
    }

    public List<Listing> listByBidOrNot(int bidOrNot){  //If bid -> 1 if not ->0
        Database db = Database.getDatabase();
        List<Listing> allListings = db.getAllListing();
        List<Listing> matchingListings = new ArrayList<Listing>();
        allListings.forEach(temp -> {
            if(temp.getMaximumBid() + 1 >= bidOrNot){
                matchingListings.add(temp);
            }
        });
        return matchingListings;
    }

    public List<Listing> listByPrice(Integer min, Integer max){
        Database db = Database.getDatabase();
        List<Listing> allListings = db.getAllListing();
        List<Listing> matchingListings = new ArrayList<Listing>();
        allListings.forEach(temp -> {
            if((temp.getFixedPrice() < max && temp.getFixedPrice() > min) || (temp.getMostRecentBid().getValue() < max && temp.getMostRecentBid().getValue() > min)){
                matchingListings.add(temp);
            }
        });
        return matchingListings;
    }

    public List<Listing> listByUser(String username){
        Database db = Database.getDatabase();
        List<Listing> matchingListings = db.getListings(username);
        return matchingListings;
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
        Item item = new Item("try", features, pictures, "somewich");
        Listing listing = new Listing("title", "try",
                1, item, "me",
                10000, 9999999, 0, 10,
                date, date, date, "VatikaniValuta", "Teleport");
        temp.create("me", listing);
        item = new Item("try", features, pictures, "Electrical");
        listing = new Listing("title", "try",
                1, item, "me",
                10000, 9999999, 0, 10,
                date, date, date, "cash", "personal");
        temp.create("me", listing);
        temp.listListings("me");
        temp.remove();
    }
}
