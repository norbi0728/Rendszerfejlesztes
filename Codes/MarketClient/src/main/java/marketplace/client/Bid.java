package marketplace.client;

import java.util.Date;

public class Bid {
    String userName;
    int value;
    int listingID;
    int id;
    Date date;

    public Bid(String userName, int value, int listingID, Date date) {
        this.userName = userName;
        this.value = value;
        this.listingID = listingID;
        this.date = date;
    }

    public Bid(int id, String userName, int value, int listingID, Date date) {
        this.id = id;
        this.userName = userName;
        this.value = value;
        this.listingID = listingID;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }

    public int getValue() {
        return value;
    }

    public int getListingID() {
        return listingID;
    }

    public int getId() {
        return id;
    }

    public Bid() {
    }
}
