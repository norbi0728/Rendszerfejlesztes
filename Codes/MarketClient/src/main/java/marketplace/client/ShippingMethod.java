package marketplace.client;

public enum ShippingMethod {
    PERSONAL("Szem�lyes �tv�tel", "personal"),
    POSTAL("Posta", "postal");

    private String hungarianName;
    private String englishName;
    ShippingMethod(String hungarianName, String englishName) {
        this.hungarianName = hungarianName;
        this.englishName = englishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    @Override
    public String toString() {
        return hungarianName;
    }
}
