package marketplace.logic;

import marketplace.database.Database;

import java.util.HashMap;
import java.util.Map;

public class Logic {
    private Map<String, Integer> loginsTries = new HashMap<>();

    public Integer login(String userName, String passwordHash){
        loginsTries.put(userName, 0);
        if (Database.getDatabase().getPasswordHash(userName) == passwordHash){
            return 1;
        } else {
            loginsTries.put(userName, loginsTries.get(userName) + 1);
            if (loginsTries.get(userName) > 3){
                return 30;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        Logic logic = new Logic();

    }
}