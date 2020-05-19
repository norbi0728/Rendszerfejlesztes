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
            case "HUF":
                newPrice = price;
                break;
            case "EUR":
                newPrice = price / EURRate;
                break;
            case "USD":
                newPrice = price / USDRate;
                break;
            case "GBP":
                newPrice = price / GBPRate;
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
