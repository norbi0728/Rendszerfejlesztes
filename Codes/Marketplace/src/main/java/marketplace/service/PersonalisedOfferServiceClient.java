package marketplace.service;

import marketplace.model.User;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class PersonalisedOfferServiceClient {
    private final String REST_URI = "http://localhost:5000/";
    private Client client = ClientBuilder.newClient();
    private Map<String, Double> stat;
    private final String[] categories = {"Electrical", "Sport", "Vehicle and Parts", "Beautycare", "Cultural", "Home", "Gathering"};

    public Map<String, Integer> getDispersion(User user){
        Map<String, Integer> dispersion = new HashMap<>();
        stat = user.getStatistics().getStats();

        JSONObject json = new JSONObject(stat);

        Response response = client.target(REST_URI).request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).post(Entity.json(json.toString()));

        JSONObject resp = new JSONObject(response.readEntity(String.class));
        Iterator<String> keys = resp.keys();
        for(int i = 0; i < resp.length(); i++){
            String key = keys.next();
            dispersion.put(key, resp.getInt(key));
        }
        int counter = 0;
        for(Integer val: dispersion.values()){
            counter += val;
        }

        if (counter != 10){
            if (counter < 10){
                int diff = 10 - counter;
                for (int i = 0; i < diff; i++){
                    String randCat = categories[new Random().nextInt(categories.length)];
                    dispersion.put(randCat, dispersion.get(randCat) + 1);
                }
            }
            else if(counter > 11){
                int diff = 10 - counter;
                String maxCat = "";
                int maxVal = 0;
                for (Map.Entry<String, Integer> disp: dispersion.entrySet()){
                    if(disp.getValue() > maxVal){
                        maxVal = disp.getValue();
                        maxCat = disp.getKey();
                    }
                }
                for (int i = 0; i < diff; i++){
                    dispersion.put(maxCat, dispersion.get(maxCat) - 1);
                }
            }
        }
        return dispersion;
    }
}
