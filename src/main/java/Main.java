import Controller.HelloController;
import Controller.MensalistaController;
import io.javalin.Javalin;

import java.time.Instant;
import java.util.Map;


record Mensagem(String mensagem) {}
public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);

        //RUBRICA 1

        app.get("/hello", HelloController::handleHello);

        app.get("/status", ctx -> {
            String timestamp = Instant.now().toString();
            Map<String, String> response = Map.of(
                    "status", "ok",
                    "timestamp", timestamp
            );
            ctx.json(response);
        });

        app.post("/echo",ctx->{
            Mensagem mensagem = ctx.bodyAsClass(Mensagem.class);
            ctx.json(mensagem);
        });

        app.get("/saudacao/{nome}",ctx->{
            String nome = ctx.pathParam("nome");
            ctx.result("mensagem: Olá "+nome);
        });

//        RUBRICA 4

        app.get("/mensalistas", MensalistaController::getAll);
        app.get("/mensalistas/{matricula}", MensalistaController::getOne);
        app.post("/mensalistas", MensalistaController::create);
    }
}