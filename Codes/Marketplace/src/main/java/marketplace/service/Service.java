package marketplace.service;

import io.javalin.Javalin;
import marketplace.logic.ListingLogic;
import marketplace.logic.LoginReg;
import marketplace.model.Listing;
import marketplace.security.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private LoginReg loginReg;
    private ListingLogic listingLogic;
    private AuthenticationService authenticationService;
    private Map<String, String> users;//we need to identify the users, so map their security key to their name

    public Service() {
        this.listingLogic = new ListingLogic();
        this.loginReg = new LoginReg();
        this.authenticationService = new AuthenticationService();
        this.users = new HashMap<>();
    }

    private void start() {
        Javalin app = Javalin.create().start(7000);

        app.post("/login", ctx -> {
            String username = ctx.queryParam("username");
            String securityKey = authenticationService.createNewSecurityKey(10, 2);
            users.put(securityKey, username);
            ctx.header("Server-Response", loginReg.login(username, ctx.queryParam("passwordHash")));
            ctx.result(securityKey);
                }
           );

        app.post("/registration", ctx ->
                ctx.result(loginReg.registration(ctx.queryParam("username"), ctx.queryParam("passwordHash"),
                        ctx.queryParam("firstName"), ctx.queryParam("lastName"),
                        ctx.queryParam("address"), ctx.queryParam("phone"), ctx.queryParam("email"))));

        app.post("/getUserListings", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
               ctx.json(listingLogic.listListings(users.get(securityKey)));
            }
            else
               ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/addListing", ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                Listing listing = ctx.bodyAsClass(Listing.class);
                ctx.header("Server-Response", listingLogic.create(users.get(securityKey),listing));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });
//        app.post("removeListing",ctx -> {
//            String securityKey = ctx.queryParam("securityKey");
//            if(authenticationService.validateKey(securityKey)){
//                ctx.header("Server-Response",listingLogic.remove(ctx.queryParam("listingID")));
//            }
//        });

    }

    public static void main(String[] args) {
        //uncomment if you want to test the personalised offer function
//        try {
//            Process process = new ProcessBuilder("clusterService.exe").start();
//            Thread.sleep(40000); //cluster service needs time to start
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        new Service().start();
    }
}
