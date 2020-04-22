package marketplace.client;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Item {
    private int id;
    private String name;
    private Map<String, String> features;
    private String picture;
    private String category;

    public Item(int id, String name, Map<String, String> features, String picture, String category) {
        this.id = id;
        this.name = name;
        this.features = features;
        this.picture = picture;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

