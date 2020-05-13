package marketplace.currencyexchange;

import java.io.IOException;

public class MockCurrencyExchange implements CurrencyExchange {
    @Override
    public double getExchangeRates(int price, String currency) throws IOException, InterruptedException {
        double newPrice = 0;

        switch (currency)
        {
            case "EUR":
                newPrice = price / 350.88;
                break;
            case "USD":
                newPrice = price / 323.23;
                break;
            case "GBP":
                newPrice = price / 396.86;
                break;
        }
        return newPrice;
    }
}
