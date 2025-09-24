package Controller;

import Model.Mensalista;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MensalistaController {
    private static List<Mensalista> mensalistas = new ArrayList<>();
    static {
        mensalistas.add(new Mensalista(1234,"Gustavo"));
        mensalistas.add(new Mensalista(2321,"Mario"));
    }

    public static void getAll(Context ctx) {
        ctx.json(mensalistas);
    }

    public static void getOne(Context ctx) {
        try {
            int matricula = Integer.parseInt(ctx.pathParam("matricula"));
            Optional<Mensalista> mensalistaEncontrado = mensalistas.stream()
                    .filter(m -> m.matricula() == matricula)
                    .findFirst();

            if (mensalistaEncontrado.isPresent()) {
                ctx.json(mensalistaEncontrado.get());
            } else {
                ctx.status(404).json(Map.of("erro", "Mensalista com a matrícula " + matricula + " não encontrado."));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("erro", "A matrícula informada não é um número válido."));
        }
    }

    public static void create(Context ctx){
        Mensalista novoMensalista = ctx.bodyAsClass(Mensalista.class);

        if (novoMensalista.nome() == null || novoMensalista.nome().trim().isEmpty()) {
            ctx.status(400).json(Map.of("erro", "O campo 'nome' é obrigatório."));
            return;
        }
        mensalistas.add(novoMensalista);
        ctx.status(201).json(novoMensalista);
    }
}
