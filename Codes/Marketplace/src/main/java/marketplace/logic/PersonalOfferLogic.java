package marketplace.logic;


import marketplace.model.Listing;
import marketplace.model.User;
import marketplace.service.PersonalisedOfferServiceClient;

import java.util.*;

public class PersonalOfferLogic {
    public List<Listing> getPersonalisedOffer(User user){
        Random rand = new Random();
        Map<String, Integer> dispersion = new PersonalisedOfferServiceClient().getDispersion(user);
        List<Listing> offers = new ArrayList<Listing>();
        for(Map.Entry<String,Integer> entry : dispersion.entrySet()){
            List<Integer> alreadyIn = new ArrayList<Integer>();
            for(int i = 0; i < entry.getValue(); i++){
                List<Listing> catOff = new ListingLogic().listByCategory(entry.getKey());
                Integer index = rand.nextInt(catOff.size());
                if(!alreadyIn.contains(index)) {
                    offers.add(catOff.get(index));
                } else {
                    i--;
                }
            }
        }
        return offers;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println();
    }
}
