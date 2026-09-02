package service;

import model.Filtro;
import model.Jugada;
import model.JugadorMaquina;

import java.util.List;

public interface EstrategiaMaquina {
    Jugada decidirJugada(JugadorMaquina jugador, List<Filtro> filtrosDisponibles);
}