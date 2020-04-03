package marketplace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private List<String> categories;
    private Map<String, Double> stats;

    public Statistics() {
        stats = new HashMap<>();
        categories = new ArrayList<>();
        categories.add("Electrical");
        categories.add("Sport");
        categories.add("Vehicle and Parts");
        categories.add("Beautycare");
        categories.add("Cultural");
        categories.add("Home");
        categories.add("Gathering");

        for (String cat: categories){
            stats.put(cat, 0.0);
        }
    }

    public Map<String, Double> getStats() {
        return stats;
    }

}
