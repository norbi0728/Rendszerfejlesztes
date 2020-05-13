package marketplace.client.model;

public enum Category {
    ELECTRICAL("Elektromos berendezések", "Electrical"),
    SPORT("Sport", "Sport"),
    VEHICLE_AND_PARTS("Jármûvek és jármûalkatrészek", "Vehicle and Parts"),
    BEAUTYCARE("Szépségápolás", "Beautycare"),
    HOME("Otthon", "Home"),
    GATHERING("Gyûjtõi tárgyak", "Gathering");

    private String hungarianName;
    private String englishName;
    Category(String hungarianName, String englishName) {
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
