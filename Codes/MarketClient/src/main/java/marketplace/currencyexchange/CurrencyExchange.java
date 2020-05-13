package marketplace.currencyexchange;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public interface CurrencyExchange {
    double getExchangeRates(int price, String currency) throws IOException, InterruptedException;
}
