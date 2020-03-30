package marketplace.client;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;

public class RestClient {
    private Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:7000");

    public String login(String name, String passwordHash) {
        Invocation.Builder invocationBuilder
                = webTarget
                .path("login")
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        return response.readEntity(String.class);
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
}
