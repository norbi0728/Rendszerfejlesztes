package marketplace.client.currencycomponents;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import marketplace.currencyexchange.CurrencyExchange;
import marketplace.currencyexchange.MockCurrencyExchange;
import marketplace.currencyexchange.RealCurrencyExchange;

import java.util.ArrayList;
import java.util.List;

public class CurrencyChanger {
    private CurrencyExchange currencyExchange;
    private List<TextField> textFields = new ArrayList<>();
    private List<Label> labels = new ArrayList<>();
    public Currency currency = Currency.HUF;
    private static CurrencyChanger instance;

    private CurrencyChanger() {
        currencyExchange = new RealCurrencyExchange();
    }

    public void changeDisplayCurrency(Currency currency) {
        Currency lastCurrency = this.currency;
        this.currency = currency;
        for (TextField textField : textFields) {
            try {
                textField.setText(transformValue(textField.getText(), lastCurrency, currency));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (Label label : labels) {
            label.setText(currency.toString());
        }
    }

    private String transformValue(String oldValueText, Currency fromCurrency, Currency toCurrency) throws Exception {
        double oldValue = Double.valueOf(oldValueText);
        double forints;
        if (fromCurrency == Currency.HUF) {
            forints = oldValue;
        } else {
            forints = oldValue / currencyExchange.getExchangeRates(1, fromCurrency.toString());
        }
        double newValue;
        if (toCurrency == Currency.HUF) {
            newValue = forints;
        } else {
            newValue = currencyExchange.getExchangeRates((int) forints, toCurrency.toString());
        }
        return String.valueOf(((double) (int) (newValue * 100)) / 100);
    }

    private int inHUF(TextField textField) {
        double value = Double.valueOf(textField.getText());
        double forints;
        if (currency == Currency.HUF) {
            forints = value;
        } else {
            try {
                forints = value / currencyExchange.getExchangeRates(1, currency.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (int) forints;
    }

    public String inChosenCurrency(int forints) {
        try {
            double newValue = currencyExchange.getExchangeRates((int) forints, currency.toString());
            return String.valueOf(((double) (int) (newValue * 100)) / 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addTextField(TextField textField) {
        textFields.add(textField);
    }

    public void addCurrencyLabel(Label label) {
        labels.add(label);
        label.setText(currency.toString());
    }

    public static CurrencyChanger getInstance() {
        if (instance == null) {
            instance = new CurrencyChanger();
        }
        return instance;
    }
}
