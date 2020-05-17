package marketplace.client.currencycomponents;

public enum Currency {
    HUF,
    EUR,
    USD,
    GBP;

    public static Currency forName(String name) {
        if (name == null) return null;
        switch (name) {
            case "EUR":
                return Currency.EUR;
            case "USD":
                return Currency.USD;
            case "GBP":
                return Currency.GBP;
            case "HUF":
                return Currency.HUF;
        }
        return null;
    }
}
