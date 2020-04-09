package marketplace.model;

import java.sql.Statement;
import java.util.List;

public class User {
    private String name; //username
    private String passwordHash;
    private PersonalInformation personalInformation;
    private Statistics statistics;
    private List<Listing> listings;

    public User() {
    }

    public User(String name, String passwordHash, PersonalInformation personalInformation,
                Statistics statistics, List<Listing> listings) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.personalInformation = personalInformation;
        this.statistics = statistics;
        this.listings = listings;
    }
    //use when it comes from registration
    public User(String name, String passwordHash, PersonalInformation personalInformation) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.personalInformation = personalInformation;
        this.statistics = new Statistics();
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
}
