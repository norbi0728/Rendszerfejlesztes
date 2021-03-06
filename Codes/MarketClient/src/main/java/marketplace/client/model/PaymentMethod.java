package marketplace.client.model;

public enum PaymentMethod {
    CASH("K�szp�nz", "cash"),
    TRANSFER("�tutal�s", "transfer");

    private String hungarianName;
    private String englishName;
    PaymentMethod(String hungarianName, String englishName) {
        this.hungarianName = hungarianName;
        this.englishName = englishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public static PaymentMethod forName(String name) {
        for (PaymentMethod myEnum : PaymentMethod.values()) {
            if (myEnum.englishName.equals(name)) return myEnum;
        }
        return null;
    }

    @Override
    public String toString() {
        return hungarianName;
    }
}
