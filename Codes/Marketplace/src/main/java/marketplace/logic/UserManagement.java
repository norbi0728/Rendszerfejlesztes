package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.PersonalInformation;
import marketplace.model.User;

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

    public String setPersonalInformations(User user){
        Database db = Database.getDatabase();
        if(!user.getPersonalInformation().equals(db.getUser(user.getName()).getPersonalInformation())){
            if(user.getPersonalInformation().getAddress().equals(db.getUser(user.getName()).getPersonalInformation().getAddress())){
                return "Setterek kellenek";
            }
        } else {
            return "Nothing has changed";
        }
        return "default";
    }

    public static void main(String[] args) throws InterruptedException {
        Integer passHash = "P@ssw0rd".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail");
        new User("Try", passHash.toString(), mine);
    }
}
