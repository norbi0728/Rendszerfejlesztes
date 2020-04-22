package marketplace.client;

import java.util.Map;

public class Listing {
    private String title;
    private String description;
    private Item item;

    public Listing(String title, String description, Item item) {
        this.title = title;
        this.description = description;
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
