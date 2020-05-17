package marketplace.client.model;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    ELECTRICAL("Elektromos berendez�sek", "Electrical"),
    SPORT("Sport", "Sport"),
    VEHICLE_AND_PARTS("J�rm�vek �s j�rm�alkatr�szek", "Vehicle and Parts"),
    BEAUTYCARE("Sz�ps�g�pol�s", "Beautycare"),
    HOME("Otthon", "Home"),
    GATHERING("Gy�jt�i t�rgyak", "Gathering");

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
