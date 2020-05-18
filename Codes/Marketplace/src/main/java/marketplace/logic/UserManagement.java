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

    public static void main(String[] args) throws InterruptedException {
        Integer passHash = "P@ssw0rd".hashCode();
        PersonalInformation mine = new PersonalInformation("Norbert", "Radákovits", "Earth", "007", "@mail", "HUF");
        new User("Try", passHash.toString(), mine);
    }
}
