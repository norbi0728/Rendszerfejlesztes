package marketplace.database;

import marketplace.model.*;
import marketplace.service.PersonalisedOfferServiceClient;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Database {
    public static Database database = null;
    private Connection connection;

    private Database(String name) {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:file:" + name);
            createTablesIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database("./mydatabase");
        }
        return database;
    }

    public void clearTables() throws SQLException {
        connection.createStatement().execute(
                "DROP TABLE USERS"
        );
    }

    /**USER RELATED STUFF*/

    public boolean userExists(String name) {
        return (getPasswordHash(name) != null);
    }

    /**
     * You need to make sure that no such user exists yet.
     */

    public Statistics getStatistics(String userName) {
        Statistics statistics = new Statistics();
        ResultSet statResultSet;
        try {
            statResultSet = connection.createStatement().executeQuery(
                    "SELECT C.NAME, VALUE " +
                            "FROM USERS U " +
                            "JOIN USER_STATS US on U.NAME = US.USER_NAME " +
                            "JOIN CATEGORIES C on US.CATEGORY_ID = C.ID " +
                            "WHERE U.NAME = '" + userName + "'"
            );
            while (statResultSet.next()) {
                statistics.getStats().put(statResultSet.getString("NAME"),
                        statResultSet.getDouble("VALUE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }

    public User getUser(String userName){
        User user = null;
        try {
            ResultSet userResultSet = connection.createStatement().executeQuery(
                    "SELECT PASSWORD_HASH " +
                            " FROM USERS" +
                            " WHERE NAME = '" + userName + "'");
            userResultSet.first();
            String passwordHash = userResultSet.getString("PASSWORD_HASH");
            user = new User(userName, passwordHash, getPersonalInformation(userName),
                    getStatistics(userName), getListings(userName));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> resultList = new ArrayList<>();

        try {
            ResultSet userResultSet = connection.createStatement().executeQuery(
                    "SELECT *" +
                            " FROM USERS");
            while (userResultSet.next()) {

                String userName = userResultSet.getString("NAME");
                String passwordHash = userResultSet.getString("PASSWORD_HASH");
                resultList.add(new User(userName, passwordHash, getPersonalInformation(userName),
                        getStatistics(userName), getListings(userName)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultList;
    }
    public void updatePersonalInformations(User user){
        try {
            String sql = "UPDATE PERSONAL_INFORMATIONS " +
                    "SET FIRST_NAME = ?," +
                    "LAST_NAME = ?," +
                    "ADDRESS = ?, " +
                    "PHONE = ?, " +
                    "EMAIL = ?, " +
                    "PREFERRED_CURRENCY = ? " +
                    "WHERE USER_NAME = '" + user.getName() + "'";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getPersonalInformation().getFirstName());
            preparedStatement.setString(2, user.getPersonalInformation().getLastName());
            preparedStatement.setString(3, user.getPersonalInformation().getAddress());
            preparedStatement.setString(4, user.getPersonalInformation().getPhone());
            preparedStatement.setString(5, user.getPersonalInformation().getEmail());
            preparedStatement.setString(6, user.getPersonalInformation().getPreferredCurrency());

            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateUserStatistics(User user){
        try {
            for(Map.Entry<String, Double> stat: user.getStatistics().getStats().entrySet()){
                String sql = "UPDATE USER_STATS " +
                        "SET VALUE = ? " +
                        "WHERE CATEGORY_ID = " + getCategoryID(stat.getKey()) + " AND USER_NAME = '" + user.getName() + "'";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, stat.getValue());

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private void addUserStatistics(User user){
        try {
            for(Map.Entry<String, Double> stat: user.getStatistics().getStats().entrySet()){
                String sql = "INSERT INTO USER_STATS (USER_NAME, CATEGORY_ID, VALUE) VALUES(?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, getCategoryID(stat.getKey()));
                preparedStatement.setDouble(3, stat.getValue());

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void addUser(User user) {
        try {
            String sql = "INSERT INTO USERS (NAME, PASSWORD_HASH) VALUES(?, ?);"
                    + "INSERT INTO PERSONAL_INFORMATIONS (USER_NAME, FIRST_NAME," +
                    " LAST_NAME, ADDRESS," +
                    " PHONE, EMAIL, PREFERRED_CURRENCY) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPasswordHash());


            preparedStatement.setString(3, user.getName()); //username
            preparedStatement.setString(4, user.getPersonalInformation().getFirstName());
            preparedStatement.setString(5, user.getPersonalInformation().getLastName());
            preparedStatement.setString(6, user.getPersonalInformation().getAddress());
            preparedStatement.setString(7, user.getPersonalInformation().getPhone());
            preparedStatement.setString(8, user.getPersonalInformation().getEmail());
            preparedStatement.setString(9, user.getPersonalInformation().getPreferredCurrency());

            preparedStatement.executeUpdate();
            addUserStatistics(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return null, if no such user
     */
    public String getPasswordHash(String userName) {
        String result;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT * FROM USERS WHERE NAME ='" + userName + "'"
            );
            resultSet.first();
            result = resultSet.getString("PASSWORD_HASH");
        } catch (SQLException e) {
            return null;
        }
        return result;
    }

    private PersonalInformation getPersonalInformation(String userName) {
        ResultSet personalInformationResultSet;
        String firstName = "";
        String lastName = "";
        String address = "";
        String phone = "";
        String email = "";
        String preferredCurrency = "";
        try {
            personalInformationResultSet = connection.createStatement().executeQuery(
                    "SELECT *" +
                            " FROM USERS U" +
                            " JOIN PERSONAL_INFORMATIONS P ON P.USER_NAME = U.NAME" +
                            " WHERE USER_NAME = '" + userName + "'"
            );
            personalInformationResultSet.first();
            firstName = personalInformationResultSet.getString("FIRST_NAME");
            lastName = personalInformationResultSet.getString("LAST_NAME");
            address = personalInformationResultSet.getString("ADDRESS");
            phone = personalInformationResultSet.getString("PHONE");
            email = personalInformationResultSet.getString("EMAIL");
            preferredCurrency = personalInformationResultSet.getString("PREFERRED_CURRENCY");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PersonalInformation(firstName, lastName, address, phone, email, preferredCurrency);
    }

    /**LISTING RELATED STUFF*/

    public List<Listing> getListings(String userName) {
        List<Listing> listings = new ArrayList<>();
        ResultSet listingResultSet;
        int id = 0;
        String title = "";
        int quantity = 0;
        String description = "";
        int itemID = 0;
        int increment = 0;
        int maximumBid = 0;
        int startingBid = 0;
        int fixedPrice = 0;
        Date expirationDate = null;
        Date creationDate = null;
        Date removeDate = null;
        String paymentMethod = "";
        String shippingMethod = "";
        try {
            listingResultSet = connection.createStatement().executeQuery(
                    "SELECT *" +
                            "FROM LISTINGS " +
                            "WHERE USER_NAME = '" + userName + "'"
            );
            while (listingResultSet.next()) {
                id = listingResultSet.getInt("ID");
                title = listingResultSet.getString("TITLE");
                quantity = listingResultSet.getInt("QUANTITY");
                description = listingResultSet.getString("DESCRIPTION");
                itemID = listingResultSet.getInt("ITEM_ID");
                increment = listingResultSet.getInt("INCREMENT");
                maximumBid = listingResultSet.getInt("MAXIMUM_BID");
                startingBid = listingResultSet.getInt("STARTING_BID");
                fixedPrice = listingResultSet.getInt("FIXED_PRICE");
                expirationDate = listingResultSet.getDate("EXPIRATION_DATE");
                creationDate = listingResultSet.getDate("CREATED_ON");
                removeDate = listingResultSet.getDate("REMOVED_ON");
                paymentMethod = listingResultSet.getString("PAYMENT_METHOD");
                shippingMethod = listingResultSet.getString("SHIPPING_METHOD");

                listings.add(new Listing(id, title, description, quantity, getItem(itemID), userName,
                        increment, maximumBid, startingBid, fixedPrice, expirationDate,
                        creationDate, removeDate, paymentMethod, shippingMethod, getBids(id)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listings;
    }
    public List<Listing> getAllListing() {
        List<Listing> listings = new ArrayList<>();
        ResultSet listingResultSet;
        int id = 0;
        String title = "";
        String userName = "";
        int quantity = 0;
        String description = "";
        int itemID = 0;
        int increment = 0;
        int maximumBid = 0;
        int startingBid = 0;
        int fixedPrice = 0;
        Date expirationDate = null;
        Date creationDate = null;
        Date removeDate = null;
        String paymentMethod = "";
        String shippingMethod = "";
        try {
            listingResultSet = connection.createStatement().executeQuery(
                    "SELECT *" +
                            "FROM LISTINGS"
            );
            while (listingResultSet.next()) {
                id = listingResultSet.getInt("ID");
                title = listingResultSet.getString("TITLE");
                userName = listingResultSet.getString("USER_NAME");
                quantity = listingResultSet.getInt("QUANTITY");
                description = listingResultSet.getString("DESCRIPTION");
                itemID = listingResultSet.getInt("ITEM_ID");
                increment = listingResultSet.getInt("INCREMENT");
                maximumBid = listingResultSet.getInt("MAXIMUM_BID");
                startingBid = listingResultSet.getInt("STARTING_BID");
                fixedPrice = listingResultSet.getInt("FIXED_PRICE");
                expirationDate = listingResultSet.getDate("EXPIRATION_DATE");
                creationDate = listingResultSet.getDate("CREATED_ON");
                removeDate = listingResultSet.getDate("REMOVED_ON");
                paymentMethod = listingResultSet.getString("PAYMENT_METHOD");
                shippingMethod = listingResultSet.getString("SHIPPING_METHOD");

                listings.add(new Listing(id, title, description, quantity, getItem(itemID), userName,
                        increment, maximumBid, startingBid, fixedPrice, expirationDate,
                        creationDate, removeDate, paymentMethod, shippingMethod, getBids(id)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listings;
    }

    public void addListing(Listing listing) {
        try {
            String sql = "INSERT INTO LISTINGS (TITLE, QUANTITY," +
                    " DESCRIPTION, ITEM_ID, USER_NAME, INCREMENT," +
                    " MAXIMUM_BID, STARTING_BID, FIXED_PRICE, EXPIRATION_DATE," +
                    " CREATED_ON, REMOVED_ON, PAYMENT_METHOD, SHIPPING_METHOD)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            addItem(listing.getItem());

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setInt(2, listing.getQuantity());
            preparedStatement.setString(3, listing.getDescription()); //username
            preparedStatement.setInt(4, listing.getItem().getId());
            preparedStatement.setString(5, listing.getAdvertiser());
            preparedStatement.setInt(6, listing.getIncrement());
            preparedStatement.setInt(7, listing.getMaximumBid());
            preparedStatement.setInt(8, listing.getStartingBid());
            preparedStatement.setInt(9, listing.getFixedPrice());
            preparedStatement.setDate(10, new java.sql.Date(listing.getExpirationDate().getTime()));
            preparedStatement.setDate(11, new java.sql.Date(listing.getCreationDate().getTime()));
            preparedStatement.setDate(12, new java.sql.Date(listing.getRemoveDate().getTime()));
            preparedStatement.setString(13, listing.getPaymentMethod());
            preparedStatement.setString(14, listing.getShippingMethod());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void removeListing(int id){
        Item item = getListingByID(id).getItem();
        List<Picture> itemPictures = item.getPictures();
        List<Bid> bids = getListingByID(id).getBids();
        try {
//            for (Picture picture: itemPictures){
//                String sql = "DELETE FROM ITEM_PICTURES WHERE ID = " + picture.getId();
//                connection.createStatement().execute(sql);
//            }
            String deletePictures = "DELETE FROM ITEM_PICTURES WHERE ITEM_ID = " + item.getId();
            connection.createStatement().execute(deletePictures);
            for (Bid bid: bids){
                String sql = "DELETE FROM SALE WHERE BID_ID = " + bid.getId();
                connection.createStatement().execute(sql);
            }

            String sqlBid = "DELETE FROM BIDS WHERE LISTING_ID = " + id;
            connection.createStatement().execute(sqlBid);

            String sqlListing = "DELETE FROM LISTINGS WHERE ID = " + id;
            String sqlItem = "DELETE FROM ITEMS WHERE ID = " + item.getId();
            connection.createStatement().execute(sqlListing);
            connection.createStatement().execute(sqlItem);


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void updateListing(Listing listing){
        try {
            String listingUpdate = "UPDATE LISTINGS SET " +
                    "TITLE = '" + listing.getTitle() + "', " +
                    "QUANTITY = " + listing.getQuantity() + ", " +
                    "DESCRIPTION = '" + listing.getDescription() + "', " +
                    "INCREMENT = " + listing.getIncrement() + ", " +
                    "MAXIMUM_BID = " + listing.getMaximumBid() + ", " +
                    "STARTING_BID = " + listing.getStartingBid() + ", " +
                    "FIXED_PRICE = " + listing.getFixedPrice() + ", " +
                    "EXPIRATION_DATE = '" + listing.getExpirationDate() + "'," +
                    "PAYMENT_METHOD = '" + listing.getPaymentMethod() + "', " +
                    "SHIPPING_METHOD = '" + listing.getShippingMethod() + "' " +
                    "WHERE ID = " + listing.getId();

            String itemUpdate = "UPDATE ITEMS SET " +
                    "NAME = '" + listing.getItem().getName() + "' " +
                    "WHERE ID = " + listing.getItem().getId();

            for (Map.Entry<String, String> feature: listing.getItem().getFeatures().entrySet()){
                String featureUpdate = "UPDATE ITEMS SET " +
                        "FEATURE_VALUE = '" + feature.getValue() + "' " +
                        "WHERE FEATURE = '" + feature.getKey() + "' " +
                        "AND ID = " + listing.getItem().getId();
                connection.createStatement().executeUpdate(featureUpdate);
            }

            String deletePictures = "DELETE FROM ITEM_PICTURES WHERE ITEM_ID = " + listing.getItem().getId();
            connection.createStatement().executeUpdate(deletePictures);
            System.out.println(getItemPictures(listing.getItem().getId()).size());
            addItemPictures(listing.getItem());
            connection.createStatement().executeUpdate(itemUpdate);
            connection.createStatement().executeUpdate(listingUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Listing> getOngoingAuctions(String username){
        List<Listing> ongoing = new ArrayList<>();

        try {
            String queryOngoing = "SELECT LISTINGS.ID FROM LISTINGS " +
                    "JOIN BIDS B on LISTINGS.ID = B.LISTING_ID " +
                    "WHERE B.USER_NAME = '"+ username +"' and EXPIRATION_DATE > DATE";

            ResultSet ongoingRS = connection.createStatement().executeQuery(queryOngoing);
            while (ongoingRS.next()){
                ongoing.add(getListingByID(ongoingRS.getInt("ID")));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ongoing;
    }

    public Listing getListingByID(int id){
        String userName;
        try {
            String sql = "SELECT USER_NAME FROM LISTINGS WHERE ID = " + id;
            ResultSet listingResultSet = connection.createStatement().executeQuery(sql);
            listingResultSet.first();
            userName = listingResultSet.getString("USER_NAME");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (Listing listing: getListings(userName)){
            if(listing.getId() == id)
                return listing;
        }
        return null;
    }

    public void addBid(Bid bid){
        try {
            String sql = "INSERT INTO BIDS (VALUE, USER_NAME, LISTING_ID, DATE) " +
                         "VALUES(?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bid.getValue());
            preparedStatement.setString(2, bid.getUserName());
            preparedStatement.setInt(3, bid.getListingID());
            preparedStatement.setDate(4,
                    new java.sql.Date(bid.getDate().getTime()));
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void removeBid(int bidID){
        try {
            connection.createStatement().execute("DELETE FROM BIDS WHERE ID = " + bidID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Bid> getBids(int listingID){
        List<Bid> bids = new ArrayList<>();
        String userName = "";
        int value = 0;
        int id = 0;
        Date date = null;

        try {
            ResultSet bidResultSet = connection.createStatement().executeQuery(
                    "SELECT ID, VALUE, USER_NAME, DATE " +
                            "FROM BIDS WHERE LISTING_ID = " + listingID
            );

            while (bidResultSet.next()){
                userName = bidResultSet.getString("USER_NAME");
                value = bidResultSet.getInt("VALUE");
                id = bidResultSet.getInt("ID");
                date = bidResultSet.getDate("DATE");

                bids.add(new Bid(id, userName, value, listingID, date));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return bids;
    }
    public void addSale(int bidID){
        try {
            String sql = "INSERT INTO SALE(BID_ID) VALUES(?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bidID);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

        /**ITEM RELATED STUFF*/

    private Item getItem(int id) {
        Map<String, String> features = new HashMap<>();
        List<Picture> pictures = new ArrayList<>();
        String category = "";
        String name = "";
        ResultSet itemResultSet;
        try {
            itemResultSet = connection.createStatement().executeQuery(
                    "SELECT * " +
                            "FROM ITEMS " +
                            "WHERE ID = " + id
            );
            itemResultSet.first();
            name = itemResultSet.getString("NAME");
            features = getItemFeatures(id);
            pictures = getItemPictures(id);
            category = getItemCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Item(id, name, features, pictures, category);
    }

    private void addItem(Item item) {
        try {
            /*first only the category and the name*/
            String sql = "INSERT INTO ITEMS (ID, CATEGORY_ID, NAME)" +
                    " VALUES(?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, item.getId());
            preparedStatement.setInt(2, getCategoryID(item.getCategory()));
            preparedStatement.setString(3, item.getName());

            preparedStatement.executeUpdate();
            /*then get all the features and pictures*/
            addItemFeatures(item);
            addItemPictures(item);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private int getCategoryID(String category) {
        int categoryID = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT ID FROM CATEGORIES WHERE NAME = '" + category + "'"
            );
            resultSet.first();
            categoryID = resultSet.getInt("ID");
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return categoryID;
    }


    /*this function adds the dynamic features to the item*/
    private void addItemFeatures(Item item) {
        String sql = "UPDATE ITEMS " +
                " SET FEATURE = ?," +
                "FEATURE_VALUE = ? " +
                "WHERE ID = " + item.getId();
        try {
            for (Map.Entry<String, String> feature : item.getFeatures().entrySet()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, feature.getKey());
                preparedStatement.setString(2, feature.getValue());

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addItemCategory(String category) {
        try {
            /*first only the category and the name*/
            String sql = "INSERT INTO CATEGORIES (NAME)" +
                    " VALUES(?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, category);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addItemPictures(Item item) {
        String sql = "INSERT INTO ITEM_PICTURES (ITEM_ID, PICTURE) " +
                "VALUES (?, ?)";
        for (Picture pic : item.getPictures()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, item.getId());
                preparedStatement.setBytes(2, pic.getData());

                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private Map<String, String> getItemFeatures(int itemID) {
        Map<String, String> features = new HashMap<>();
        try {
            ResultSet featureResultSet = connection.createStatement().executeQuery(
                    "SELECT FEATURE, FEATURE_VALUE " +
                            "FROM ITEMS " +
                            "WHERE ID = " + itemID
            );

            while (featureResultSet.next()) {
                String name = featureResultSet.getString("FEATURE");
                String value = featureResultSet.getString("FEATURE_VALUE");
                features.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return features;
    }

    private String getItemCategory(int itemID) {
        String category = "";
        ResultSet categoryResultSet;
        try {
            categoryResultSet = connection.createStatement().executeQuery(
                    "SELECT C.NAME " +
                            "FROM CATEGORIES C " +
                            "JOIN ITEMS I ON I.CATEGORY_ID = C.ID " +
                            "WHERE I.ID = " + itemID
            );
            categoryResultSet.first();
            category = categoryResultSet.getString("NAME");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }

    private List<Picture> getItemPictures(int itemID) {
        List<Picture> pictures = new ArrayList<>();
        try {
            ResultSet pictureResultSet = connection.createStatement().executeQuery(
                    "SELECT IP.ID, PICTURE " +
                            "FROM ITEMS I " +
                            "JOIN ITEM_PICTURES IP on IP.ITEM_ID = I.ID " +
                            "WHERE ITEM_ID = " + itemID
            );

            while (pictureResultSet.next()) {
                int pictureID = pictureResultSet.getInt("ID");
                byte[] pictureData = pictureResultSet.getBytes("PICTURE");
                pictures.add(new Picture(pictureID, pictureData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictures;
    }
    /*TABLE CREATION*/
    private final void createTablesIfNotExists() throws SQLException {
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS USERS("
                        + "NAME VARCHAR UNIQUE, "
                        + "PASSWORD_HASH VARCHAR, "
                        + "PRIMARY KEY (NAME))"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS PERSONAL_INFORMATIONS("
                        + "USER_NAME VARCHAR REFERENCES USERS(NAME), "//this binds the personal information to a user
                        + "FIRST_NAME VARCHAR, "
                        + "LAST_NAME VARCHAR, "
                        + "ADDRESS VARCHAR, "
                        + "PHONE VARCHAR,"
                        + "EMAIL VARCHAR)"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS CATEGORIES("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "NAME VARCHAR UNIQUE CHECK(NAME IN("
                        + "'Electrical', 'Sport', 'Vehicle and Parts',"
                        + "'Beautycare', 'Cultural', 'Home', 'Gathering')))"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS FEATURES("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "NAME VARCHAR)"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS ITEMS("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "CATEGORY_ID INT REFERENCES CATEGORIES(ID), "
                        + "NAME VARCHAR, "
                        + "FEATURE_ID INT REFERENCES FEATURES(ID),"
                        + "FEATURE_VALUE VARCHAR)"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS LISTINGS("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "TITLE VARCHAR, "
                        + "QUANTITY INT, "
                        + "DESCRIPTION VARCHAR, "
                        + "ITEM_ID INT REFERENCES ITEMS(ID), "
                        + "USER_NAME VARCHAR REFERENCES USERS(NAME),"
                        + "INCREMENT INT, " //licitlepcso
                        + "MAXIMUM_BID INT, "
                        + "STARTING_BID INT, "
                        + "FIXED_PRICE INT, "
                        + "EXPIRATION_DATE DATE, "
                        + "CREATED_ON DATE, "
                        + "REMOVED_ON DATE, "
                        + "PAYMENT_METHOD VARCHAR CHECK(PAYMENT_METHOD IN('cash', 'transfer')), "
                        + "SHIPPING_METHOD VARCHAR CHECK(SHIPPING_METHOD IN('personal', 'postal')))"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS BIDS("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "VALUE INT, "
                        + "USER_NAME VARCHAR REFERENCES USERS(NAME), "
                        + "LISTING_ID INT REFERENCES LISTINGS(ID), "
                        + "DATE DATETIME)"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS SALE("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "BID_ID INT REFERENCES BIDS(ID))"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS USER_STATS("
                        + "USER_NAME VARCHAR REFERENCES USERS(NAME), "
                        + "CATEGORY_ID INT REFERENCES CATEGORIES(ID) , "
                        + "VALUE FLOAT)"
        );

        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS ITEM_PICTURES("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "ITEM_ID INT REFERENCES ITEMS(ID), "
                        + "PICTURE VARBINARY)"
        );
    }

    public static void main(String[] args) {
        Database database = getDatabase();

//        database.addUser("János", "12345");
//        database.addUser("Péter", "54321");
//        System.out.println(database.getPasswordHash("Jánoss"));
//        database.addUser(new User("testUser2", "testPw2", new PersonalInformation(
//                "testFirstName2", "testLastName2", "testAddress2",
//                "testPhone2", "testEmail2"
//        )));

//        Map<String, String> features = new HashMap<>();
//        features.put("testFeatureName", "testFeatureValue");
//        List<Picture> pictures = new ArrayList<>();
//        Item item = new Item("testItem", features, pictures, "Electrical");
//        database.addListing(new Listing("testTitle", "testDescription",
//                3, item, "testUser",
//                500, 600, 100, 0,
//                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
//                new Date(System.currentTimeMillis()), "cash", "personal"));
//
//        for (Listing listing : database.getListings("testUser")) {
//            System.out.println(listing.getItem().getCategory());
//        }
        //database.removeListing(2);
//        File pic1 = new File("test.png");
//        File pic2 = new File("test2.png");
//        byte[] data1 = new byte[1000];
//        byte[] data2 = new byte[1000];
//        try {
//            data1 = Files.readAllBytes(pic1.toPath());
//            data2 = Files.readAllBytes(pic2.toPath());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Picture picture1 = new Picture(data1);
//        Picture picture2 = new Picture(data2);
//
//        Map<String, String> features = new HashMap<>();
//        features.put("testFeatureName", "testFeatureValue");
//
//        List<Picture> pictures = new ArrayList<>();
//        pictures.add(picture1);
//        pictures.add(picture2);
//
//        Item item = new Item("testItemWithPicture2", features, pictures, "Electrical");
//        database.addListing(new Listing("testTitle2", "testDescription2",
//                3, item, "testUser2",
//                500, 600, 100, 0,
//                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
//                new Date(System.currentTimeMillis()), "cash", "personal"));

//        for (Listing listing : database.getListings("testUser")) {
//            for(Picture picture: listing.getItem().getPictures()){
//                byte[] dataToWrite = picture.getData();
//                try (FileOutputStream stream = new FileOutputStream("testWrittenBack"+picture.getId()+".png")){
//                    stream.write(dataToWrite);
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
//        Process process = null;
//        try {
//             process = new ProcessBuilder("clusterService.exe").start();
//            Thread.sleep(60000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        User user = database.getUser("testUser2");
//        user.getStatistics().getStats().put("Electrical", 11.0);
//        user.getStatistics().getStats().put("Sport", 30.2);
//        user.getStatistics().getStats().put("Cultural", 200.0);
//        user.getStatistics().getStats().put("Vehicle and Parts", 1000.0);
//        user.getStatistics().getStats().put("Gathering", 500.0);
//        user.getStatistics().getStats().put("Home", 50.0);
//        database.updateUserStatistics(user);
//        Map<String, Integer> dispersion = new PersonalisedOfferServiceClient().getDispersion(user);
//        for (Map.Entry<String, Integer> disp: dispersion.entrySet()){
//            System.out.println("key: "+disp.getKey()+", value:"+disp.getValue());
//        }
//        process.destroyForcibly();
//        for (Listing listing: database.getListings("testUser2")){
//            listig.bid("Bela", 300, new Date(System.currentTimeMillis()));
//            database.addBid(new Bid("Bela", 300, listing.getId(), new Date(System.currentTimeMillis())));
//        }
//        for (Listing listig: database.getListings("testUser2")){
//            database.addSale(listig.mostRecentBid().getId());
//        }

//        for (Listing listig: database.getListings("testUser2")){
//            database.removeListing(listig.getId());
//        }
//        Listing listing = database.getListingByID(43);
//        for(int i = 0; i < 4; i++){
//            listing.bid("Bela", 300 + i, new Date(System.currentTimeMillis()));
//            database.addBid(listing.mostRecentBid());
//        }
//        Listing listing = database.getListingByID(43);
//        database.addSale(listing.mostRecentBid().getId());
//        User user = database.getUser("testUser2");
//        user.setPersonalInformation(new PersonalInformation("testFirstName3", "testLastName3",
//                "testAddress3", "06301111111", "test@mail.com"));
//        database.updatePersonalInformations(user);
//        Listing listing = database.getListingByID(250);
//        listing.setDescription("subsequently modified2");
//        listing.getItem().setFeature("testFeatureName", "subsequently modified2");
//        File pic1 = new File("test.png");
//        File pic2 = new File("test2.png");
//        File pic3 = new File("white.png");
//        byte[] data1 = new byte[1000];
//        byte[] data2 = new byte[1000];
//        byte[] data3 = new byte[1000];
//        try {
//            data1 = Files.readAllBytes(pic1.toPath());
//            data2 = Files.readAllBytes(pic2.toPath());
//            data3 = Files.readAllBytes(pic3.toPath());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Picture picture1 = new Picture(data1);
//        Picture picture2 = new Picture(data2);
//        Picture picture3 = new Picture(data3);
//
//        listing.getItem().addPicture(picture1);
//        listing.getItem().addPicture(picture2);
//        listing.getItem().addPicture(picture3);
//
//        database.updateListing(listing);

        for (Listing l:database.getOngoingAuctions("testUser2")){
            System.out.println(l.getCreationDate());
        }

    }
}
