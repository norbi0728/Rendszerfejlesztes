package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.Bid;
import marketplace.model.Listing;
import marketplace.model.PersonalInformation;
import marketplace.model.User;

import java.util.Date;
import java.util.List;

public class BidLogic {
    static void ki(Object a){
        System.out.println(a);
    }   //Lusta vagyok

    public String addBid(String userName, int value, int listingId){
        Database db = Database.getDatabase();
        for(Listing temp : db.getAllListing()){
            if(temp.getId() == listingId){
                if(db.getListingByID(listingId).mostRecentBid().getValue() < value){
                    db.addBid(new Bid(userName, value, listingId, new Date(System.currentTimeMillis())));
                    return "Done";
                }
                return "Too low bid value!";
            }
        }
        return "There is no such a listing with this ID!";
    }

    public static void main(String[] args) throws InterruptedException {
        BidLogic temp = new BidLogic();
        /*Database.getDatabase().addBid(new Bid("ItsMe", 666666666,
                Database.getDatabase().getListings("ItsMe").get(0).getId(),
                new Date(System.currentTimeMillis())));*/
        ki(Database.getDatabase().getListingByID(Database.getDatabase().getListings("ItsMe").get(0).getId()).mostRecentBid().getValue());
        ki(temp.addBid("ItsMe", 0, Database.getDatabase().getListings("ItsMe").get(0).getId()));
        ki(temp.addBid("ItsMe", 0, 0));
    }
}
