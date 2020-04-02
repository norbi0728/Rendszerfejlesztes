package marketplace.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, Date> securityKeys;
    private SecurityKeyGenerator securityKeyGenerator;

    public AuthenticationService() {
        this.securityKeys = new HashMap<>();
        this.securityKeyGenerator = new SecurityKeyGenerator();
    }

    public String createNewSecurityKey(int keyLength, int durationInHours){
        String securityKey = securityKeyGenerator.randomAlphaNumeric(keyLength);
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * durationInHours);

        //make sure to be unique
        //due to the randomness, it could generate an existing key
        while(securityKeys.containsKey(securityKey))
            securityKey = securityKeyGenerator.randomAlphaNumeric(keyLength);

        securityKeys.put(securityKey, expiration);
        return securityKey;
    }

    public boolean validateKey(String key){
        //if the expiration date of the examined key is after the current time, then it's ok
        if (securityKeys.containsKey(key) && securityKeys.get(key).after(new Date(System.currentTimeMillis())))
            return true;

        return false;
    }
}
