package marketplace.client;

import marketplace.client.model.Listing;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RestClient {
    private Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:7000");
    private String securityKey;
    public String name;
    private static RestClient instance;

    private RestClient() {}

    public static RestClient getRestClient() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public String login(String name, String passwordHash) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("login")
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        securityKey = response.readEntity(String.class);
        return (String) response.getHeaders().get("Server-Response").get(0);
    }

    public String register(String name, String passwordHash) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("registration")
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        return response.readEntity(String.class);
    }

    public String addListing(Listing listing) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("addListing")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(listing, MediaType.APPLICATION_JSON));
        return response.getHeaderString("Server-Response");
    }

    public List<Listing> getUserListings() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getUserListings")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {});
    }

    public List<Listing> getAllListings() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getAllListings")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(List.class);
    }

    public List<Listing> getPersonalOffer() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getPersonalOffer")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {});
    }

    public List<Listing> getOngoingAuctions() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getOngoingAuctions")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {});
    }
}
