package marketplace.client.model;

public enum PaymentMethod {
    CASH("Készpénz", "cash"),
    TRANSFER("Átutalás", "transfer");

    private String hungarianName;
    private String englishName;
    PaymentMethod(String hungarianName, String englishName) {
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
