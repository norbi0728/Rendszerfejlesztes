package marketplace.service;

import io.javalin.Javalin;
import marketplace.database.Database;
import marketplace.logic.*;
import marketplace.model.Listing;
import marketplace.model.PersonalInformation;
import marketplace.model.User;
import marketplace.security.AuthenticationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private PersonalOfferLogic personalOfferLogic;
    private BidLogic bidLogic;
    private UserManagement userManagement;
    private LoginReg loginReg;
    private ListingLogic listingLogic;
    private AuthenticationService authenticationService;
    private Map<String, String> users;//we need to identify the users, so map their security key to their name

    public Service() {
        this.personalOfferLogic = new PersonalOfferLogic();
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
            String result = loginReg.login(username, ctx.queryParam("passwordHash"));
            if (result.equals("Correct")) {
                String securityKey = authenticationService.createNewSecurityKey(10, 2);
                users.put(securityKey, username);
                ctx.header("Server-Response", result);
                ctx.result(securityKey);
            }
            else
                ctx.header("Server-Response", result);
        });


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

        app.post("/getAllListings", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
               ctx.json(listingLogic.listListings());
            }
            else
               ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/getListingsByCategory", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                List<String> categories = ctx.bodyAsClass(List.class);
               ctx.json(listingLogic.listByCategories(categories));
            }
            else
               ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/getListingsByBidOrNot", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(listingLogic.listByBidOrNot(Integer.valueOf(ctx.queryParam("bidOrNot"))));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/getListingsByPrice", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                listingLogic.listByPrice(Integer.valueOf(ctx.queryParam("min")),
                        Integer.valueOf(ctx.queryParam("max")));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/getListingsByUser", ctx ->{
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(listingLogic.listByUser(ctx.queryParam("userName")));
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

        app.post("/updateListing",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                listingLogic.updateListing(ctx.bodyAsClass(Listing.class));
            }
        });

//        app.post("/removeBid",ctx -> {
//            String securityKey = ctx.queryParam("securityKey");
//            if(authenticationService.validateKey(securityKey)){
//                ctx.header("Server-Response",listingLogic
//                        .removeBid(Integer.valueOf(ctx.queryParam("bidID"))));
//            }
//        });

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
                user.setPersonalInformation(
                        ctx.bodyAsClass(PersonalInformation.class)
                );
                ctx.header("Server-Response", userManagement.setPersonalInformations(user.getName(), user.getPersonalInformation()));
            }
        });

        app.post("/getOwnPersonalInformation",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(userManagement.getPersonalInformations(users.get(securityKey)));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });

        app.post("/getPersonalInformation",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(userManagement.getPersonalInformations(ctx.queryParam("userName")));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });
        
        app.post("/getPersonalOffer",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                User user = new User();
                user.setName(users.get(securityKey));
                ctx.json(personalOfferLogic.getPersonalisedOffer(user));
            }
            else
                ctx.header("Server-Response", "Invalid security key");
        });
        app.post("/getOngoingAuctions",ctx -> {
            String securityKey = ctx.queryParam("securityKey");
            if(authenticationService.validateKey(securityKey)){
                ctx.json(listingLogic.getOngoingAuctions(users.get(securityKey)));
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
