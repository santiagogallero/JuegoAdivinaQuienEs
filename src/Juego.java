import model.Filtro;
import model.Personaje;
import service.Buscador;
import service.GeneradorFiltros;
import service.GeneradorPersonaje;
import service.OrdenadorPorGenero;
import java.util.List;
import java.util.Random;

public class Juego {

    private final List<Personaje> personajes;
    private final List<Filtro> filtros;
    private final Buscador buscador;

    public Juego() {
        OrdenadorPorGenero ordenador = new OrdenadorPorGenero();
        this.personajes = ordenador.ordenarPersonajes(GeneradorPersonaje.generarPersonajes());
        this.filtros = GeneradorFiltros.generarTodos();
        this.buscador = new Buscador();
    }

    public Personaje obtenerElegido() {
        Random random = new Random();
        return personajes.get(random.nextInt(personajes.size()));
    }

    public String conocerElegido(int id) {
        Personaje p = buscador.buscarPorId(personajes, id);
        if (p == null) {
            return "Personaje con id " + id + " no encontrado";
        }
        return p.getNombre();
    }

    public String comodin(int idElegido) {
        Personaje elegido = buscador.buscarPorId(personajes, idElegido);

        if (elegido == null) {
            return "Error: personaje no encontrado.";
        }

        String pistas = "Pistas sobre el elegido:\n";

        for (Filtro filtro : filtros) {
            if (filtro.cumple(elegido)) {
                // Si lo cumple, le sumamos la pista al texto
                pistas += "  ✓ " + filtro.getDescripciones() + "\n";
            }
        }

        return pistas;
    }

    public void mostrarPersonajes() {
        for (Personaje p : personajes) {
            System.out.println("[" + p.getId() + "] " + p.getNombre());
        }
    }
}