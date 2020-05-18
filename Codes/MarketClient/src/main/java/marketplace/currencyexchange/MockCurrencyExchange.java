package marketplace.currencyexchange;

import marketplace.client.currencycomponents.Currency;

import java.io.IOException;

public class MockCurrencyExchange implements CurrencyExchange {
    double EURRate = 350.88;
    double USDRate = 323.23;
    double GBPRate = 396.86;
    CurrencyExchange realCurrencyExchange = new RealCurrencyExchange();

    @Override
    public double getExchangeRates(int price, String currency) throws IOException, InterruptedException {
        double newPrice = 0;

        switch (currency)
        {
            case "EUR":
                newPrice = price / 350.88; // TODO use the fields
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

    public void refreshRates() {
        try {
            EURRate = 1 / realCurrencyExchange.getExchangeRates(1, "EUR");
            USDRate = 1 / realCurrencyExchange.getExchangeRates(1, "USD");
            GBPRate = 1 / realCurrencyExchange.getExchangeRates(1, "GBP");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
