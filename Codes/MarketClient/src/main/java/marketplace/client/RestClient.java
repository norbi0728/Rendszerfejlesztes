package marketplace.client;

import marketplace.client.model.Listing;
import marketplace.client.model.PersonalInformation;

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

    private RestClient() {
    }

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

    public void updateListing(Listing listing) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("updateListing")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(listing, MediaType.APPLICATION_JSON));
        //return response.getHeaderString("Server-Response");
    }

    public List<Listing> getUserListings() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getUserListings")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {
        });
    }

    public List<Listing> getAllListings() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getAllListings")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {
        });
    }

    public List<Listing> getPersonalOffer() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getPersonalOffer")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {
        });
    }

    public PersonalInformation getOwnPersonalInformation() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getOwnPersonalInformation")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return response.readEntity(PersonalInformation.class);
    }

    public PersonalInformation getPersonalInformation(String userName) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getPersonalInformation")
                .queryParam("securityKey", securityKey)
                .queryParam("userName", userName)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return response.readEntity(PersonalInformation.class);
    }

    public String setPersonalInformation(PersonalInformation personalInformation) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("setPersonalInformation")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(personalInformation, MediaType.APPLICATION_JSON));
        return response.getHeaderString("Server-Response");
    }


    public List<Listing> getOngoingAuctions() {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("getOngoingAuctions")
                .queryParam("securityKey", securityKey)
                .request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(null);
        return (List<Listing>) response.readEntity(new GenericType<List<Listing>>() {
        });
    }

    public String addBid(int value, Listing listing) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("addBid")
                .queryParam("securityKey", securityKey)
                .queryParam("value", value)
                .queryParam("listingID", listing.getId())
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        return (String) response.getHeaders().get("Server-Response").get(0);
    }

    public String delete(Listing listing) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("removeListing")
                .queryParam("securityKey", securityKey)
                .queryParam("listingID", listing.getId())
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        return response.getHeaderString("Server-Response");
    }

    public String updateStatistics(Listing listing, String event) {
        new Thread(() -> {
            Invocation.Builder invocationBuilder
                    = webTarget
                    .path("updateStatistics")
                    .queryParam("securityKey", securityKey)
                    .queryParam("category", listing.getItem().getCategory())
                    .queryParam("event", event)
                    .request(MediaType.TEXT_PLAIN_TYPE);

            Response response = invocationBuilder.post(null);
            response.getHeaderString("Server-Response");
        }).start();
        return null;
    }

    public Listing getListingById(int id) {
        List<Listing> allListings = getAllListings();
        for (Listing listing : allListings) {
            if (listing.getId() == id) {
                return listing;
            }
        }
        return null;
    }
}
