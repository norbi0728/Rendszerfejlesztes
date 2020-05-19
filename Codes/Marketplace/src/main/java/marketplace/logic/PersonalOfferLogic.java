package marketplace.logic;


import marketplace.database.Database;
import marketplace.model.Listing;
import marketplace.model.PersonalInformation;
import marketplace.model.User;
import marketplace.service.PersonalisedOfferServiceClient;

import java.util.*;

public class PersonalOfferLogic {
    static void ki(Object a){
        System.out.println(a);
    }   //Lusta vagyok
    public List<Listing> getPersonalisedOffer(User user){
        Random rand = new Random();
        Database db = Database.getDatabase();
        Map<String, Integer> dispersion = new PersonalisedOfferServiceClient().getDispersion(user);
        List<Listing> offers = new ArrayList<Listing>();
        List<Listing> mine = db.getListings(user.getName());
        for(Map.Entry<String,Integer> entry : dispersion.entrySet()){
            List<Integer> alreadyIn = new ArrayList<Integer>();
            for(int i = 0; i < entry.getValue(); i++){
                List<Listing> catOff = new ListingLogic().listByCategory(entry.getKey());
                if(!catOff.isEmpty()) {
                    Integer index = rand.nextInt(catOff.size());
                    if (!alreadyIn.contains(index)) {
                        if(!mine.contains(catOff.get(index))){
                            offers.add(catOff.get(index));
                        } else {
                            i--;
                        }
                    } else {
                        i--;
                    }
                }
            }
        }
        return offers;
    }

    public static void main(String[] args) throws InterruptedException {
        Integer passHash = "P@ssw0rd".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail", "HUF");
        List<Listing> myOffers = new PersonalOfferLogic().getPersonalisedOffer(new User("ItsMe", passHash.toString(), mine));
        ki(myOffers);
        for(Listing l: myOffers){
            System.out.println(l.getAdvertiser());
        }
    }
}
