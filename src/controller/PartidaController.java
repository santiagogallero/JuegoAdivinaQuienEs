package controller;

import model.Filtro;
import model.Jugada;
import model.Jugador;
import model.JugadorHumano;
import model.JugadorMaquina;
import model.Personaje;
import service.Buscador;
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
import java.util.Scanner;

public class PartidaController {

    private final List<Personaje> personajesPorId;
    private final List<Personaje> personajesPorGenero;
    private final List<Filtro> filtrosBase;
    private final RecordService recordService;
    private final Buscador buscador = new Buscador();
    private final Random random = new Random();
    private final Scanner scanner;

    public PartidaController(Scanner scanner) {
        this.scanner = scanner;
        this.personajesPorId = GeneradorPersonaje.generarPersonajes();

        Ordenador<Personaje> ordenador = new OrdenadorPorGenero();
        this.personajesPorGenero = ordenador.ordenar(personajesPorId);

        this.filtrosBase = GeneradorFiltros.generarTodos();
        this.recordService = new RecordService();
    }

    public void mostrarPersonajes() {
        for (Personaje p : personajesPorGenero) {
            System.out.println(p.getId() + " - " + p.getNombre() + " (" + p.getGenero() + ")");
        }
    }

    public void mostrarMarcador() {
        recordService.mostrarTabla();
    }

    // ---------- Modo Humano vs Maquina ----------

    public void jugarHumanoVsMaquina(String nombreUsuario, boolean maquinaAsertiva) {
        Personaje secretoHumano = elegirSecretoHumano(scanner);

        List<Personaje> yaElegidos = new ArrayList<>();
        yaElegidos.add(secretoHumano);
        Personaje secretoMaquina = elegirSecretoAleatorioDistinto(yaElegidos);

        JugadorHumano humano = new JugadorHumano(nombreUsuario, secretoHumano, personajesPorId, scanner);

        EstrategiaMaquina estrategia = maquinaAsertiva ? new EstrategiaAsertiva() : new EstrategiaBasica();
        String nombreMaquina = maquinaAsertiva ? "Maquina Asertiva" : "Maquina Basica";
        JugadorMaquina maquina = new JugadorMaquina(nombreMaquina, secretoMaquina, personajesPorId, estrategia);

        System.out.println("\nListo, " + nombreUsuario + ". La " + nombreMaquina + " ya eligio su personaje secreto.");

        String ganador = jugarPartida(humano, maquina, null, false);

        System.out.println("\n>>> Gana " + ganador + " <<<");

        if (ganador.equals(nombreUsuario)) {
            recordService.registrarVictoria(nombreUsuario);
        }
    }

    // ---------- Modo Maquina vs Maquina (exhibicion) ----------

    public void jugarMaquinaVsMaquina() {
        List<Personaje> yaElegidos = new ArrayList<>();
        Personaje secreto1 = elegirSecretoAleatorioDistinto(yaElegidos);
        yaElegidos.add(secreto1);
        Personaje secreto2 = elegirSecretoAleatorioDistinto(yaElegidos);

        JugadorMaquina maquina1 = new JugadorMaquina("Maquina 1 (Basica)", secreto1, personajesPorId, new EstrategiaBasica());
        JugadorMaquina maquina2 = new JugadorMaquina("Maquina 2 (Asertiva)", secreto2, personajesPorId, new EstrategiaAsertiva());

        HistorialPreguntas historial = new HistorialPreguntas();
        historial.suscribir(maquina2); // Solo Maquina 2 conoce lo que pregunta Maquina 1

        System.out.println("\n### MODO MAQUINA VS MAQUINA ###");
        System.out.println("(secretos revelados solo para que se pueda seguir el proceso; las maquinas no se los dicen entre si)");
        System.out.println(maquina1.getNombre() + " eligio en secreto a: " + secreto1.getNombre());
        System.out.println(maquina2.getNombre() + " eligio en secreto a: " + secreto2.getNombre());

        String ganador = jugarPartida(maquina1, maquina2, historial, true);

        System.out.println("\n>>> Gana " + ganador + " <<<");
    }

    // ---------- Bucle de turnos compartido ----------

    private String jugarPartida(Jugador jugador1, Jugador jugador2, HistorialPreguntas historial, boolean mostrarProceso) {
        Jugador actual = jugador1;
        Jugador rival = jugador2;
        int turno = 1;

        while (true) {
            if (mostrarProceso) {
                System.out.println("\n--- Turno " + turno + ": " + actual.getNombre() + " ---");
                System.out.println("Candidatos restantes de " + actual.getNombre() + ": " + actual.getCandidatosRestantes().size());
            }

            Jugada jugada = actual.decidirJugada(filtrosBase);

            if (jugada.getTipo() == Jugada.Tipo.Adivinar) {
                Personaje adivinado = jugada.getPersonajeAdivinado();
                String nombreAdivinado = (adivinado != null) ? adivinado.getNombre() : "(id invalido)";
                System.out.println(actual.getNombre() + " arriesga una adivinanza: " + nombreAdivinado);

                if (adivinado != null && rival.esMiSecreto(adivinado)) {
                    System.out.println(actual.getNombre() + " ACERTO. Era " + adivinado.getNombre() + ".");
                    return actual.getNombre();
                } else {
                    System.out.println(actual.getNombre() + " se equivoco. Pierde la partida.");
                    return rival.getNombre();
                }
            } else {
                Filtro filtro = jugada.getFiltro();
                boolean respuesta = rival.respondeFiltro(filtro);
                System.out.println(actual.getNombre() + " pregunta: \"" + filtro.getDescripciones()
                        + "\" -> " + (respuesta ? "SI" : "NO"));

                actual.aplicarFiltro(filtro, respuesta);

                if (mostrarProceso) {
                    System.out.println("Candidatos de " + actual.getNombre() + " despues del filtro: "
                            + actual.getCandidatosRestantes().size());
                }

                if (historial != null) {
                    historial.registrarPregunta(actual.getNombre(), filtro);
                }
            }

            Jugador temp = actual;
            actual = rival;
            rival = temp;
            turno++;
        }
    }

    // ---------- Helpers de seleccion de personaje ----------

    private Personaje elegirSecretoHumano(Scanner scanner) {
        System.out.println("Elegi tu personaje secreto:");
        mostrarPersonajes();

        Personaje elegido = null;
        while (elegido == null) {
            System.out.print("Ingresa el id de tu personaje: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            elegido = buscador.buscarPorId(personajesPorId, id);
            if (elegido == null) {
                System.out.println("Id invalido, proba de nuevo.");
            }
        }
        return elegido;
    }

    private Personaje elegirSecretoAleatorioDistinto(List<Personaje> yaElegidos) {
        Personaje candidato;
        do {
            candidato = personajesPorId.get(random.nextInt(personajesPorId.size()));
        } while (yaElegidos.contains(candidato));
        return candidato;
    }
}
