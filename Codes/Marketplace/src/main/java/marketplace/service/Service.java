package marketplace.service;

import io.javalin.Javalin;
import marketplace.logic.BidLogic;
import marketplace.logic.ListingLogic;
import marketplace.logic.LoginReg;
import marketplace.logic.UserManagement;
import marketplace.model.Listing;
import marketplace.model.PersonalInformation;
import marketplace.model.User;
import marketplace.security.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private BidLogic bidLogic;
    private UserManagement userManagement;
    private LoginReg loginReg;
    private ListingLogic listingLogic;
    private AuthenticationService authenticationService;
    private Map<String, String> users;//we need to identify the users, so map their security key to their name

    public Service() {
        this.bidLogic = new BidLogic();
        this.userManagement = new UserManagement();
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
                        ctx.queryParam("address"), ctx.queryParam("phone"), ctx.queryParam("email"),
                        ctx.queryParam("preferredCurrency"))));

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
        app.post("/removeListing",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.header("Server-Response",listingLogic
                        .removeListing(users.get(securityKey),
                                Integer.valueOf(ctx.queryParam("listingID"))));
            }
        });
//        app.post("/removeBid",ctx -> {
//            String securityKey = ctx.queryParam("securityKey");
//            if(authenticationService.validateKey(securityKey)){
//                ctx.header("Server-Response",listingLogic
//                        .removeBid(Integer.valueOf(ctx.queryParam("bidID"))));
//            }
//        });
//        public String addBid(String userName, int value, int listingId){
        app.post("/addBid",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.header("Server-Response",bidLogic
                        .addBid(users.get(securityKey),
                                Integer.valueOf(ctx.queryParam("value")),
                                Integer.valueOf(ctx.queryParam("listingID"))));
            }
        });
        app.post("/setPersonalInformation",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                User user = new User();
                user.setName(users.get(securityKey));
                user.setPersonalInformation(new PersonalInformation(
                        ctx.queryParam("firstName"),
                        ctx.queryParam("lastName"),
                        ctx.queryParam("address"),
                        ctx.queryParam("phone"),
                        ctx.queryParam("email")
                ));
                ctx.header("Server-Response", userManagement.setPersonalInformations(user));
            }
        });
        app.post("/getPersonalInformation",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(userManagement.getPersonalInformations(users.get(securityKey)));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });

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
