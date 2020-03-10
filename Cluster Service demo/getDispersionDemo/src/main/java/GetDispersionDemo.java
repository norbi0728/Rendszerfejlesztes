import org.json.JSONObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GetDispersionDemo {
    private final String REST_URI = "http://localhost:5000/";
    private Client client = ClientBuilder.newClient();
    private Map<String, Double> stat;

    public static void main(String[] args) throws IOException {
        new GetDispersionDemo().start();
    }

    private void start() throws IOException {
        stat = new HashMap<String, Double>();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        String[] keys = {"Electrical", "Sport", "VehicleAndParts", "BeautyCare", "Cultural", "Home", "Gathering"};

        for (int i = 0; i < keys.length; i ++){
            System.out.println(keys[i] + " :");
            stat.put(keys[i], Double.parseDouble(reader.readLine()));
        }

        JSONObject json = new JSONObject(stat);
        System.out.println("your input:\n" + json);

        Response response = client.target(REST_URI).request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).post(Entity.json(json.toString()));

        String respStr = response.readEntity((String.class));
        System.out.println("model response:\n" +respStr);
        /*
        JSONObject resp = new JSONObject(respStr);
        System.out.println("Sport "+resp.getDouble("Sport"));

         */

    }
}
