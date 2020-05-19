package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.PersonalInformation;
import marketplace.model.Statistics;
import marketplace.model.User;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagement {
    static void ki(Object a){
        System.out.println(a);
    }   //Lusta vagyok

    /*
    * Returns null if there is no User with the given name
    * */
    public PersonalInformation getPersonalInformations(String userName){
        Database db = Database.getDatabase();
        if(db.userExists(userName)){
            return db.getUser(userName).getPersonalInformation();
        }
        return null;
    }

    public String setPersonalInformations(String userName, PersonalInformation personalInformations){
        Database db = Database.getDatabase();
        if(db.userExists(userName)) {
            if (!personalInformations.equals(db.getUser(userName).getPersonalInformation())) {
                User temp = db.getUser(userName);
                temp.setPersonalInformation(personalInformations);
                db.updatePersonalInformations(temp);
                return "Done";
            }
            return "Nothing has changed";
        }
        return "The user does not exist!";
    }

    /*
    * Implemented events:
    * onList (when list by category),
    * onClick (when click on a listing),
    * onBid (when make a bid),
    * onAddListing (when create a listing),
    * onWin (when successfully wins an auction),
    * onDidNotGet (when made a bid on an auction but lost it)*/
    public String updateUserStatistic(String userName, String category, String event){
        Database db = Database.getDatabase();
        List<String> categories = Arrays.asList("Electrical",
                "Sport", "Vehicle and Parts", "Beautycare",
                "Cultural", "Home", "Gathering");
        if (db.userExists(userName)) {
            if (categories.contains(category)) {
                User me = db.getUser(userName);
                Map<String, Double> temp = new HashMap<>();
                switch (event) {
                    case "onList":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 0.1);
                        me.getStatistics().setStat(temp);
                        break;
                    case "onClick":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 30);
                        me.getStatistics().setStat(temp);
                        break;
                    case "onBid":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 50);
                        me.getStatistics().setStat(temp);
                        break;
                    case "onAddListing":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 0.5);
                        me.getStatistics().setStat(temp);
                        break;
                    case "onWin":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 1);
                        me.getStatistics().setStat(temp);
                        break;
                    case "onDidNotGet":
                        temp = me.getStatistics().getStats();
                        temp.put(category, temp.get(category) + 1.2);
                        me.getStatistics().setStat(temp);
                        break;
                    default:
                        return "There is no such event yet!";
                }
                db.updateUserStatistics(me);
                return "Done";
            }
            return "No such a category in the database!";
        }
        return "There is no such a user in the database!";
    }

    public static void main(String[] args) throws InterruptedException {
        Integer passHash = "P@ssw0rd".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail", "HUF");
        new User("Try", passHash.toString(), mine);
    }
}
