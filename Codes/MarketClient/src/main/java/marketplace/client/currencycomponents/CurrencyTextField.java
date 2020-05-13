package marketplace.client.currencycomponents;

import javafx.scene.control.TextField;
import marketplace.currencyexchange.CurrencyExchange;

public class CurrencyTextField extends TextField {
    private CurrencyExchange currencyExchange;
    private double valueInHUF;

    public CurrencyTextField(CurrencyExchange currencyExchange) {
        this.currencyExchange = currencyExchange;
    }
}
