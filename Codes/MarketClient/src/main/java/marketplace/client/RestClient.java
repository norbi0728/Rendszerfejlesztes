package marketplace.client;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {
    private Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:7000");
    private String token;

    public String login(String name, String passwordHash) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("login")
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        token = response.readEntity(String.class);
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

    public void addNewListing(Listing listing) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("registration")
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);
        WebTarget newListingWebTarget
                = webTarget.path("addlisting");

        Invocation.Builder invocationBuilder
                = newListingWebTarget.request(MediaType.APPLICATION_JSON);
        Response response
                = invocationBuilder
                .post(Entity.entity(listing, MediaType.APPLICATION_JSON);
    }
}
