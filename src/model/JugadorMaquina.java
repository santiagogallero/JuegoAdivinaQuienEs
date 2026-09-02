package model;
import service.EstrategiaMaquina;
import service.HistorialPreguntas;

import java.util.List;
import java.util.ArrayList;

public class JugadorMaquina extends Jugador implements HistorialPreguntas.Observador {

    private final EstrategiaMaquina estrategia;
    private final List<Filtro> filtrosUsados = new ArrayList<>();

    public JugadorMaquina(String nombre, Personaje secreto, List<Personaje> universoPersonajes, EstrategiaMaquina estrategia) {
        super(nombre, secreto, universoPersonajes);
        this.estrategia = estrategia;
    }

    @Override
    public Jugada decidirJugada(List<Filtro> filtrosDisponibles) {
        List<Filtro> filtrosRestantes = new ArrayList<>();
        for (Filtro filtro : filtrosDisponibles) {
            if (!filtrosUsados.contains(filtro)) {
                filtrosRestantes.add(filtro);
            }
        }

        Jugada jugada = estrategia.decidirJugada(this, filtrosRestantes);

        if (jugada.getTipo() == Jugada.Tipo.Preguntar) {
            filtrosUsados.add(jugada.getFiltro());
        }

        return jugada;
    }

    @Override
    public void onPreguntaRegistrada(String jugador, Filtro filtro) {
        if (!filtrosUsados.contains(filtro)) {
            filtrosUsados.add(filtro);
        }
    }
}