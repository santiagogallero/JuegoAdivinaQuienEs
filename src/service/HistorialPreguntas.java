package service;

import model.Filtro;

import java.util.ArrayList;
import java.util.List;

public class HistorialPreguntas {

    public interface Observador {
        void onPreguntaRegistrada(String jugador, Filtro filtro);
    }

    public void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    private final List<Filtro> preguntasRealizadas = new ArrayList<>();
    private final List<Observador> observadores = new ArrayList<>();

    public void suscribir(Observador observador) {
        observadores.add(observador);
    }

    public void registrarPregunta(String jugador, Filtro filtro) {
        preguntasRealizadas.add(filtro);
        System.out.println("[Historial] " + jugador + " pregunto: " + filtro.getDescripciones());

        for (Observador observador : observadores) {
            observador.onPreguntaRegistrada(jugador, filtro);
        }
    }

    public List<Filtro> getPreguntasRealizadas() {
        return preguntasRealizadas;
    }
}