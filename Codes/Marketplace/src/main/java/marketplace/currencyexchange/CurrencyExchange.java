package marketplace.currencyexchange;

import java.io.IOException;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public class CurrencyExchange
{
    public double getExchangeRates(int price, String currency) throws IOException, InterruptedException
    {
        double newPrice = 0;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.exchangeratesapi.io/latest")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());

        String responseString = response.body();

        ObjectMapper mapper = new ObjectMapper();
        CurrencyExchangeData exchangeRates = mapper.readValue(responseString, CurrencyExchangeData.class);

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
