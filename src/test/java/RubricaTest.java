import Controller.HelloController;
import Controller.MensalistaController;
import Model.Mensalista;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class RubricaTest {
    private final Context ctx = mock(Context.class);

    @BeforeEach
    void setUp() throws Exception {
        Field field = MensalistaController.class.getDeclaredField("mensalistas");
        field.setAccessible(true);
        List<Mensalista> mensalistas = (List<Mensalista>) field.get(null);
        mensalistas.clear();
        mensalistas.add(new Mensalista(1234,"Gustavo"));
        mensalistas.add(new Mensalista(2321,"Mario"));
    }
    @Test
    void deveRetornarHelloComStatus200() {
        when(ctx.status(200)).thenReturn(ctx);

        HelloController.handleHello(ctx);

        verify(ctx).status(200);
        verify(ctx).result("Hello, Javalin!");
    }

    @Test
    void deveCriarNovoMensalistaComStatus201() {

        Mensalista novoMensalista = new Mensalista(9999, "Novo");
        when(ctx.bodyAsClass(Mensalista.class)).thenReturn(novoMensalista);
        when(ctx.status(201)).thenReturn(ctx);

        MensalistaController.create(ctx);

        verify(ctx).status(201);

        verify(ctx).json(novoMensalista);
    }
    @Test
    void deveBuscarMensalistaRecemCriado() {
        Mensalista mensalistaCriado = new Mensalista(301, "Joana d'Arc");
        when(ctx.bodyAsClass(Mensalista.class)).thenReturn(mensalistaCriado);
        when(ctx.status(201)).thenReturn(ctx);

        MensalistaController.create(ctx);

        when(ctx.pathParam("matricula")).thenReturn("301");

        MensalistaController.getOne(ctx);

        ArgumentCaptor<Mensalista> mensalistaCaptor = ArgumentCaptor.forClass(Mensalista.class);
        verify(ctx, atLeastOnce()).json(mensalistaCaptor.capture());

        Mensalista mensalistaRecuperado = mensalistaCaptor.getValue();
        assertEquals(301, mensalistaRecuperado.matricula());
        assertEquals("Joana d'Arc", mensalistaRecuperado.nome());
    }

    @Test
    void deveNaoRetornarUmArrayVazioAposCriacao() {
        Mensalista novoMensalista = new Mensalista(401, "Carlos");
        when(ctx.bodyAsClass(Mensalista.class)).thenReturn(novoMensalista);
        when(ctx.status(201)).thenReturn(ctx);

        MensalistaController.create(ctx);

        ArgumentCaptor<List<Mensalista>> listaCaptor = ArgumentCaptor.forClass(List.class);

        MensalistaController.getAll(ctx);

        verify(ctx, atLeastOnce()).json(listaCaptor.capture());
        List<Mensalista> listaRetornada = listaCaptor.getValue();

        assertFalse(listaRetornada.isEmpty(), "A lista não deve estar vazia");
        assertEquals(3, listaRetornada.size());
    }
}
