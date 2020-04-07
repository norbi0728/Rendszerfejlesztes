package marketplace.service;

import io.javalin.Javalin;
import marketplace.logic.LoginReg;
import marketplace.security.AuthenticationService;

public class Service {
    private LoginReg loginReg;
    private AuthenticationService authenticationService;

    public Service() {
        this.loginReg = new LoginReg();
        this.authenticationService = new AuthenticationService();
    }

    private void start() {
        Javalin app = Javalin.create().start(7000);

        app.post("/login", ctx -> {
            String securityKey = authenticationService.createNewSecurityKey(10, 2);
            ctx.header("Server-Response", loginReg.login(ctx.queryParam("username"), ctx.queryParam("passwordHash")));
            ctx.result(securityKey);
                }
           );

//        app.post("/registration", ctx ->
//                ctx.result(loginReg.registration(ctx.queryParam("username"), ctx.queryParam("passwordHash"),
//                        ctx.queryParam("firstName"), ctx.queryParam("lastName"), ctx.queryParam("firstName"),
//                        ctx.queryParam("address"), ctx.queryParam("phone"), ctx.queryParam("email"))));
    }

    public static void main(String[] args) {
        new Service().start();
    }
}
