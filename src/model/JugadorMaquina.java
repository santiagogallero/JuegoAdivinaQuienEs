package model;
import service.EstrategiaMaquina;

import java.util.List;

public class JugadorMaquina extends Jugador {    
    private final EstrategiaMaquina estrategia;

    public JugadorMaquina(String nombre, Personaje secreto, List<Personaje> universoPersonajes, EstrategiaMaquina estrategia) {
        super(nombre, secreto, universoPersonajes);
        this.estrategia = estrategia;
    }
    @Override
    public Jugada decidirJugada(List<Filtro> filtrosDisponibles) {
        return estrategia.decidirJugada(this, filtrosDisponibles);
    }
    
}
