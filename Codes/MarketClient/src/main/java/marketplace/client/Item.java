package marketplace.client;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Item {
    private int id;
    private String name;
    private Map<String, String> features;
    private List<Picture> pictures;
    private String category;

    public Item() {}

    public Item(String name, Map<String, String> features, List<Picture> pictures, String category) {
        this.id = new Random().nextInt(Integer.MAX_VALUE);
        this.name = name;
        this.features = features;
        this.pictures = pictures;
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

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

