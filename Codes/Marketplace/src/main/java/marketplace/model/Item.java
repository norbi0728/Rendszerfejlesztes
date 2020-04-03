package marketplace.model;

import java.util.List;
import java.util.Map;

public class Item {
    private int id;
    private String name;
    private Map<String, String> features;
    private List<Picture> pictures;
    private String category;

    public Item() {
    }
    //if it's read from the database
    public Item(int id, String name, Map<String, String> features, List<Picture> pictures, String category) {
        this.id = id;
        this.name = name;
        this.features = features;
        this.pictures = pictures;
        this.category = category;
    }

    //if it comes from the client
    public Item(String name, Map<String, String> features, List<Picture> pictures, String category) {
        this.name = name;
        this.features = features;
        this.pictures = pictures;
        this.category = category;
    }

    public Map<String, String> getFeatures() {
        return features;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
