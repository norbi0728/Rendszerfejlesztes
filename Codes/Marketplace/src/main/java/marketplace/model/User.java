package marketplace.model;

import java.sql.Statement;
import java.util.List;

public class User {
    private String name; //username
    private String passwordHash;
    private PersonalInformation personalInformation;
    private Statistics statistics;
    private List<Listing> listings;
    private String preferredCurrency;

    //must have because of serialization
    public User() {
    }

    public User(String name, String passwordHash, PersonalInformation personalInformation,
                Statistics statistics, List<Listing> listings, String preferredCurrency) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.personalInformation = personalInformation;
        this.statistics = statistics;
        this.listings = listings;
        this.preferredCurrency = preferredCurrency;
    }
    //use when it comes from registration
    public User(String name, String passwordHash, PersonalInformation personalInformation, String preferredCurrency) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.personalInformation = personalInformation;
        this.statistics = new Statistics();
        this.preferredCurrency = preferredCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }
}
