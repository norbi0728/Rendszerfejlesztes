package marketplace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import marketplace.model.User;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
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

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(stat);
        }catch (Exception e){
            e.printStackTrace();
        }
        Response response = client.target(REST_URI).request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).post(Entity.json(json));

        try {
            dispersion = mapper.readValue(response.readEntity(String.class), Map.class);
        }catch (Exception e){
            e.printStackTrace();
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
