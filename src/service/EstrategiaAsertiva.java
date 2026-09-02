package service;

import model.Filtro;
import model.Jugada;
import model.JugadorMaquina;
import model.Personaje;

import java.util.List;

public class EstrategiaAsertiva implements EstrategiaMaquina {

    @Override
    public Jugada decidirJugada(JugadorMaquina jugador, List<Filtro> filtrosDisponibles) {
        List<Personaje> candidatos = jugador.getCandidatosRestantes();

        if (candidatos.size() == 1) {
            return Jugada.crearJugadaAdivinanza(candidatos.get(0));
        }

        if (filtrosDisponibles.isEmpty()) {
            return Jugada.crearJugadaAdivinanza(candidatos.get(0));
        }

        Filtro mejorFiltro = null;
        int menorDiferencia = Integer.MAX_VALUE;

        for (Filtro filtro : filtrosDisponibles) {
            int cumplen = 0;
            for (Personaje p : candidatos) {
                if (filtro.cumple(p)) {
                    cumplen++;
                }
            }
            int noCumplen = candidatos.size() - cumplen;
            int diferencia = Math.abs(cumplen - noCumplen);

            if (diferencia < menorDiferencia) {
                menorDiferencia = diferencia;
                mejorFiltro = filtro;
            }
        }

        return Jugada.crearJugadaPregunta(mejorFiltro);
    }
}