package service;

import model.Filtro;
import model.Jugada;
import model.JugadorMaquina;
import model.Personaje;

import java.util.List;

public class EstrategiaBasica implements EstrategiaMaquina {

    @Override
    public Jugada decidirJugada(JugadorMaquina jugador, List<Filtro> filtrosDisponibles) {
        List<Personaje> candidatos = jugador.getCandidatosRestantes();

        if (candidatos.size() == 1) {
            return Jugada.crearJugadaAdivinanza(candidatos.get(0));
        }

        if (filtrosDisponibles.isEmpty()) {
            return Jugada.crearJugadaAdivinanza(candidatos.get(0));
        }

        return Jugada.crearJugadaPregunta(filtrosDisponibles.get(0));
    }
}