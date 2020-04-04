package marketplace.service;

import io.javalin.Javalin;
import marketplace.logic.Logic;
import marketplace.security.AuthenticationService;

public class Service {
    private Logic logic;
    private AuthenticationService authenticationService;

    public Service() {
        this.logic = new Logic();
        this.authenticationService = new AuthenticationService();
    }

    private void start() {
        Javalin app = Javalin.create().start(7000);

        app.post("/login", ctx -> {
            String securityKey = authenticationService.createNewSecurityKey(10, 2);
            ctx.header("Server-Response",logic.login(ctx.queryParam("username"), ctx.queryParam("passwordHash")));
            ctx.result(securityKey);
                }
           );

//        app.post("/registration", ctx ->
//                ctx.result(logic.registration(ctx.queryParam("username"), ctx.queryParam("passwordHash"),
//                        ctx.queryParam("firstName"), ctx.queryParam("lastName"), ctx.queryParam("firstName"),
//                        ctx.queryParam("address"), ctx.queryParam("phone"), ctx.queryParam("email"))));
    }

    public static void main(String[] args) {
        new Service().start();
    }
}
