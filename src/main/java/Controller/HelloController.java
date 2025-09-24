package Controller;

import io.javalin.http.Context;

public class HelloController {
    public static void handleHello(Context ctx) {
        ctx.status(200).result("Hello, Javalin!");
    }
}
