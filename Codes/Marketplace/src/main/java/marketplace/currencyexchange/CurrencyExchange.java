package marketplace.currencyexchange;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Response;


public class CurrencyExchange
{
    public double getExchangeRates(int price, String currency) throws IOException, InterruptedException, JsonMappingException {
        double newPrice = 0;

        final String REST_URI = "https://api.exchangeratesapi.io/latest";

        Response response = client.target(REST_URI).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get;

        ObjectMapper mapper = new ObjectMapper();
        CurrencyExchangeData exchangeRates = mapper.readValue(response.readEntity(String.class), CurrencyExchangeData.class);

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
