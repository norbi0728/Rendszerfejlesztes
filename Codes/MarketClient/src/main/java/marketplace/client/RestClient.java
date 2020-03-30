package marketplace.client;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {
    private Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:7000/login");

    public void login(String name, String passwordHash) {
        Invocation.Builder invocationBuilder
                = webTarget
                .queryParam("username", name)
                .queryParam("passwordHash", passwordHash)
                .request(MediaType.TEXT_PLAIN_TYPE);

        Response response = invocationBuilder.post(null);
        System.out.println(response.readEntity(String.class));
    }
}
