package marketplace.client;

import java.util.Map;

public class Listing {
    private String title;
    private String description;
    private Map<String, String> features;

    public Listing(String title, String description, Map<String, String> features) {
        this.title = title;
        this.description = description;
        this.features = features;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getFeatures() {
        return features;
    }
}
