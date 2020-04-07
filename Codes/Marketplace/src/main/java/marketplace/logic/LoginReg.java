package marketplace.logic;

import marketplace.database.Database;
import marketplace.model.PersonalInformation;
import marketplace.model.User;

import java.util.HashMap;
import java.util.Map;

public class LoginReg {
    private Map<String, Integer> loginsTries = new HashMap<>();
    private Map<String, Long> firstTry = new HashMap<>();
    private Map<String, Long> blocked = new HashMap<>();

    static void ki(Object a){
        System.out.println(a);
    }

    private boolean blocker(String userName){
        if (blocked.get(userName) == null || (System.currentTimeMillis() - blocked.get(userName))/1000 > 30){
            if (blocked.get(userName) != null){
                blocked.put(userName, null);
                loginsTries.put(userName, 0);
                firstTry.put(userName, null);
            }
            return false;
        }
        return true;
    }
    /**
     * @return
     * Correct, if the password is correct
     * Wrong username, if there are no such a username in the database
     * Wrong password, if the password is not correct
     * Too many tries, if the password has been mistaken 3 times
     * Prohibited, if user is not allowed to login
     */
    public String login(String userName, String passwordHash){
        if (!blocker(userName)) {
            if (loginsTries.get(userName) == null) {
                loginsTries.put(userName, 0);
            }
            if (Database.getDatabase().getPasswordHash(userName) == null) {
                return "Wrong username";
            } else {
                if (Database.getDatabase().getPasswordHash(userName).equals(passwordHash)) {
                    return "Correct";
                } else {
                    loginsTries.put(userName, (loginsTries.get(userName) + 1));
                    if (loginsTries.get(userName) == 1){
                        firstTry.put(userName, System.currentTimeMillis());
                    } else {
                        if ((System.currentTimeMillis() - firstTry.get(userName)) / 1000 > 30) {
                            firstTry.put(userName, System.currentTimeMillis());
                            loginsTries.put(userName, 1);
                        }
                    }
                    if (loginsTries.get(userName) > 3) {
                        blocked.put(userName, System.currentTimeMillis());
                        return "Too many tries";
                    } else {
                        return "Wrong password";
                    }
                }
            }
        } else {
            return "Prohibited";
        }
    }

    public String registration(String userName, String passHash, String firstName, String lastName, String address, String phone, String email){
        PersonalInformation personalInformation = new PersonalInformation(firstName, lastName, address, phone, email);
        User user = new User(userName, passHash, personalInformation);
        if(Database.getDatabase().userExists(user.getName())){
            return "Existing username";
        } else {
            Database.getDatabase().addUser(user);
            return "Done";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LoginReg loginReg = new LoginReg();
        ki(loginReg.login("János", "12345"));
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
//        Integer i = 4;
//        Long t = i.toUnsignedLong(31);
//        ki(t);
//        TimeUnit.SECONDS.sleep(t);
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
//        TimeUnit.SECONDS.sleep(t);
//        ki(loginReg.login("János", "1234"));
//        ki(loginReg.login("János", "1234"));
    }
}