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
        ListingLogic lL = new ListingLogic();
        Map<String, Integer> dispersion = new PersonalisedOfferServiceClient().getDispersion(user);
        List<Listing> myOffers = new ArrayList<Listing>();
        List<Listing> mine = lL.listByUser(user.getName());
        for(Map.Entry<String,Integer> entry : dispersion.entrySet()){
            List<Listing> listingsOfCategory = lL.listByCategory(entry.getKey());
            List<Listing> notMyListingsOfCategory = new ArrayList<Listing>();
            for(Listing listing : listingsOfCategory){
                if(listing.getAdvertiser() != user.getName()){
                    notMyListingsOfCategory.add(listing);
                }
            }
            Integer limit = entry.getValue();
            if(notMyListingsOfCategory.size() < limit){
                limit = notMyListingsOfCategory.size();
            }
            for(int i = 0; i < limit; i++) {
                Integer index = rand.nextInt(notMyListingsOfCategory.size());
                myOffers.add(notMyListingsOfCategory.get(index));
                notMyListingsOfCategory.remove(index);
            }
        }
        return myOffers;
    }

    public static void main(String[] args) throws InterruptedException {
//        Integer passHash = "P@ssw0rd".hashCode();
//        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail", "HUF");
//        List<Listing> myOffers = new PersonalOfferLogic().getPersonalisedOffer(new User("ItsMe", passHash.toString(), mine));
        Integer passHash = "testPw2".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail", "HUF");
        List<Listing> myOffers = new PersonalOfferLogic().getPersonalisedOffer(new User("testUser2", passHash.toString(), mine));
        ki(myOffers);
        ki(new ListingLogic().listByUser("testUser2"));
        for(Listing l: myOffers){
            System.out.println(l.getAdvertiser());
            ki(l);
        }
    }
}
