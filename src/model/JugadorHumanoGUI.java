package model;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Version del jugador humano pensada para la interfaz grafica (Swing).
 * En vez de leer de consola, decidirJugada() bloquea el hilo de la partida
 * hasta que la ventana entregue la jugada elegida por el usuario mediante
 * responderConPregunta(...) o responderConAdivinanza(...).
 *
 * Esta clase corre en un hilo separado del Event Dispatch Thread de Swing;
 * la ventana es la que llama a los metodos "responderCon..." desde el EDT
 * cuando el usuario hace clic.
 */
public class JugadorHumanoGUI extends Jugador {

    private final BlockingQueue<Jugada> buzon = new ArrayBlockingQueue<>(1);

    public JugadorHumanoGUI(String nombre, Personaje secreto, List<Personaje> universoPersonajes) {
        super(nombre, secreto, universoPersonajes);
    }

    @Override
    public Jugada decidirJugada(List<Filtro> filtrosDisponibles) {
        try {
            return buzon.take(); // bloquea hasta que la GUI deposite una jugada
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Se interrumpio la espera de la jugada del usuario", e);
        }
    }

    /** Llamado desde la GUI cuando el usuario elige preguntar un filtro. */
    public void responderConPregunta(Filtro filtro) {
        buzon.offer(Jugada.crearJugadaPregunta(filtro));
    }

    /** Llamado desde la GUI cuando el usuario elige arriesgar una adivinanza. */
    public void responderConAdivinanza(Personaje personaje) {
        buzon.offer(Jugada.crearJugadaAdivinanza(personaje));
    }
}
