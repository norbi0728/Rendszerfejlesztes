package marketplace.currencyexchange;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;
import java.io.IOException;


public class CurrencyExchange
{
    public double getExchangeRates(int price, String currency) throws IOException, InterruptedException, JsonMappingException {
        double newPrice = 0;

        final String REST_URI = "https://api.exchangeratesapi.io/latest";
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();

        Response response = (Response) client.target(REST_URI).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

        CurrencyExchangeData exchangeRates = mapper.readValue((JsonParser) response, CurrencyExchangeData.class);

        switch (currency)
        {
            case "EUR":
                newPrice = price / exchangeRates.getRates().getHUF();
                break;
            case "USD":
                newPrice = (price / exchangeRates.getRates().getHUF()) * exchangeRates.getRates().getUSD();
                break;
            case "GBP":
                newPrice = (price / exchangeRates.getRates().getHUF()) * exchangeRates.getRates().getGBP();
                break;
        }
        return newPrice;
    }
}
