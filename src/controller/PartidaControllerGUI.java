package controller;

import model.Filtro;
import model.Jugada;
import model.Jugador;
import model.JugadorHumanoGUI;
import model.JugadorMaquina;
import model.Marcador;
import model.Personaje;
import service.EstrategiaAsertiva;
import service.EstrategiaBasica;
import service.EstrategiaMaquina;
import service.GeneradorFiltros;
import service.GeneradorPersonaje;
import service.HistorialPreguntas;
import service.Ordenador;
import service.OrdenadorPorGenero;
import service.RecordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Version del PartidaController pensada para la interfaz grafica.
 * No usa Scanner ni System.out: toda la comunicacion con el usuario pasa
 * por la interfaz PartidaListener, que la ventana Swing implementa.
 * El motor del juego (model/service) es el mismo que usa la version consola.
 */
public class PartidaControllerGUI {

    public interface PartidaListener {
        void onTurno(String nombreJugadorEnTurno, int candidatosRestantes, boolean esHumano);
        void onPregunta(String nombreQuePregunta, String descripcionFiltro, boolean respuesta);
        void onAdivinanza(String nombreQueAdivina, String personajeAdivinado, boolean acerto);
        void onFinDePartida(String ganador);
    }

    private final List<Personaje> personajesPorId;
    private final List<Personaje> personajesPorGenero;
    private final List<Filtro> filtrosBase;
    private final RecordService recordService;
    private final Random random = new Random();

    public PartidaControllerGUI() {
        this.personajesPorId = GeneradorPersonaje.generarPersonajes();

        Ordenador<Personaje> ordenador = new OrdenadorPorGenero();
        this.personajesPorGenero = ordenador.ordenar(personajesPorId);

        this.filtrosBase = GeneradorFiltros.generarTodos();
        this.recordService = new RecordService();
    }

    public List<Personaje> getPersonajesPorGenero() {
        return personajesPorGenero;
    }

    public List<Filtro> getFiltrosBase() {
        return filtrosBase;
    }

    public List<Marcador> obtenerMarcadores() {
        return recordService.obtenerTablaOrdenada();
    }

    /**
     * Arranca una partida Humano vs Maquina en un hilo aparte (para no bloquear el EDT).
     * Devuelve el JugadorHumanoGUI para que la ventana le entregue las jugadas del usuario.
     */
    public JugadorHumanoGUI iniciarHumanoVsMaquina(String nombreUsuario, Personaje secretoHumano,
                                                    boolean maquinaAsertiva, PartidaListener listener) {
        List<Personaje> yaElegidos = new ArrayList<>();
        yaElegidos.add(secretoHumano);
        Personaje secretoMaquina = elegirSecretoAleatorioDistinto(yaElegidos);

        JugadorHumanoGUI humano = new JugadorHumanoGUI(nombreUsuario, secretoHumano, personajesPorId);

        EstrategiaMaquina estrategia = maquinaAsertiva ? new EstrategiaAsertiva() : new EstrategiaBasica();
        String nombreMaquina = maquinaAsertiva ? "Maquina Asertiva" : "Maquina Basica";
        JugadorMaquina maquina = new JugadorMaquina(nombreMaquina, secretoMaquina, personajesPorId, estrategia);

        Thread hiloPartida = new Thread(() -> {
            String ganador = jugarPartida(humano, maquina, null, listener);
            if (ganador.equals(nombreUsuario)) {
                recordService.registrarVictoria(nombreUsuario);
            }
            listener.onFinDePartida(ganador);
        }, "hilo-partida-humano-vs-maquina");
        hiloPartida.setDaemon(true);
        hiloPartida.start();

        return humano;
    }

    /** Arranca una partida Maquina vs Maquina (exhibicion) en un hilo aparte. */
    public void iniciarMaquinaVsMaquina(PartidaListener listener) {
        List<Personaje> yaElegidos = new ArrayList<>();
        Personaje secreto1 = elegirSecretoAleatorioDistinto(yaElegidos);
        yaElegidos.add(secreto1);
        Personaje secreto2 = elegirSecretoAleatorioDistinto(yaElegidos);

        JugadorMaquina maquina1 = new JugadorMaquina("Maquina 1 (Basica)", secreto1, personajesPorId, new EstrategiaBasica());
        JugadorMaquina maquina2 = new JugadorMaquina("Maquina 2 (Asertiva)", secreto2, personajesPorId, new EstrategiaAsertiva());

        HistorialPreguntas historial = new HistorialPreguntas();
        historial.suscribir(maquina2);

        Thread hiloPartida = new Thread(() -> {
            String ganador = jugarPartida(maquina1, maquina2, historial, listener);
            listener.onFinDePartida(ganador);
        }, "hilo-partida-maquina-vs-maquina");
        hiloPartida.setDaemon(true);
        hiloPartida.start();
    }

    private String jugarPartida(Jugador jugador1, Jugador jugador2, HistorialPreguntas historial,
                                 PartidaListener listener) {
        Jugador actual = jugador1;
        Jugador rival = jugador2;

        while (true) {
            boolean esHumano = actual instanceof JugadorHumanoGUI;
            listener.onTurno(actual.getNombre(), actual.getCandidatosRestantes().size(), esHumano);

            Jugada jugada = actual.decidirJugada(filtrosBase);

            if (jugada.getTipo() == Jugada.Tipo.Adivinar) {
                Personaje adivinado = jugada.getPersonajeAdivinado();
                String nombreAdivinado = (adivinado != null) ? adivinado.getNombre() : "(id invalido)";
                boolean acerto = adivinado != null && rival.esMiSecreto(adivinado);

                listener.onAdivinanza(actual.getNombre(), nombreAdivinado, acerto);

                return acerto ? actual.getNombre() : rival.getNombre();
            } else {
                Filtro filtro = jugada.getFiltro();
                boolean respuesta = rival.respondeFiltro(filtro);

                listener.onPregunta(actual.getNombre(), filtro.getDescripciones(), respuesta);

                actual.aplicarFiltro(filtro, respuesta);

                if (historial != null) {
                    historial.registrarPregunta(actual.getNombre(), filtro);
                }
            }

            Jugador temp = actual;
            actual = rival;
            rival = temp;
        }
    }

    private Personaje elegirSecretoAleatorioDistinto(List<Personaje> yaElegidos) {
        Personaje candidato;
        do {
            candidato = personajesPorId.get(random.nextInt(personajesPorId.size()));
        } while (yaElegidos.contains(candidato));
        return candidato;
    }
}
