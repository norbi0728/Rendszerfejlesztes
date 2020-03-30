package marketplace.service;

import io.javalin.Javalin;
import marketplace.logic.Logic;

public class Service {
    private static Logic logic;

    public Service() {
        this.logic = new Logic();
    }
    //###############USE####################
    //localhost:7000/login?username=Lajos1&passwordHash=1234
    //localhost:7000/registration?username=Lajos1&passwordHash=1234
    private void start() {
        Javalin app = Javalin.create().start(7000);

        app.post("/login", ctx ->
           ctx.result(logic.login(ctx.queryParam("username"), ctx.queryParam("passwordHash"))));


        app.post("/registration", ctx ->
                ctx.result(logic.registration(ctx.queryParam("username"), ctx.queryParam("passwordHash"))));
    }

    public static void main(String[] args) {
        new Service().start();
    }
}
