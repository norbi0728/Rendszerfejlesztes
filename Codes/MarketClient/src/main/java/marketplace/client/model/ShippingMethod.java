package marketplace.client.model;

public enum ShippingMethod {
    PERSONAL("Személyes átvétel", "personal"),
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

    public static ShippingMethod forName(String name) {
        for (ShippingMethod myEnum : ShippingMethod.values()) {
            if (myEnum.englishName.equals(name)) return myEnum;
        }
        return null;
    }

    @Override
    public String toString() {
        return hungarianName;
    }
}
