package marketplace.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Listing {
    private int id;
    private String title;
    private String description;
    private int quantity;
    private Item item;
    private String advertiser; //the username that published the listing
    private int increment;
    private int maximumBid;
    private int startingBid;
    private int fixedPrice;
    private Date expirationDate;
    private Date creationDate;
    private Date removeDate;
    private String paymentMethod;
    private String shippingMethod;
    private List<Bid> bids;

    public Listing() {
    }

    public Listing(int id, String title, String description,
                   int quantity, Item item, String advertiser,
                   int increment, int maximumBid, int startingBid,
                   int fixedPrice, Date expirationDate, Date creationDate,
                   Date removeDate, String paymentMethod, String shippingMethod, List<Bid> bids) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.item = item;
        this.advertiser = advertiser;
        this.increment = increment;
        this.maximumBid = maximumBid;
        this.startingBid = startingBid;
        this.fixedPrice = fixedPrice;
        this.expirationDate = expirationDate;
        this.creationDate = creationDate;
        this.removeDate = removeDate;
        this.paymentMethod = paymentMethod;
        this.shippingMethod = shippingMethod;
        this.bids = bids;
    }

    public Listing(String title, String description,
                   int quantity, Item item, String advertiser,
                   int increment, int maximumBid, int startingBid,
                   int fixedPrice, Date expirationDate, Date creationDate,
                   Date removeDate, String paymentMethod, String shippingMethod) {
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.item = item;
        this.advertiser = advertiser;
        this.increment = increment;
        this.maximumBid = maximumBid;
        this.startingBid = startingBid;
        this.fixedPrice = fixedPrice;
        this.expirationDate = expirationDate;
        this.creationDate = creationDate;
        this.removeDate = removeDate;
        this.paymentMethod = paymentMethod;
        this.shippingMethod = shippingMethod;
        this.bids = new ArrayList<>();
    }
    public Bid getMostRecentBid(){
        bids.sort((o1, o2) -> {
            return o2.value - o1.value;
        });

        return bids.get(0);
    }

    public void bid(String userName, int value, Date date){
        bids.add(new Bid(userName, value, id, date));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getMaximumBid() {
        return maximumBid;
    }

    public void setMaximumBid(int maximumBid) {
        this.maximumBid = maximumBid;
    }

    public int getStartingBid() {
        return startingBid;
    }

    public void setStartingBid(int startingBid) {
        this.startingBid = startingBid;
    }

    public int getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(int fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public List<Bid> getBids() {
        return bids;
    }
}
