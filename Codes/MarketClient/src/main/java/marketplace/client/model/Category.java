package marketplace.client.model;

import java.util.HashMap;
import java.util.Map;

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

    public static Category forName(String name) {
        for (Category category : Category.values()) {
            if (category.englishName.equals(name)) return category;
        }
        return null;
    }

    @Override
    public String toString() {
        return hungarianName;
    }
}
