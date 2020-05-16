package marketplace.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Item {
    private int id;
    private String name;
    private Map<String, String> features;
    private List<Picture> pictures;
    private String category;

    public Item() {
    }

    public String getName() {
        return name;
    }

    //if it's read from the database
    public Item(int id, String name, Map<String, String> features, List<Picture> pictures, String category) {
        this.id = id;
        this.name = name;
        this.features = features;
        this.pictures = pictures;
        this.category = category;
    }

    //if it comes from the client nested in a listing
    //we need to generate it's own id, because other way we cannot bind it to the listing in the database
    public Item(String name, Map<String, String> features, List<Picture> pictures, String category) {
        this.id = new Random().nextInt(Integer.MAX_VALUE);
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

    public void setFeature(String name, String value){
        features.put(name, value);
    }

    public void addPicture(Picture picture){
        boolean isAlreadyAdded = false;
        for (Picture picture1: pictures){
            if (Arrays.equals(picture.getData(), picture1.getData()))
                isAlreadyAdded = true;
        }
        if (!isAlreadyAdded)
            pictures.add(picture);
    }
}
