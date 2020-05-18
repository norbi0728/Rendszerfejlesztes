package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.*;

import java.io.File;
import java.nio.file.Files;
import java.util.*;


public class ListingLogic {
    static void ki(Object a){
        System.out.println(a);
    }   //Lusta vagyok
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

    public List<Listing> listListings(String useName){
        List<Listing> listings = Database.getDatabase().getListings(useName);
        return listings;
    }

    public List<Listing> listListings(){
        List<Listing> listings = Database.getDatabase().getAllListing();
        return listings;
    }
    /*
    * Return those listings which category contained in the categorise parameter list
    * */
    public List<Listing> listByCategories(List<String> categories){
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

    /*
     * Return those listings which category is the category parameter
     * */
    public List<Listing> listByCategory(String category){
        Database db = Database.getDatabase();
        List<Listing> allListings = db.getAllListing();
        List<Listing> matchingListings = new ArrayList<Listing>();
        allListings.forEach(temp -> {
            if(temp.getItem().getCategory().equals(category)){
                matchingListings.add(temp);
            }
        });
        return matchingListings;
    }

    /*
    * Return the bid listings if 1, the simple listings if 0
    * */
    public List<Listing> listByBidOrNot(int bidOrNot){
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

    /*
    * Return the listings what are in the price range (if it a bid listing, then the last bid is checked)
    * */
    public List<Listing> listByPrice(Integer min, Integer max){
        Database db = Database.getDatabase();
        List<Listing> allListings = db.getAllListing();
        List<Listing> matchingListings = new ArrayList<Listing>();
        allListings.forEach(temp -> {
            if((temp.getFixedPrice() < max && temp.getFixedPrice() > min) || (temp.mostRecentBid().getValue() < max && temp.mostRecentBid().getValue() > min)){
                matchingListings.add(temp);
            }
        });
        return matchingListings;
    }

    public List<Listing> listByUser(String userName){
        Database db = Database.getDatabase();
        if(db.userExists(userName)) {
            List<Listing> matchingListings = db.getListings(userName);
            return matchingListings;
        } else {
            return new ArrayList<Listing>();
        }
    }


    public String removeListing(String userName, int id){
        Database db = Database.getDatabase();
        List<Listing> listings = db.getListings(userName);
        for(Listing temp : listings){
            if(temp.getId() == id) {
                db.removeListing(id);
                return "Done";
            }
        }
        return "This listing doesn't exists";
    }

    public String updateListing(Listing listing){
        Database db = Database.getDatabase();
        if(!listing.equals(db.getListingByID(listing.getId()))) {
            db.updateListing(listing);
            return "Done";
        }
        return "There was nochange in this listing";
    }

    /*
    * Returns null if there is no such a user in the database
    * */
    public List<Listing> getOngoingAuctions(String userName){
        Database db = Database.getDatabase();
        if(db.userExists(userName)) {
            List<Listing> temp = db.getOngoingAuctions(userName);
            return temp;
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        /*Integer passHash = "P@ssw0rd".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail");
        Database.getDatabase().addUser(new User("ItsMe", passHash.toString(), mine));*/

        ListingLogic temp = new ListingLogic();
        Map<String, String> features = new HashMap<String, String>();
        List<Picture> pictures = new ArrayList<>();
        Date date = new Date();
        /*features.put("testFeatureName", "testFeatureValue");
        File pic1 = new File("test.png");
        File pic2 = new File("test2.png");
        byte[] data1 = new byte[1000];
        byte[] data2 = new byte[1000];
        try {
            data1 = Files.readAllBytes(pic1.toPath());
            data2 = Files.readAllBytes(pic2.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        pictures.add(new Picture(data1));
        pictures.add(new Picture(data2));*/

        Item item = new Item("try", features, pictures, "Electrical");
        Listing listing = new Listing("title", "try",
                1, item, "ItsMe",
                10000, 9999999, 0, 10,
                date, date, date, "cash", "personal");

        ki(temp.create("ItsMe", listing));
        ki(temp.listByUser("ItsMe"));
        ki(temp.removeListing("ItsMe", Database.getDatabase().getListings("ItsMe").get(0).getId()));
        ki(temp.listByUser("ItsMe"));
    }
}
